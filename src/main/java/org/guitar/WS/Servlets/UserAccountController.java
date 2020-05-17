package org.guitar.WS.Servlets;

import org.guitar.WS.Errors.JsonErrorBuilder;
import org.guitar.WS.Services.UserAccountServiceImpl;
import org.guitar.WS.Utils.ServletUtils;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAccountController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// We need to set Response Header's Content-type and CharacterEncoding before sending it to the client
		ServletUtils.setResponseSettings(response);
		
        JsonObject jsonResponse = null;

        // If any parameters are given (request URL is /login or /login/)
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            // Read the body content and send it to UserService for user creation
            try {
                //Will transform the Json String from the body content into a Json object
                JsonObject user = ServletUtils.readBody(request);
                jsonResponse = UserAccountServiceImpl.getInstance().verifyCredentials(user);

                if(jsonResponse == null) {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WRITE BEHIND TO AVOID CODE REDUNDENCE
                    jsonResponse = JsonErrorBuilder.getJsonObject(400, "invalid username or password");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    //response.getWriter().write(jsonResponse.toString());
                } else {
                    //FIXME CREATING AN ERROR BUILDER THAT COULD WORK WITH SUCCESS HTTP REQUESTS
                    response.setStatus(200);
                    //response.getWriter().write(jsonResponse.toString());
                }

            } catch (Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500, "An error occurred while reading the data provided");
                response.setStatus(jsonResponse.get("code").getAsInt());
                //response.getWriter().write(jsonResponse.toString());
                e.printStackTrace();
            }
        }else {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    404,
                    request.getServletPath() + request.getPathInfo() + " is not a supported GET url");
            response.setStatus(jsonResponse.get("code").getAsInt());
            //response.getWriter().write(jsonResponse.toString());
        }

        response.getWriter().write(jsonResponse.toString());
    }
}
