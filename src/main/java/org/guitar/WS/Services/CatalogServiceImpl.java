package org.guitar.WS.Services;

import org.guitar.DAO.Beans.Catalog;
import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.ICatalogDAO;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.WS.Errors.JsonErrorBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class CatalogServiceImpl implements ICatalogServices {

    private static CatalogServiceImpl _instance = null;
    private static GetPropertyValues propertyValues;

    private CatalogServiceImpl() {
        //Getting Properties Values needed to get schema names
        propertyValues = new GetPropertyValues();
        try {
            propertyValues.getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CatalogServiceImpl getInstance() {
        if (CatalogServiceImpl._instance == null) {
            CatalogServiceImpl._instance = new CatalogServiceImpl();
        }

        return CatalogServiceImpl._instance;
    }

    @Override
    public JsonArray getCatalogsJson() {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ICatalogDAO catalogDAO = daoFactory.getCatalogDAO();
        List<Catalog> catalogs = catalogDAO.GetCatalogs();

        JsonArray catalogsJsonArray = new JsonArray();

        for (Catalog catalog : catalogs) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("idSong", catalog.getIdSong());
            jsonObj.addProperty("artistName", catalog.getArtistName());
            jsonObj.addProperty("songTitle", catalog.getSongTitle());

            catalogsJsonArray.add(jsonObj);
        }
        return catalogsJsonArray;
    }

    @Override
    public JsonObject getCatalogJson(String songTitle) {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ICatalogDAO catalogDAO = daoFactory.getCatalogDAO();

        Catalog catalog = catalogDAO.GetCatalog(songTitle);
        JsonObject jsonObj = null;

        if (catalog != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("idSong", catalog.getIdSong());
            jsonObj.addProperty("artistName", catalog.getArtistName());
            jsonObj.addProperty("songTitle", catalog.getSongTitle());
        }
        return jsonObj;
    }

    @Override
    public JsonObject addCatalogJson(JsonObject catalogJsonObj) {

        if (!isCatalogValid(catalogJsonObj)) {
            //FIXME This error will never been shown up as it will be encapsulated in a Servlet TRY
            return JsonErrorBuilder.getJsonObject(400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ICatalogDAO catalogDAO = daoFactory.getCatalogDAO();

        JsonObject jsonObj = null;

        // Do not allow catalog creation if songTitle already exists
        //FIXME DB Will handle it, should I handle in the Service too ?

        Catalog catalog = new Catalog();

        catalog.setArtistName(catalogJsonObj.get("artistName").getAsString());
        catalog.setSongTitle(catalogJsonObj.get("songTitle").getAsString());
        
        // We insert the catalog and check if it has been correctly inserted into the db
        Catalog lastInsertCatalog = catalogDAO.AddCatalog(catalog);

        // FIXME rename or extends JsonBuilder so we do not use "error" here
        //jsonResponse = JsonErrorBuilder.getJsonObject(201, "User " + lastInsertLogin + " has been sucesfully created");

        if(lastInsertCatalog != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("idSong", lastInsertCatalog.getIdSong());
            jsonObj.addProperty("artistName", lastInsertCatalog.getArtistName());
            jsonObj.addProperty("songTitle", lastInsertCatalog.getSongTitle());
        }
        return jsonObj;
    }

    @Override
    public JsonObject deleteCatalogJson(String songTitle) {
        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ICatalogDAO catalogDAO = daoFactory.getCatalogDAO();

        //Checking if catalog actually exists
        Catalog catalog = catalogDAO.GetCatalog(songTitle);
        if(catalog != null) {

            if(catalogDAO.DeleteCatalog(catalog.getIdSong())) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "Catalog deleted");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Catalog not found");
        }
        return jsonResponse;
    }

    @Override
    public JsonObject updateCatalogJson(JsonObject jsonObject) {

        if(!isCatalogValid(jsonObject) && jsonObject.has("oldSongtitle")) {
            //FIXME This error will never show up as it will be encapsulated in a Servlet TRY
            return JsonErrorBuilder.getJsonObject(400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ICatalogDAO catalogDAO = daoFactory.getCatalogDAO();

        JsonObject jsonResponse = null;

        String oldSongTitle = jsonObject.get("oldSongTitle").getAsString();
        Catalog catalog = catalogDAO.GetCatalog(jsonObject.get("oldSongTitle").getAsString());

        if(catalog != null) {
            // TODO Send message if field provided does not exist ?
            if(jsonObject.has("idSong")) {
                catalog.setIdSong(jsonObject.get("idSong").getAsInt());
            }
            if(jsonObject.has("artistName")) {
                catalog.setArtistName(jsonObject.get("artistName").getAsString());
            }
            if(jsonObject.has("songTitle")) {
                catalog.setSongTitle(jsonObject.get("songTitle").getAsString());
            }

            if(catalogDAO.UpdateCatalog(catalog, oldSongTitle)) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "Catalog " + catalog.getIdSong() + " updated");
            	jsonResponse.addProperty("idCatalogSong",catalog.getIdSong());

            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(400, "Catalog found but not updated");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Catalog not found");
        }

        return jsonResponse;
    }

    private boolean isCatalogValid(JsonObject jsonObj) {
        return  jsonObj != null &&
                jsonObj.has("artistName") &&
                jsonObj.has("songTitle");
    }
}
