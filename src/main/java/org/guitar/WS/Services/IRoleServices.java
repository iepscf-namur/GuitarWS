package org.guitar.WS.Services;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface IRoleServices {

    public JsonObject getRoleJson(int id);
    public JsonArray getRolesJson();
    
}
