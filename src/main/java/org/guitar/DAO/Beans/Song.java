package org.guitar.DAO.Beans;

public class Song {

	private int id;
	private int idCatalogSong;
	private String song;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public int getIdCatalogSong() { return idCatalogSong; }
	public void setIdCatalogSong(int idCatalogSong) { this.idCatalogSong = idCatalogSong; }
	
	public String getSong() { return song; }
	public void setSong(String song) { this.song = song; }

}