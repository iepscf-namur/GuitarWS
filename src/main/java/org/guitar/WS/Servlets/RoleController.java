package org.guitar.WS.Servlets;

import org.guitar.WS.Errors.JsonErrorBuilder;
import org.guitar.WS.Utils.ServletUtils;
import org.guitar.WS.Services.RoleServiceImpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// We need to set Response Header's Content-Type and CharacterEncoding before sending it to the client
	    ServletUtils.setResponseSettings(response);
	
	    // If any parameters are given (request URL is /roles or /roles/)
	    if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
	        JsonArray jsonResponse = RoleServiceImpl.getInstance().getRolesJson();
	        response.setStatus(200);
	        response.getWriter().write(jsonResponse.toString());
	        return;
	
	        // If some parameters are given (request URL is /users{login})
	    } else if(request.getPathInfo().substring(1).length()> 0) {
	        JsonObject jsonResponse =  null;
	        // Remove "/" at the beginning of the string 
	        String id = "";
	        id = request.getPathInfo();
	        id = id.substring(2, id.length() -1);
	        int i = Integer.parseInt(id);
	        
	        jsonResponse = RoleServiceImpl.getInstance().getRoleJson(i);
	
	        if(jsonResponse == null) {
	            //FIXME WILL NEVER HAPPENS CAUSE OF ERROR HANDLING IN SERVICE
	            jsonResponse = JsonErrorBuilder.getJsonObject(404, "role not found");
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
