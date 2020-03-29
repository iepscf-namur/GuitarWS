package org.guitar.WS.Services;

import java.util.List;

import org.guitar.DAO.Beans.Catalog;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ICatalogServices {

    public JsonObject getCatalogJson(String songTitle);
    public JsonArray getCatalogsJson();
    public JsonObject updateCatalogJson(JsonObject catalog);
    public JsonObject deleteCatalogJson(String songTitle);
    public JsonObject addCatalogJson(JsonObject catalog);
    
}