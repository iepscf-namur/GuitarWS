package org.guitar.DAO;

import org.guitar.DAO.Beans.Catalog;
import java.util.List;

public interface ICatalogDAO {

    Catalog AddCatalog(Catalog catalog);
    int UpdateCatalog(Catalog catalog, int oldCatalog);
    int DeleteCatalog(int catalog);
    Catalog GetCatalog(int idSong);
    List<Catalog> GetCatalogs();

}
