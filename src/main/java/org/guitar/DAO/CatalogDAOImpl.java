package org.guitar.DAO;

import org.guitar.DAO.Beans.Catalog;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CatalogDAOImpl implements ICatalogDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM \"Catalog\" WHERE \"idSong\" = ?;";
    private static final String FIND_BY_IDSONG = "SELECT * FROM \"Catalog\" WHERE \"idSong\" = ?;";
    private static final String FIND_ALL = "SELECT * FROM \"Catalog\" ORDER BY \"idSong\";";
    private static final String INSERT = "INSERT INTO \"Catalog\" (\"idSong\",\"artistName\",\"songTitle\") VALUES (?,?,?,?)";
    private static final String UPDATE = "UPDATE \"Catalog\" SET \"idSong\"=?, \"artistName\"=?, \"songTitle\"=? WHERE \"idSong\"=?";

    public CatalogDAOImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Catalog AddCatalog(Catalog catalog) {

        Catalog lastInsertCatalog = null;
        int lastInsertID = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, catalog.getSongTitle());

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
    public int UpdateCatalog(Catalog catalog, int oldCatalog) {

        int nbRowsAffected = 0;
        try {

            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, catalog.getSongTitle());
            preparedStatement.setInt(2, oldCatalog);

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public int DeleteCatalog(int catalog) {

        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE);
            preparedStatement.setInt(1, catalog);

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nbRowsAffected;
    }

    @Override
    public Catalog GetCatalog(int idSong) {
        Catalog catalog = null;

        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.prepareStatement(FIND_BY_IDSONG);
            ((PreparedStatement) statement).setInt(1, idSong);
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