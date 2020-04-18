package org.guitar.DAO;

import org.guitar.DAO.Beans.Role;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RoleDAOImpl implements IRoleDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String FIND_BY_ID = "select * from roles where id = ";
    private static final String FIND_ALL = "select * from roles order by name";

    public RoleDAOImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Role GetRole(int id) {

    	Role role = null;

        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.prepareStatement(FIND_BY_ID + id);
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();

            if (resultSet.next()) {
                role = new Role();
                role.setId(Integer.parseInt(resultSet.getString("id")));
                role.setName(resultSet.getString("name"));
                role.setDescription(resultSet.getString("description"));
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    @Override
    public List<Role> GetRoles() {

    	List<Role> roles = new LinkedList<>();

        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while (resultSet.next()) {
                Role role = new Role();
                role.setId(Integer.parseInt(resultSet.getString("id")));
                role.setName(resultSet.getString("name"));
                role.setDescription(resultSet.getString("description"));

                roles.add(role);
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

}
