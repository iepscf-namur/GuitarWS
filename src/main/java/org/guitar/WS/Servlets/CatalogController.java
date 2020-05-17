package org.guitar.WS.Servlets;

import org.guitar.WS.Errors.JsonErrorBuilder;
import org.guitar.WS.Utils.ServletUtils;
import org.guitar.WS.Services.CatalogServiceImpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CatalogController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// We need to set Response Header's Content-Type and CharacterEncoding before sending it to the client
        ServletUtils.setResponseSettings(response);

        JsonObject jsonResponse = null;

        // If any parameters are given (request URL is /catalog or /catalog/)
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            // Read the body content and send it to CatalogService for catalog creation
            try {
                // Transform the Json String from the body content into a Json object
                JsonObject catalog = ServletUtils.readBody(request);
                jsonResponse = CatalogServiceImpl.getInstance().addCatalogJson(catalog);

                if (jsonResponse == null) {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                    jsonResponse = JsonErrorBuilder.getJsonObject(500, "Catalog not created");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());

                } else {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WORK WITH SUCCESS HTTP REQUESTS
                    jsonResponse = JsonErrorBuilder.getJsonObject(201, "Catalog " +
                    		jsonResponse.get("idSong").getAsInt() + " has been sucesfully created");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());
                }

            } catch(Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(500,
                        "An error occurred while processing the data provided (POST CatalogController)");
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

        // If any parameters are given (request URL is /catalog or /catalog/)
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            JsonArray jsonResponse = CatalogServiceImpl.getInstance().getCatalogsJson();
            response.setStatus(200);
            response.getWriter().write(jsonResponse.toString());
            return;

            // If some parameters are given (request URL is /catalog{songTitle})
        } else if(request.getPathInfo().substring(1).length()> 0) {
            JsonObject jsonResponse =  null;
            // Remove "/" at the beginning of the string as well as "
            String songTitle = "";
            songTitle = request.getPathInfo();
            songTitle = songTitle.substring(2, songTitle.length() -1);
            
            jsonResponse = CatalogServiceImpl.getInstance().getCatalogJson(songTitle);

            if(jsonResponse == null) {
                //FIXME WILL NEVER HAPPENS CAUSE OF ERROR HANDLING IN SERVICE
                jsonResponse = JsonErrorBuilder.getJsonObject(404, "catalog not found");
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

        // If some parameters are given (request URL is /catalogs{songTitle})
        if (request.getPathInfo().substring(1).length()> 0) {
            // Remove "/" at the beginning of the string
            String songTitle = "";
            songTitle = request.getPathInfo();
            songTitle = songTitle.substring(2, songTitle.length() -1);

            jsonResponse = CatalogServiceImpl.getInstance().deleteCatalogJson(songTitle);

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

        // If some parameters are given (request URL is /catalog{songTitle})
        if (request.getPathInfo().substring(1).length()> 0) {
            // Remove "/" at the beginning of the string as well as "
            String songTitle = "";
            songTitle = request.getPathInfo();
            songTitle = songTitle.substring(2, songTitle.length() -1);

            try {
                JsonObject catalog = ServletUtils.readBody(request);
                catalog.addProperty("Oldsongtitle", songTitle);
                jsonResponse = CatalogServiceImpl.getInstance().updateCatalogJson(catalog);

                if (jsonResponse == null) {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                    jsonResponse = JsonErrorBuilder.getJsonObject(500, "Catalog not updated");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());

                } else {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WORK WITH SUCCESS HTTP REQUESTS + RETRIEVE A COMPLETE CATALOG OBJECT
                    //jsonResponse = JsonErrorBuilder.getJsonObject(201, "Catalog " + jsonResponse.get("songTitle").getAsString() + " has been sucesfully updated");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());
                }

            } catch(Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500, "An error occurred while processing the data provided (PUT CatalogController)");
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
