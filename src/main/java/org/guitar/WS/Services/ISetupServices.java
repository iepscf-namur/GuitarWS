package org.guitar.WS.Services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ISetupServices {

    public JsonObject getSetupJson(int idCatalogSong);
    public JsonObject updateSetupJson(JsonObject setup);
    public JsonObject deleteSetupJson(int idCatalogSong);

}
