package org.guitar.DAO;

import org.guitar.DAO.Beans.User;
import org.guitar.DAO.Utils.PasswordHashing;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {

	private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM \"Users\" WHERE \"login\" =?";
    private static final String FIND_BY_LOGIN = "SELECT * FROM \"Users\" WHERE \"login\" = ?";
    private static final String FIND_ALL = "SELECT * FROM \"Users\" ORDER BY \"login\"";
    //private static final String AUTH_USER = "SELECT * FROM \"Users\" WHERE \"login\" = ? AND \"Password\" = ?";
    private static final String INSERT = "INSERT INTO \"Users\" (\"login\",\"password\",\"userName\",\"idRoleUser\",\"salt\") VALUES (?,?,?,?,?)";
    private static final String UPDATE = "UPDATE \"Users\" SET \"login\"=?, \"password\"=?, \"userName\"=?, \"idRoleUser\"=?, \"salt\"=? WHERE \"login\"=?";

    public UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public User AddUser(User user){

        User lastInsertUser = null;

        PasswordHashing passwordHashing = new PasswordHashing();
        User userToAdd = user;

        String saltedPassword = passwordHashing.salting(userToAdd.getPassword());
        String salt = passwordHashing.getSalt(saltedPassword);

        userToAdd.setPassword(passwordHashing.generateHash(saltedPassword));
        userToAdd.setSalt(salt);

        System.out.println("MON SEL EST" + salt);

        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(INSERT);
            preparedStatement.setString(1, userToAdd.getLogin());
            preparedStatement.setString(2, userToAdd.getPassword());
            preparedStatement.setString(3, userToAdd.getUserName());
            preparedStatement.setInt(4, userToAdd.getIdRoleUser());
            preparedStatement.setString(5, userToAdd.getSalt());

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

            String saltedPassword = passwordHashing.salting(userToAdd.getPassword());
            String salt = passwordHashing.getSalt(saltedPassword);

            userToAdd.setPassword(passwordHashing.generateHash(saltedPassword));
            userToAdd.setSalt(salt);

            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, userToAdd.getLogin());
            preparedStatement.setString(2, userToAdd.getPassword());
            preparedStatement.setString(3, userToAdd.getUserName());
            preparedStatement.setInt(4, userToAdd.getIdRoleUser());
            preparedStatement.setString(5, userToAdd.getSalt());
            preparedStatement.setString(6, oldUser);

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
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE);
            preparedStatement.setString(1, login);

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
            Statement statement = connexion.prepareStatement(FIND_BY_LOGIN);
            ((PreparedStatement) statement).setString(1, login);
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