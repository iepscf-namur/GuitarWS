package org.guitar.DAO;

import org.guitar.DAO.Beans.Song;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SongDAOImpl implements ISongDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "delete from songs where idCatalogSong = ";
    private static final String FIND_BY_IDCATALOGSONG = "select * from songs where idCatalogSong = ";
    private static final String FIND_ALL = "select * from songs order by idCatalogSong";
    private static final String INSERT = "insert into songs (idCatalogSong, song) values (";
    private static final String UPDATE = "update songs set ";

    public SongDAOImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Song AddSong(Song song) {

        Song lastInsertSong = null;
        int lastInsertID = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(INSERT +
            	    song.getIdCatalogSong() + ","
            	  + "\"" + song.getSong() + "\")", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();

            if (generatedKey.next()) {
                lastInsertID = generatedKey.getInt(1);
                lastInsertSong = song;
                lastInsertSong.setId(lastInsertID);
            }
            generatedKey.close();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastInsertSong;
    }

    @Override
    public boolean UpdateSong(Song song, int oldIdCatalogSong) {

        boolean response = false ;
        
        try {

            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE +
            		"idCatalogSong = " + song.getIdCatalogSong() + ", " +
            		"song = " + "\"" + song.getSong() + "\" " +
            		"where idCatalogSong = " + oldIdCatalogSong);

            if(preparedStatement.executeUpdate() > 0) response = true ;
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public boolean DeleteSong(int idCatalogSong) {

        boolean response = false ;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE + 
            		idCatalogSong);

            if(preparedStatement.executeUpdate() > 0) response = true;
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Song GetSong(int idCatalogSong) {

    	Song song = null;

        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.prepareStatement(FIND_BY_IDCATALOGSONG + idCatalogSong);
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();

            if (resultSet.next()) {
                song = new Song();
                song.setId(Integer.parseInt(resultSet.getString("id")));
                song.setIdCatalogSong(Integer.parseInt(resultSet.getString("idCatalogSong")));
                song.setSong(resultSet.getString("song"));
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return song;
    }

    @Override
    public List<Song> GetSongs() {

    	List<Song> songs = new LinkedList<>();

        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while (resultSet.next()) {
                Song song = new Song();
                song.setId(Integer.parseInt(resultSet.getString("id")));
                song.setIdCatalogSong(Integer.parseInt(resultSet.getString("idCatalogSong")));
                song.setSong(resultSet.getString("song"));

                songs.add(song);
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return songs;
    }

}
