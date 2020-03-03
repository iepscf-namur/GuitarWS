package org.guitar.DAO.Beans;

public class User {

	private String login;
	private String password;
	private String userName;
	private int idRoleUser;
	private String Salt;

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getUserName() { return userName; }
	public void setUserName(String userName) { this.userName = userName; }

	public int getIdRoleUser() { return idRoleUser; }
	public void setIdRoleUser(int idRoleUser) { this.idRoleUser = idRoleUser; }

	public String getSalt() { return Salt; }
	public void setSalt(String salt) { Salt = salt; }

}
