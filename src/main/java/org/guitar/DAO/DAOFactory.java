package org.guitar.DAO;

import org.guitar.DAO.Utils.GetPropertyValues;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {

    private static DAOFactory _instance = null;
    private String url;
    private String username;
    private String password;

    private DAOFactory(String url, String username, String password){
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
            //Loading the driver
            Class.forName(propertyValues.getDriverClassName());
        }
        catch (ClassNotFoundException e){
        }

        if(DAOFactory._instance == null) {
        	System.out.println(propertyValues.getUrl() + propertyValues.getUserSchema() +
					"?" + "user=" + propertyValues.getUsername() + "&password=" + propertyValues.getPassword());
//			DAOFactory._instance =
//					new DAOFactory(propertyValues.getUrl() + propertyValues.getUserSchema() +
//							"?" + "user=" + propertyValues.getUsername() + "&password=" + propertyValues.getPassword(),
//							propertyValues.getUsername(),propertyValues.getPassword());
			DAOFactory._instance =
					new DAOFactory(propertyValues.getUrl() + propertyValues.getUserSchema()
					, propertyValues.getUsername(),propertyValues.getPassword());

        } else {
        	DAOFactory._instance.url = propertyValues.getUrl() + propertyValues.getUserSchema();
        }

        return DAOFactory._instance;
    }

    public Connection getConnection() throws SQLException {
    	try {
    	
    	// ICICICICICICICI
    	System.out.println("url, username, password: " + url + " - " + username + " - " + password);

    	return DriverManager.getConnection("jdbc:mysql://localhost:3306/guitar", "root", "cefalu");
    	
    	//return DriverManager.getConnection(url, username, password);

    	} catch (SQLException e) {
    		return null;
        }
    }

    public IUserDAO getUserDAO() { return new UserDAOImpl(this); }
    public ICatalogDAO getCatalogDAO() { return new CatalogDAOImpl(this); }
}