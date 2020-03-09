package org.guitar.DAO;

import org.guitar.DAO.Beans.Catalog;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CatalogDAOImpl implements ICatalogDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "delete from catalog where idSong = ";
    private static final String FIND_BY_SONGTITLE = "select * from catalog where songTitle = ";
    private static final String FIND_ALL = "select * from catalog order by idSong";
    private static final String INSERT = "insert into catalog (artistName, songTitle) values (";
    private static final String UPDATE = "update catalog set ";

    public CatalogDAOImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Catalog AddCatalog(Catalog catalog) {

        Catalog lastInsertCatalog = null;
        int lastInsertID = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(INSERT +
            		"\"" + catalog.getArtistName() + "\","
            	  + "\"" + catalog.getSongTitle() + "\")");
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();

            if (generatedKey.next()) {
                lastInsertID = generatedKey.getInt(1);
                lastInsertCatalog = catalog;
                lastInsertCatalog.setIdSong(lastInsertID);
            }
            generatedKey.close();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastInsertCatalog;
    }

    @Override
    public int UpdateCatalog(Catalog catalog, int oldSongTitle) {

        int nbRowsAffected = 0;
        
        try {

            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE +
            		"artistName = " + "\"" + catalog.getArtistName() + "\", " +
            		"songTitle = " + "\"" + catalog.getSongTitle() + "\"" +
            		"where songTitle = " + "\"" + oldSongTitle + "\"");

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public boolean DeleteCatalog(int idSong) {

        boolean response = false ;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE + "\"" + idSong + "\"");

            if(preparedStatement.executeUpdate() > 0) response = true;
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Catalog GetCatalog(int songTitle) {
        Catalog catalog = null;

        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.prepareStatement(FIND_BY_SONGTITLE + "\"" + songTitle + "\"");
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();

            if (resultSet.next()) {
                catalog = new Catalog();
                catalog.setIdSong(Integer.parseInt(resultSet.getString("idSong")));
                catalog.setArtistName(resultSet.getString("artistName"));
                catalog.setSongTitle(resultSet.getString("songTitle"));
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return catalog;
    }

    @Override
    public List<Catalog> GetCatalogs() {
        List<Catalog> catalogs = new LinkedList<>();

        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while (resultSet.next()) {
                Catalog catalog = new Catalog();
                catalog.setIdSong(Integer.parseInt(resultSet.getString("idsong")));
                catalog.setArtistName(resultSet.getString("artistName"));
                catalog.setSongTitle(resultSet.getString("songTitle"));

                catalogs.add(catalog);
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return catalogs;
    }

}