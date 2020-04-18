package org.guitar.WS.Services;

import org.guitar.DAO.Beans.Role;
import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.IRoleDAO;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.WS.Errors.JsonErrorBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class RoleServiceImpl implements IRoleServices {

    private static RoleServiceImpl _instance = null;
    private static GetPropertyValues propertyValues;

    private RoleServiceImpl() {
        //Getting Properties Values needed to get schema names
        propertyValues = new GetPropertyValues();
        try {
            propertyValues.getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RoleServiceImpl getInstance() {
        if (RoleServiceImpl._instance == null) {
            RoleServiceImpl._instance = new RoleServiceImpl();
        }

        return RoleServiceImpl._instance;
    }

    @Override
    public JsonArray getRolesJson() {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        IRoleDAO roleDAO = daoFactory.getRoleDAO();
        List<Role> roles = roleDAO.GetRoles();

        JsonArray rolesJsonArray = new JsonArray();

        for (Role role : roles) {
        	JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("id", role.getId());
            jsonObj.addProperty("name", role.getName());
            jsonObj.addProperty("description", role.getDescription());

            rolesJsonArray.add(jsonObj);
        }
        return rolesJsonArray;
    }
 
    @Override
    public JsonObject getRoleJson(int id) {
    	DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        IRoleDAO roleDAO = daoFactory.getRoleDAO();

        Role role = roleDAO.GetRole(id);
        JsonObject jsonObj = null;

        if (role != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", role.getId());
            jsonObj.addProperty("name", role.getName());
            jsonObj.addProperty("description", role.getDescription());
        }
        return jsonObj;
    }

}

