package org.guitar.WS.Servlets;

import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.WS.Errors.JsonErrorBuilder;
import org.guitar.WS.Utils.ServletUtils;
import org.guitar.WS.Services.CatalogServiceImpl;
import org.guitar.WS.Services.SongServiceImpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SongController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// We need to set Response Header's Content-Type and CharacterEncoding before sending it to the client
        ServletUtils.setResponseSettings(response);

        JsonObject jsonResponse = null;

        // If any parameters are given (request URL is /songs or /songs/)
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            // Read the body content and send it to SongService for song creation
            try {
                // Transform the Json String from the body content into a Json object
            	//JsonObject song = ServletUtils.readBody(request);
                JsonObject temp = ServletUtils.readBody(request);
                
                // Create the Json catalog
                JsonObject catalog = new JsonObject();
                catalog.addProperty("artistName", temp.get("artistName").getAsString());
                catalog.addProperty("songTitle", temp.get("songTitle").getAsString());
                
                jsonResponse = CatalogServiceImpl.getInstance().addCatalogJson(catalog);
                
                if (jsonResponse == null) {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                    jsonResponse = JsonErrorBuilder.getJsonObject(500, "Catalog not created");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());

                } else {
                	// Search the new idCatalogSong just created
                	Integer idCatalogSong = jsonResponse.get("idSong").getAsInt();

                	// Create the Json song
                    JsonObject song = new JsonObject();
                    song.addProperty("idCatalogSong", jsonResponse.get("idSong").getAsInt());
                    song.addProperty("song", temp.get("song").getAsString());

                    jsonResponse = SongServiceImpl.getInstance().addSongJson(song);

                    if (jsonResponse == null) {
                        //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                        jsonResponse = JsonErrorBuilder.getJsonObject(500, "Song not created");
                        response.setStatus(jsonResponse.get("code").getAsInt());
                        //response.getWriter().write(jsonResponse.toString());

                    } else {
                        //FIXME CREATING AN ERROR BUILDER THAT COULD WORK WITH SUCCESS HTTP REQUESTS
                        jsonResponse = JsonErrorBuilder.getJsonObject(201, "Song " +
                        		jsonResponse.get("id").getAsInt() + " has been sucesfully created");
                        response.setStatus(jsonResponse.get("code").getAsInt());
                        //response.getWriter().write(jsonResponse.toString());
                    }
                
                }

            } catch(Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(500,
                        "An error occurred while processing the data provided (POST SongController)");
                response.setStatus(jsonResponse.get("code").getAsInt());
                e.printStackTrace();
            }
            
          } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404,
                    request.getServletPath() + request.getPathInfo() + " is not a supported POST url");
            response.setStatus(jsonResponse.get("code").getAsInt());
            //response.getWriter().write(jsonResponse.toString());
          }

        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// We need to set Response Header's Content-Type and CharacterEncoding before sending it to the client
        ServletUtils.setResponseSettings(response);

        // If any parameters are given (request URL is /songs or /songs/)
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            JsonArray jsonResponse = SongServiceImpl.getInstance().getSongsJson();
            response.setStatus(200);
            response.getWriter().write(jsonResponse.toString());
            return;

            // If some parameters are given (request URL is /song{idCatalogSong})
        } else if(request.getPathInfo().substring(1).length()> 0) {
            JsonObject jsonResponse =  null;
            
            // Remove "/" at the beginning of the string as well as "
            String idCatalogSong = "";
            idCatalogSong = request.getPathInfo();
            idCatalogSong = idCatalogSong.substring(2, idCatalogSong.length() -1);
	        int i = Integer.parseInt(idCatalogSong);    
        
            jsonResponse = SongServiceImpl.getInstance().getSongJson(i);

            if(jsonResponse == null) {
                //FIXME WILL NEVER HAPPENS CAUSE OF ERROR HANDLING IN SERVICE
                jsonResponse = JsonErrorBuilder.getJsonObject(404, "song not found");
                response.setStatus(jsonResponse.get("code").getAsInt());
                //response.getWriter().write(jsonResponse.toString());
            } else {
                response.setStatus(200);
                //response.getWriter().write(jsonResponse.toString());
            }

            response.getWriter().write(jsonResponse.toString());

            // else the request URL is not supported
        } else {
            JsonObject jsonResponse = JsonErrorBuilder.getJsonObject(404,
                    request.getServletPath() + request.getPathInfo() + " is not a supported GET url");
            response.setStatus(jsonResponse.get("code").getAsInt());
            response.getWriter().write(jsonResponse.toString());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // We need to set Response Header's Content-Type and CharacterEncoding before sending it to the client
        ServletUtils.setResponseSettings(response);

        JsonObject jsonResponse = null;

        // If some parameters are given (request URL is /songs{songTitle})
        if (request.getPathInfo().substring(1).length()> 0) {
            // Remove "/" at the beginning of the string
            String songTitle = "";
            songTitle = request.getPathInfo();
            songTitle = songTitle.substring(2, songTitle.length() -1);

            // delete the song from the catalog but previously, take the idCatalogSong
            jsonResponse = CatalogServiceImpl.getInstance().getCatalogJson(songTitle);
        	Integer idCatalogSong = jsonResponse.get("idSong").getAsInt();
        	jsonResponse = CatalogServiceImpl.getInstance().deleteCatalogJson(songTitle);

            // now delete the song from the song repository
        	jsonResponse = SongServiceImpl.getInstance().deleteSongJson(idCatalogSong);

            response.setStatus(jsonResponse.get("code").getAsInt());

        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404,
                    request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo()) +
                            " is not a supported DELETE url");
            response.setStatus(jsonResponse.get("code").getAsInt());

    	}

        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// We need to set Response Header's Content-Type and CharacterEncoding before sending it to the client
        ServletUtils.setResponseSettings(response);

        JsonObject jsonResponse = null;

        // If some parameters are given (request URL is /songs{songTitle})
        if (request.getPathInfo().substring(1).length()> 0) {
        	// Remove "/" at the beginning of the string as well as "
            String oldSongTitle = "";
            oldSongTitle = request.getPathInfo();
            oldSongTitle = oldSongTitle.substring(2, oldSongTitle.length() -1);

            try {
                // Transform the Json String from the body content into a Json object
            	// JsonObject song = ServletUtils.readBody(request);
                JsonObject temp = ServletUtils.readBody(request);
                
                // Create the Json catalog
                JsonObject catalog = new JsonObject();
                catalog.addProperty("artistName", temp.get("artistName").getAsString());
                catalog.addProperty("songTitle", temp.get("songTitle").getAsString());
                catalog.addProperty("oldSongTitle", oldSongTitle);
                
                jsonResponse = CatalogServiceImpl.getInstance().updateCatalogJson(catalog);
               
                if (jsonResponse == null) {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                    jsonResponse = JsonErrorBuilder.getJsonObject(500, "Song not updated");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());

                } else {
                	
                	// Create the Json song
                	JsonObject song = new JsonObject();
                	song.addProperty("oldIdCatalogSong", jsonResponse.get("idCatalogSong").getAsInt());
                	song.addProperty("idCatalogSong", jsonResponse.get("idCatalogSong").getAsInt());
                	song.addProperty("song",temp.get("song").getAsString());
                	
                	jsonResponse = SongServiceImpl.getInstance().updateSongJson(song);

                    if (jsonResponse == null) {
                        jsonResponse = JsonErrorBuilder.getJsonObject(500, "Song not updated");
                        response.setStatus(jsonResponse.get("code").getAsInt());
                    } else {
                    	response.setStatus(jsonResponse.get("code").getAsInt());
                    }
                }

            } catch(Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500, "An error occurred while processing the data provided (PUT SongController)");
                response.setStatus(jsonResponse.get("code").getAsInt());
                e.printStackTrace();
            }

        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404,
                    request.getServletPath() +
                            (request.getPathInfo() == null ? "" : request.getPathInfo()) +
                            " is not a supported PUT url");
            response.setStatus(jsonResponse.get("code").getAsInt());
        }

        response.getWriter().write(jsonResponse.toString());
    }
    
}

