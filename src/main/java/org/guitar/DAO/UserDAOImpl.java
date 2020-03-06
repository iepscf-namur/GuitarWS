package org.guitar.DAO;

import org.guitar.DAO.Beans.User;
import org.guitar.DAO.Utils.PasswordHashing;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {

	private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "delete from users where login = ";
    private static final String FIND_BY_LOGIN = "select * from users where login like ";
    private static final String FIND_ALL = "select * from users order by login";
    private static final String INSERT = "insert into users (login, username, idRoleUser, password, Salt) values (";
    private static final String UPDATE = "update users set ";
    //private static final String AUTH_USER = "SELECT * FROM \"Users\" WHERE \"login\" = ? AND \"Password\" = ?";

    public UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public User AddUser(User user){

        User lastInsertUser = null;

        PasswordHashing passwordHashing = new PasswordHashing();
        User userToAdd = user;

        String encodedPassword = passwordHashing.passwordEncoded(userToAdd.getPassword());
        userToAdd.setPassword(encodedPassword);
        userToAdd.setSalt(encodedPassword);

        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(INSERT + 
            	  "\"" + userToAdd.getLogin() + "\","
            	+ "\"" + userToAdd.getUserName() + "\","
            	+ userToAdd.getIdRoleUser() + ","            	
            	+ "\"" + userToAdd.getPassword() + "\","
            	+ "\"" + userToAdd.getSalt() + "\")");
            
            if(preparedStatement.executeUpdate() > 0){
                lastInsertUser = userToAdd;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastInsertUser;
    }

    @Override
    public boolean UpdateUser(User user, String oldUser) {
        boolean response = false ;
        try {

            PasswordHashing passwordHashing = new PasswordHashing();
            User userToAdd = user;

            String encodedPassword = passwordHashing.passwordEncoded(userToAdd.getPassword());
            userToAdd.setPassword(encodedPassword);
            userToAdd.setSalt(encodedPassword);

            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE +
            		"login = " + "\"" + userToAdd.getLogin() + "\", " +
            		"password = " + "\"" + userToAdd.getPassword() + "\", " +
            		"userName = " + "\"" + userToAdd.getUserName() + "\", " +
            		"idRoleUser = " + userToAdd.getIdRoleUser() + ", " +
            		"Salt = " + "\"" + userToAdd.getSalt() + "\" " +
            		"where login = " + "\"" + oldUser + "\""); 

            if(preparedStatement.executeUpdate() > 0) response = true ;
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response ;
    }

    @Override
    public boolean DeleteUser(String login) {
        //FIXME MAYBE SENDING BACK A FULL REPRESENTATION OF THE DELETED USER ?
        boolean response = false ;
        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE + "\"" + login + "\"");
            if(preparedStatement.executeUpdate() > 0) response = true ;
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<User> GetUsers() {
        List<User> users = new LinkedList<User>();
        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while(resultSet.next()){
                User user = new User();
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setUserName(resultSet.getString("username"));
                user.setIdRoleUser(Integer.parseInt(resultSet.getString("idroleuser")));
                user.setSalt(resultSet.getString("salt"));

                users.add(user);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User GetUser(String login){
        User user = null;

        try{
            connexion = daoFactory.getConnection();
            if (login.equals("*")) { 
            	login = "%";
            }
//            else { 
//            	login = ("%" + login + "%");
//            }
            	
            Statement statement = connexion.prepareStatement(FIND_BY_LOGIN + "\"" + login + "\"");
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();

            if(resultSet.next()){
                user = new User();
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setUserName(resultSet.getString("username"));
                user.setIdRoleUser(Integer.parseInt(resultSet.getString("idroleuser")));
                user.setSalt(resultSet.getString("salt"));
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }

        return user ;
    }

    @Override
    public User AuthUser(User user){
        return null;
    }

}