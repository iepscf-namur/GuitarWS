package org.guitar.WS.Services;

import org.guitar.DAO.Beans.User;
import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.IUserDAO;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.WS.Errors.JsonErrorBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class UserServiceImpl implements IUserServices {

    private static UserServiceImpl _instance = null;
    private static GetPropertyValues propertyValues;

    private UserServiceImpl() {
        //Getting Properties Values needed to get schema names
        propertyValues = new GetPropertyValues();
        try {
            propertyValues.getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserServiceImpl getInstance() {
        if (UserServiceImpl._instance == null) {
            UserServiceImpl._instance = new UserServiceImpl();
        }

        return UserServiceImpl._instance;
    }

    @Override
    public JsonArray getUsersJson() {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        IUserDAO userDAO = daoFactory.getUserDAO();
        List<User> users = userDAO.GetUsers();

        JsonArray usersJsonArray = new JsonArray();

        for (User user : users) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("login", user.getLogin());
            jsonObj.addProperty("password", user.getPassword());
            jsonObj.addProperty("userName", user.getUserName());
            jsonObj.addProperty("idRoleUser", user.getIdRoleUser());

            usersJsonArray.add(jsonObj);
        }
        return usersJsonArray;
    }

    @Override
    public JsonObject getUserJson(String login) {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        IUserDAO userDAO = daoFactory.getUserDAO();

        User user = userDAO.GetUser(login);
        JsonObject jsonObj = null;

        if (user != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("login", user.getLogin());
            jsonObj.addProperty("password", user.getPassword());
            jsonObj.addProperty("userName", user.getUserName());
            jsonObj.addProperty("idRoleUser", user.getIdRoleUser());
        }
        return jsonObj;
    }

    @Override
    public JsonObject authUsersJson(String login, String encodedPassword) {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        IUserDAO userDAO = daoFactory.getUserDAO();

        User user = userDAO.GetUser(login);
        JsonObject jsonObj = null;

        if (user != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("login", user.getLogin());
            jsonObj.addProperty("userName", user.getUserName());
            jsonObj.addProperty("idRoleUser", user.getIdRoleUser());
            
            if (user.getPassword().toString().equals(encodedPassword)) {
            	jsonObj.addProperty("authorized", "true");
            } else {
            	jsonObj.addProperty("authorized", "false");
            }
        }
        return jsonObj;
    	
    }

    @Override
    public JsonObject addUserJson(JsonObject userJsonObj) {

        if (!isUserValid(userJsonObj)) {
            //FIXME This error will never been shown up as it will be encapsulated in a Servlet TRY
            return JsonErrorBuilder.getJsonObject(400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        IUserDAO userDAO = daoFactory.getUserDAO();

        JsonObject jsonObj = null;

        // Do not allow user creation if login already exists
        //FIXME DB Will handle it, should I handle in the Service too ?

        User user = new User();
        user.setLogin(userJsonObj.get("login").getAsString());
        user.setPassword(userJsonObj.get("password").getAsString());
        user.setUserName(userJsonObj.get("userName").getAsString());
        // If the role is not set in the json object, we assign the default role to the user
        if (!userJsonObj.has("idRoleUser")) {
            user.setIdRoleUser(0);
            //FIXME WE SHOULD GET THE DEFAULT ROLE IN DB

            //FIXME SEE ORIGINAL FILE TO WORK WITH LIAISON TABLES
        } else {
            //FIXME SEE ORIGINAL FILE TO WORK WITH MULTIPLE ROLES
            user.setIdRoleUser(userJsonObj.get("idRoleUser").getAsInt());
            //FIXME DB Will handle it, should I handle in the Service too ?
            // We check if role provided in the json object exists in the db
            //FIXME SEE ORIGINAL FILE TO WORK WITH MULTIPLE ROLES
            // We check that the same role is not assigned to the user more than once
        }

        // We insert the user and check if it has been correctly inserted into the db
        User lastInsertUser = userDAO.AddUser(user);

        // FIXME rename or extends JsonBuilder so we do not use "error" here
        //jsonResponse = JsonErrorBuilder.getJsonObject(201, "User " + lastInsertLogin + " has been sucesfully created");

        if(lastInsertUser != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("login", lastInsertUser.getLogin());
            jsonObj.addProperty("password", lastInsertUser.getPassword());
            jsonObj.addProperty("userName", lastInsertUser.getUserName());
            jsonObj.addProperty("idRoleUser", lastInsertUser.getIdRoleUser());
        }
        return jsonObj;
    }

    @Override
    public JsonObject deleteUserJson(String login) {
        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        IUserDAO userDAO = daoFactory.getUserDAO();

        //Checking if user actually exists
        User user = userDAO.GetUser(login);
        if(user != null) {

            if(userDAO.DeleteUser(user.getLogin())) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "User deleted");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "User not found");
        }
        return jsonResponse;
    }

    @Override
    public JsonObject updateUserJson(JsonObject jsonObject) {

        if(!isUserValid(jsonObject) && jsonObject.has("Oldlogin")) {
            //FIXME This error will never show up as it will be encapsulated in a Servlet TRY
            return JsonErrorBuilder.getJsonObject(400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        IUserDAO userDAO = daoFactory.getUserDAO();

        JsonObject jsonResponse = null;

        String olduser = jsonObject.get("Oldlogin").getAsString();
        User user = userDAO.GetUser(jsonObject.get("Oldlogin").getAsString());

        if(user != null) {
            // TODO Send message if field provided does not exist ?
            if(jsonObject.has("login")) {
                user.setLogin(jsonObject.get("login").getAsString());
            }
            if(jsonObject.has("password")) {
                user.setPassword(jsonObject.get("password").getAsString());
            }
            if(jsonObject.has("userName")) {
                user.setUserName(jsonObject.get("userName").getAsString());
            }
            if(jsonObject.has("idRoleUser")) {
                user.setIdRoleUser(jsonObject.get("idRoleUser").getAsInt());
            }

            if(userDAO.UpdateUser(user,olduser)) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "User " + user.getLogin() + " updated");
            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(400, "User found but not updated");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "User not found");
        }

        return jsonResponse;
    }

    private boolean isUserValid(JsonObject jsonObj) {
        return  jsonObj != null &&
                jsonObj.has("login") &&
                jsonObj.has("password") &&
                jsonObj.has("userName");
    }
}