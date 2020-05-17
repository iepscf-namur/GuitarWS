package org.guitar.DAO;

import org.guitar.DAO.Beans.Setup;

public interface ISetupDAO {

    Setup AddSetup(Setup setup);
    boolean UpdateSetup(Setup setup);
    boolean DeleteSetup(int idCatalogSong);
    Setup GetSetup(int idCatalogSong);
    
}