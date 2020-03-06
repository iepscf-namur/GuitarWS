package org.guitar.WS.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

public class ServletUtils {

    public static void setResponseSettings(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    public static JsonObject readBody(HttpServletRequest request) throws Exception {
        //Will transform a Json String in body from client into a Json object
    	JsonObject jsonInput = null;
    	JsonArray jsonArray = null;  /// LP
        
        //Read from request
        StringBuilder buffer = new StringBuilder(640);
        String line;

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if(buffer.length() != 0) {
                //Converting data in buffer to JsonObject
                JsonParser jsonParser = new JsonParser();
                jsonArray = (JsonArray)jsonParser.parse(buffer.toString());
                // jsonInput = (JsonObject)jsonParser.parse(buffer.toString()); /// LP
                jsonInput = convertJsonArrayToObject(jsonArray);
            }
        } catch( Exception e) {
            //It it fails, it will most likely dur to badly formatted body
            //FIXME Fix error to match this reality
            throw e;
        }
        
        return jsonInput;
    }
    
    public static JsonObject convertJsonArrayToObject(JsonArray jsonArray) {
    	// Test if JSonArray is null
    	if (jsonArray.size() == 0) {
    		return null;
    	} else {
    		// Return only the first element of the array
    		return jsonArray.get(0).getAsJsonObject();
    	}
    }
}