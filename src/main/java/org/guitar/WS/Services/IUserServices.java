package org.guitar.WS.Services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface IUserServices {

    public JsonObject getUserJson(String login);
    public JsonArray getUsersJson();
    public JsonObject authUsersJson(String login, String encodedPassword);
    public JsonObject updateUserJson(JsonObject user);
    public JsonObject deleteUserJson(String login);
    public JsonObject addUserJson(JsonObject user);
    
}