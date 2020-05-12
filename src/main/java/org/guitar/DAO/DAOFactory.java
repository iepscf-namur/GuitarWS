package org.guitar.DAO;

import org.guitar.DAO.Utils.GetPropertyValues;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {

    private static DAOFactory _instance = null;
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private DAOFactory(String driverClassName, String url, String username, String password) {
    	this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DAOFactory getInstance(String schema){

        //Getting Properties Values needed to load the driver and connect to the DB
        GetPropertyValues propertyValues = new GetPropertyValues();
        try {
            propertyValues.getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            // Loading the driver
            Class.forName(propertyValues.getDriverClassName());
        }
        catch (ClassNotFoundException e){
        }

        if(DAOFactory._instance == null) {
			DAOFactory._instance =
					new DAOFactory(propertyValues.getDriverClassName(), propertyValues.getUrl() + propertyValues.getUserSchema()
					, propertyValues.getUsername(),propertyValues.getPassword());

        } else {
        	DAOFactory._instance.url = propertyValues.getUrl() + propertyValues.getUserSchema();
        }

        return DAOFactory._instance;
    }

    public Connection getConnection() throws SQLException {
    	try {
    		// chargement de la classe par son nom
    		Class c = Class.forName(driverClassName);

    		return DriverManager.getConnection(url, username, password);
    	
    	} catch (SQLException | ClassNotFoundException e) {
    		return null;
        }
    }

    public IUserDAO getUserDAO() { return new UserDAOImpl(this); }
    public ICatalogDAO getCatalogDAO() { return new CatalogDAOImpl(this); }
    public IRoleDAO getRoleDAO() { return new RoleDAOImpl(this); }
    public ISongDAO getSongDAO() { return new SongDAOImpl(this); }
    public ISetupDAO getSetupDAO() { return new SetupDAOImpl(this); }
    
}