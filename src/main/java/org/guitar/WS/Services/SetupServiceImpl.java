package org.guitar.WS.Services;

import org.guitar.DAO.Beans.Setup;
import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.ISetupDAO;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.WS.Errors.JsonErrorBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class SetupServiceImpl implements ISetupServices {

	private static SetupServiceImpl _instance = null;
    private static GetPropertyValues propertyValues;

    private SetupServiceImpl() {
        //Getting Properties Values needed to get schema names
        propertyValues = new GetPropertyValues();
        try {
            propertyValues.getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SetupServiceImpl getInstance() {
        if (SetupServiceImpl._instance == null) {
            SetupServiceImpl._instance = new SetupServiceImpl();
        }

        return SetupServiceImpl._instance;
    }

    @Override
    public JsonObject getSetupJson(int idCatalogSong) {
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ISetupDAO setupDAO = daoFactory.getSetupDAO();

        Setup setup = setupDAO.GetSetup(idCatalogSong);
        JsonObject jsonObj = null;

        if (setup != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("idCatalogSong", setup.getIdCatalogSong());
            jsonObj.addProperty("duration", setup.getDuration());
            jsonObj.addProperty("fontSize", setup.getFontSize());
        }
        return jsonObj;
    }

    @Override
    public JsonObject deleteSetupJson(int idCatalogSong) {
        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ISetupDAO setupDAO = daoFactory.getSetupDAO();

        //Checking if setup actually exists
        Setup setup = setupDAO.GetSetup(idCatalogSong);
        if(setup != null) {

            if(setupDAO.DeleteSetup(setup.getIdCatalogSong())) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "Setup deleted");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Setup not found");
        }
        return jsonResponse;
    }

    @Override
    public JsonObject updateSetupJson(JsonObject jsonObject) {

        if(!isSetupValid(jsonObject)) {
            //FIXME This error will never show up as it will be encapsulated in a Servlet TRY
            return JsonErrorBuilder.getJsonObject(400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
        ISetupDAO setupDAO = daoFactory.getSetupDAO();

        JsonObject jsonResponse = null;

        Setup setup = setupDAO.GetSetup(jsonObject.get("idCatalogSong").getAsInt());

        if(setup == null) {
        	
            // Fulfill setup
        	Setup tmpSetup = new Setup();
        	tmpSetup.setIdCatalogSong(jsonObject.get("idCatalogSong").getAsInt());
            tmpSetup.setDuration(jsonObject.get("duration").getAsInt());
            tmpSetup.setFontSize(jsonObject.get("fontSize").getAsInt());

            setupDAO.AddSetup(tmpSetup);
            jsonResponse = JsonErrorBuilder.getJsonObject(200, "Setup " + 
            		tmpSetup.getIdCatalogSong() + " updated");
        		
        } else {
        	
            // TODO Send message if field provided does not exist ?
            if(jsonObject.has("idCatalogSong")) {
                setup.setIdCatalogSong(jsonObject.get("idCatalogSong").getAsInt());
            }
            if(jsonObject.has("duration")) {
                setup.setDuration(jsonObject.get("duration").getAsInt());
            }
            if(jsonObject.has("fontSize")) {
                setup.setFontSize(jsonObject.get("fontSize").getAsInt());
            }

            if(setupDAO.UpdateSetup(setup)) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "Setup " + setup.getIdCatalogSong() + " updated");
            	jsonResponse.addProperty("idCatalogSong",setup.getIdCatalogSong());

            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(400, "Setup found but not updated");
            }
        }

        return jsonResponse;
    }

    private boolean isSetupValid(JsonObject jsonObj) {
        return  jsonObj != null &&
        		jsonObj.has("idCatalogSong") &&
                jsonObj.has("duration") &&
                jsonObj.has("fontSize");
    }
}