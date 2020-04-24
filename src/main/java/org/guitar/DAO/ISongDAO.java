package org.guitar.DAO;

import org.guitar.DAO.Beans.Song;

import java.util.List;

public interface ISongDAO {

    Song AddSong(Song song);
    boolean UpdateSong(Song song, int oldIdCatalogSong);
    boolean DeleteSong(int idCatalogSong);
    Song GetSong(int idCatalogSong);
    List<Song> GetSongs();

}