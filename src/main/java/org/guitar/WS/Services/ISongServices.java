package org.guitar.WS.Services;

import java.util.List;

import org.guitar.DAO.Beans.Song;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ISongServices {

    public JsonObject getSongJson(int idCatalogSong);
    public JsonArray getSongsJson();
    public JsonObject updateSongJson(JsonObject song);
    public JsonObject deleteSongJson(int idCatalogSong);
    public JsonObject addSongJson(JsonObject song);
    
}