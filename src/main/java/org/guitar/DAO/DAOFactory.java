package org.guitar.DAO;

import org.guitar.DAO.Utils.GetPropertyValues;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//If any instances of DAOFactory already exist, we will create and return one
//This class can open a connection to the Database

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
            //https://jdbc.postgresql.org/documentation/81/load.html
            //Before connecting to the DB, we need to load the driver
            //Loading the driver
            //returns org.postgresql.Driver, so basically a new instance of that class
            Class.forName(propertyValues.getDriverClassName());
        }
        catch (ClassNotFoundException e){
        }

        if(DAOFactory._instance == null) {
            DAOFactory._instance = new DAOFactory("jdbc:postgresql://"+ propertyValues.getUrl() + schema, propertyValues.getUsername(), propertyValues.getPassword());
        } else {
            DAOFactory._instance.url = "jdbc:postgresql://"+ propertyValues.getUrl() + schema;
        }

        return DAOFactory._instance;
    }

    public Connection getConnection() throws SQLException {
        //System.out.println(url);
        return DriverManager.getConnection(url,username,password);
    }

    public IUserDAO getUserDAO() { return new UserDAOImpl(this); }
    public ICatalogDAO getCatalogDAO() { return new CatalogDAOImpl(this); }
}