package org.guitar.DAO;

import org.guitar.DAO.Beans.Catalog;

import java.util.List;

public interface ICatalogDAO {

    Catalog AddCatalog(Catalog catalog);
    boolean UpdateCatalog(Catalog catalog, String oldSongTitle);
    boolean DeleteCatalog(int idSong);
    Catalog GetCatalog(String songTitle);
    List<Catalog> GetCatalogs();

}