package org.guitar.DAO;

import org.guitar.DAO.Beans.Setup;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SetupDAOImpl implements ISetupDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "delete from setup where idCatalogSong = ";
    private static final String FIND_BY_IDCATALOGSONG = "select * from setup where idCatalogSong = ";
    private static final String INSERT = "insert into setup (idCatalogSong, duration, fontSize) values (";
    private static final String UPDATE = "update setup set ";

    public SetupDAOImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Setup AddSetup(Setup setup) {

        Setup lastInsertSetup = null;
        int lastInsertID = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(INSERT +
            	    setup.getIdCatalogSong() + ", " +
            	    setup.getDuration() + ", " +
            	    setup.getFontSize() + ")", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();

            if (generatedKey.next()) {
                lastInsertID = generatedKey.getInt(1);
                lastInsertSetup = setup;
                lastInsertSetup.setIdCatalogSong(lastInsertID);
            }
            generatedKey.close();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastInsertSetup;
    }

    @Override
    public boolean UpdateSetup(Setup setup) {

        boolean response = false ;
        
        try {

            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE +
            		"duration = " + setup.getDuration() + ", " +
            		"fontSize = " + setup.getFontSize() + " " +
            		"where idCatalogSong = " + setup.getIdCatalogSong());

            if(preparedStatement.executeUpdate() > 0) response = true ;
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public boolean DeleteSetup(int idCatalogSong) {

        boolean response = false ;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE + idCatalogSong);

            if(preparedStatement.executeUpdate() > 0) response = true;
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Setup GetSetup(int idCatalogSong) {

    	Setup setup = null;

        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.prepareStatement(FIND_BY_IDCATALOGSONG + idCatalogSong);
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();

            if (resultSet.next()) {
                setup = new Setup();
                setup.setIdCatalogSong(resultSet.getInt("idCatalogSong"));
                setup.setDuration(resultSet.getInt("duration"));
                setup.setFontSize(resultSet.getInt("fontSize"));
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return setup;
    }

}
