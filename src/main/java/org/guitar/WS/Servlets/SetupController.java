package org.guitar.WS.Servlets;

import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.WS.Errors.JsonErrorBuilder;
import org.guitar.WS.Utils.ServletUtils;
import org.guitar.WS.Services.SetupServiceImpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SetupController extends HttpServlet {
       
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// We need to set Response Header's Content-Type and CharacterEncoding before sending it to the client
        ServletUtils.setResponseSettings(response);

        // If any parameters are given (request URL is /setup or /setup/)
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            JsonObject jsonResponse = JsonErrorBuilder.getJsonObject(404,
                    request.getServletPath() + request.getPathInfo() + " is not a supported GET url");
            response.getWriter().write(jsonResponse.toString());

            // If some parameters are given (request URL is /setup{idCatalogSong})
        } else if(request.getPathInfo().substring(1).length()> 0) {
            JsonObject jsonResponse =  null;
            // Remove "/" at the beginning of the string as well as "
            String idCatalogSong = "";
            idCatalogSong = request.getPathInfo();
            idCatalogSong = idCatalogSong.substring(2, idCatalogSong.length() -1);
	        int i = Integer.parseInt(idCatalogSong); 
	        
            jsonResponse = SetupServiceImpl.getInstance().getSetupJson(i);

            if(jsonResponse == null) {
                //FIXME WILL NEVER HAPPENS CAUSE OF ERROR HANDLING IN SERVICE
                jsonResponse = JsonErrorBuilder.getJsonObject(404, "setup not found");
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

        // If some parameters are given (request URL is /setup{setup})
        if (request.getPathInfo().substring(1).length()> 0) {
            // Remove "/" at the beginning of the string
            String idCatalogSong = "";
            idCatalogSong = request.getPathInfo();
            idCatalogSong = idCatalogSong.substring(2, idCatalogSong.length() -1);
	        int i = Integer.parseInt(idCatalogSong); 

            jsonResponse = SetupServiceImpl.getInstance().deleteSetupJson(i);

            response.setStatus(jsonResponse.get("code").getAsInt());

        } else {

            jsonResponse = JsonErrorBuilder.getJsonObject(404,
                    request.getServletPath() + 
                            (request.getPathInfo() == null ? "" : request.getPathInfo()) +
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

        // If some parameters are given (request URL is /setup{idCatalogSong})
        if (request.getPathInfo().substring(1).length()> 0) {
            // Remove "/" at the beginning of the string as well as "
            String idCatalogSong = "";
            idCatalogSong = request.getPathInfo();
            idCatalogSong = idCatalogSong.substring(2, idCatalogSong.length() -1);
            int i = Integer.parseInt(idCatalogSong);

            try {
                JsonObject setup = ServletUtils.readBody(request);
                setup.addProperty("idCatalogSong", i);
                jsonResponse = SetupServiceImpl.getInstance().updateSetupJson(setup);

                if (jsonResponse == null) {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                    jsonResponse = JsonErrorBuilder.getJsonObject(500, "Setup not updated");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());

                } else {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WORK WITH SUCCESS HTTP REQUESTS + RETRIEVE A COMPLETE SETUP OBJECT
                    //jsonResponse = JsonErrorBuilder.getJsonObject(201, "Setup " + jsonResponse.get("songTitle").getAsString() + " has been sucesfully updated");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());
                }

            } catch(Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500, "An error occurred while processing the data provided (PUT SetupController)");
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
