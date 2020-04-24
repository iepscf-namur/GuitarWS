package org.guitar.WS.Services;

import org.guitar.DAO.Beans.Song;
import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.ISongDAO;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.WS.Errors.JsonErrorBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class SongServiceImpl implements ISongServices {

    private static SongServiceImpl _instance = null;
    private static GetPropertyValues propertyValues;

    private SongServiceImpl() {
        //Getting Properties Values needed to get schema names
        propertyValues = new GetPropertyValues();
        try {
            propertyValues.getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SongServiceImpl getInstance() {
        if (SongServiceImpl._instance == null) {
            SongServiceImpl._instance = new SongServiceImpl();
        }

        return SongServiceImpl._instance;
    }
    
    @Override
    public JsonArray getSongsJson() {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ISongDAO songDAO = daoFactory.getSongDAO();
        List<Song> songs = songDAO.GetSongs();

        JsonArray songsJsonArray = new JsonArray();

        for (Song song : songs) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("id", song.getId());
            jsonObj.addProperty("idCatalogSong", song.getIdCatalogSong());
            jsonObj.addProperty("song", song.getSong());

            songsJsonArray.add(jsonObj);
        }
        return songsJsonArray;
    }

    @Override
    public JsonObject getSongJson(int idCatalogSong) {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ISongDAO songDAO = daoFactory.getSongDAO();

        Song song = songDAO.GetSong(idCatalogSong);
        JsonObject jsonObj = null;

        if (song != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", song.getId());
            jsonObj.addProperty("idCatalogSong", song.getIdCatalogSong());
            jsonObj.addProperty("song", song.getSong());
        }
        return jsonObj;
    }

    @Override
    public JsonObject addSongJson(JsonObject songJsonObj) {

        if (!isSongValid(songJsonObj)) {
            //FIXME This error will never been shown up as it will be encapsulated in a Servlet TRY
            return JsonErrorBuilder.getJsonObject(400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ISongDAO songDAO = daoFactory.getSongDAO();

        JsonObject jsonObj = null;

        // Do not allow song creation if songTitle already exists
        //FIXME DB Will handle it, should I handle in the Service too ?

        Song song = new Song();

        song.setIdCatalogSong(songJsonObj.get("idCatalogSong").getAsInt());
        song.setSong(songJsonObj.get("song").getAsString());
        
        // We insert the song and check if it has been correctly inserted into the db
        Song lastInsertSong = songDAO.AddSong(song);

        // FIXME rename or extends JsonBuilder so we do not use "error" here
        //jsonResponse = JsonErrorBuilder.getJsonObject(201, "User " + lastInsertLogin + " has been sucesfully created");

        if(lastInsertSong != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", lastInsertSong.getId());
            jsonObj.addProperty("idCatalogSong", lastInsertSong.getIdCatalogSong());
            jsonObj.addProperty("song", lastInsertSong.getSong());
        }
        return jsonObj;
    }

    @Override
    public JsonObject deleteSongJson(int idCatalogSong) {
        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ISongDAO songDAO = daoFactory.getSongDAO();

        //Checking if song actually exists
        Song song = songDAO.GetSong(idCatalogSong);
        if(song != null) {

            if(songDAO.DeleteSong(song.getId())) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "Song deleted");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Song not found");
        }
        return jsonResponse;
    }

    @Override
    public JsonObject updateSongJson(JsonObject jsonObject) {

        if(!isSongValid(jsonObject) && jsonObject.has("oldIdCatalogSong")) {
            //FIXME This error will never show up as it will be encapsulated in a Servlet TRY
            return JsonErrorBuilder.getJsonObject(400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ISongDAO songDAO = daoFactory.getSongDAO();

        JsonObject jsonResponse = null;

        Integer oldIdCatalogSong = jsonObject.get("oldIdCatalogSong").getAsInt();
        Song song = songDAO.GetSong(jsonObject.get("oldIdCatalogSong").getAsInt());

        if(song != null) {
            // TODO Send message if field provided does not exist ?
            if(jsonObject.has("id")) {
                song.setId(jsonObject.get("id").getAsInt());
            }
            if(jsonObject.has("idCatalogSong")) {
                song.setIdCatalogSong(jsonObject.get("idCatalogSong").getAsInt());
            }
            if(jsonObject.has("song")) {
                song.setSong(jsonObject.get("song").getAsString());
            }

            if(songDAO.UpdateSong(song, oldIdCatalogSong)) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "Song " + song.getId() + " updated");
            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(400, "Song found but not updated");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Song not found");
        }

        return jsonResponse;
    }

    private boolean isSongValid(JsonObject jsonObj) {
        return  jsonObj != null &&
                jsonObj.has("idCatalogSong") &&
                jsonObj.has("song");
    }
}
