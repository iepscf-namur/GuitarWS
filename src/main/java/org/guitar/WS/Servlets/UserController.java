package org.guitar.WS.Servlets;

import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.WS.Errors.JsonErrorBuilder;
import org.guitar.WS.Utils.ServletUtils;
import org.guitar.WS.Services.UserServiceImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserController extends HttpServlet {
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // We need to set Response Header's Content-Type and CharacterEncoding before sending it to the client
        ServletUtils.setResponseSettings(response);

        JsonObject jsonResponse = null;

        // If any parameters are given (request URL is /users or /users/)
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            // Read the body content and send it to UserService for user creation
            try {
                // Transform the Json String from the body content into a Json object
                JsonObject user = ServletUtils.readBody(request);
                jsonResponse = UserServiceImpl.getInstance().addUserJson(user);

                if (jsonResponse == null) {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                    jsonResponse = JsonErrorBuilder.getJsonObject(500, "User not created");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());

                } else {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WORK WITH SUCCESS HTTP REQUESTS
                    jsonResponse = JsonErrorBuilder.getJsonObject(201, "User " + jsonResponse.get("login").getAsString() + " has been sucesfully created");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());
                }

            } catch(Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(500,
                        "An error occurred while processing the data provided (POST UserController)");
                response.setStatus(jsonResponse.get("code").getAsInt());
                e.printStackTrace();
            }

            // If URL is different from /users
            } else if (request.getPathInfo().equals("/auth") || request.getPathInfo().equals("/auth/")) {
            	try {
                    // Transform the Json String from the body content into a Json object
            		JsonObject user = ServletUtils.readBody(request);
                    String login = (String) user.get("login").toString();
                    login = login.substring(1, login.length()-1);
                    String encodedPassword = (String) user.get("password").toString();
                    encodedPassword = encodedPassword.substring(1, encodedPassword.length()-1);
                    
            	    jsonResponse = UserServiceImpl.getInstance().authUsersJson((String)login, (String)encodedPassword);
                   
            	    if (jsonResponse == null) {
                        //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                        jsonResponse = JsonErrorBuilder.getJsonObject(500, "User not validated");
                        response.setStatus(jsonResponse.get("code").getAsInt());
                        //response.getWriter().write(jsonResponse.toString());

                    } else {
                        response.setStatus(201);
                    }

            	}
            	catch(Exception e) {
            		jsonResponse = JsonErrorBuilder.getJsonObject(500,
                        "An error occurred while processing the data provided (POST UserController)");
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

        // If any parameters are given (request URL is /users or /users/)
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            JsonArray jsonResponse = UserServiceImpl.getInstance().getUsersJson();
            response.setStatus(200);
            response.getWriter().write(jsonResponse.toString());
            return;

            // If some parameters are given (request URL is /users{login})
        } else if(request.getPathInfo().substring(1).length()> 0) {
            JsonObject jsonResponse =  null;
            // Remove "/" at the beginning of the string 
            String login = "";
            login = request.getPathInfo().substring(1);
            
            jsonResponse = UserServiceImpl.getInstance().getUserJson(login);

            if(jsonResponse == null) {
                //FIXME WILL NEVER HAPPENS CAUSE OF ERROR HANDLING IN SERVICE
                jsonResponse = JsonErrorBuilder.getJsonObject(404, "user not found");
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

        // If some parameters are given (request URL is /users{login})
        if (request.getPathInfo().substring(1).length()> 0) {
            // Remove "/" at the beginning of the string
            String login = request.getPathInfo().substring(1);

            jsonResponse = UserServiceImpl.getInstance().deleteUserJson(login);

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

        // If some parameters are given (request URL is /users{login})
        if(request.getPathInfo().substring(1).length()> 0) {
            // Remove "/" at the beginning of the string
            String login = request.getPathInfo().substring(1);

            try {
                JsonObject user = ServletUtils.readBody(request);
                user.addProperty("Oldlogin", login);
                jsonResponse = UserServiceImpl.getInstance().updateUserJson(user);

                if (jsonResponse == null) {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                    jsonResponse = JsonErrorBuilder.getJsonObject(500, "User not updated");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());

                } else {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WORK WITH SUCCESS HTTP REQUESTS + RETRIEVE A COMPLETE USER OBJECT
                    //jsonResponse = JsonErrorBuilder.getJsonObject(201, "User " + jsonResponse.get("login").getAsString() + " has been sucesfully updated");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());
                }

            } catch(Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500, "An error occurred while processing the data provided (PUT UserController)");
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