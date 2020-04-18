package org.guitar.DAO;

import org.guitar.DAO.Beans.Role;

import java.util.List;

public interface IRoleDAO {

	Role GetRole(int id);
	List<Role> GetRoles();

}