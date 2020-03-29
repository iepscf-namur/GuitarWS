package org.guitar.DAO;

import org.guitar.DAO.Beans.Catalog;
import org.guitar.DAO.Beans.User;

import java.util.List;

public interface ICatalogDAO {

    Catalog AddCatalog(Catalog catalog);
    boolean UpdateCatalog(Catalog catalog, String oldSongTitle);
    boolean DeleteCatalog(int idSong);
    Catalog GetCatalog(String songTitle);
    List<Catalog> GetCatalogs();

}