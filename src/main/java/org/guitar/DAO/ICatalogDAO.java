package org.guitar.DAO;

import org.guitar.DAO.Beans.Catalog;
import org.guitar.DAO.Beans.User;

import java.util.List;

public interface ICatalogDAO {

    Catalog AddCatalog(Catalog catalog);
    int UpdateCatalog(Catalog catalog, int oldSongTitle);
    boolean DeleteCatalog(int idSong);
    Catalog GetCatalog(int songTitle);
    List<Catalog> GetCatalogs();

}