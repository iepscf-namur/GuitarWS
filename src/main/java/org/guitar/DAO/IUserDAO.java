package org.guitar.DAO;

import org.guitar.DAO.Beans.User;
import java.util.List;

public interface IUserDAO {

    User AddUser(User user);
    boolean UpdateUser(User user,String oldUser);
    boolean DeleteUser(String login);
    User GetUser(String login);
    List<User> GetUsers();

}
