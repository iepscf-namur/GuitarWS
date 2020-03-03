package org.guitar.WS.Errors;

import com.google.gson.JsonObject;

public class JsonErrorBuilder {

    public static JsonObject getJsonObject(int code, String message) {
        //Will create and return a JsonObject with a property "code" and a property "message"
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("code", code);
        jsonObj.addProperty("message", message);

        return jsonObj;
    }

    public static String getJsonString(int code, String message) {
        //Will return a Stringified version of a JsonObject
        JsonObject jsonObj = JsonErrorBuilder.getJsonObject(code, message);
        return jsonObj.toString();
    }
    
}