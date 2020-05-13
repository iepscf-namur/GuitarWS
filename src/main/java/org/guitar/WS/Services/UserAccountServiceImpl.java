package org.guitar.WS.Services;

import org.guitar.DAO.Beans.User;
import org.guitar.DAO.DAOFactory;
import org.guitar.DAO.IUserDAO;
import org.guitar.DAO.Utils.GetPropertyValues;
import org.guitar.DAO.Utils.JWTUtils;
import org.guitar.DAO.Utils.PasswordHashing;
import org.guitar.WS.Errors.JsonErrorBuilder;

import com.google.gson.JsonObject;

import java.util.Base64;
import java.io.IOException;

public class UserAccountServiceImpl implements IUserAccountService {

	private static UserAccountServiceImpl _instance = null;
	private static GetPropertyValues propertyValues;
	
	private UserAccountServiceImpl() {
		// Getting Property Values to get schema names
		propertyValues = new GetPropertyValues();
		try {
			propertyValues.getPropValues();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static UserAccountServiceImpl getInstance() {
		if(UserAccountServiceImpl._instance == null) {
			UserAccountServiceImpl._instance = new UserAccountServiceImpl();
		}
		
		return UserAccountServiceImpl._instance;
	}
	
	@Override
	public JsonObject verifyCredentials(JsonObject credentials) {
		JsonObject jsonObj = null;
		
		if(credentials != null && credentials.has("login") && credentials.has("password")) {
			
			DAOFactory daoFactory = DAOFactory.getInstance(propertyValues.getUserSchema());
			IUserDAO userDAO = daoFactory.getUserDAO();
			
			PasswordHashing passwordHashing = new PasswordHashing();
	        String encodedPassword = passwordHashing.passwordEncoded(credentials.get("password").getAsString());
			
	        User user = userDAO.GetUser(credentials.get("login").getAsString());
			if (user != null) {
				if (user.getPassword().equals(encodedPassword)) {
					jsonObj = new JsonObject();
					jsonObj.addProperty("login", user.getLogin());
					jsonObj.addProperty("userName", user.getUserName());
					jsonObj.addProperty("idRolUser", user.getIdRoleUser());
					
					// Standard JWT Claims
					String jwtID = "SOMEID1234"; // Give unique identifier to JWT to avoid replay attacks
						// https://stackoverflow.com/questions/28907831/how-to-use-jto-claim-in-a-jwt
					String jwtIssuer = "GUITAR WS";
					String jwtSubject = user.getUserName();
					String jwtAudience = "GUITAR CLIENT";
					int jwtTimeToLive = 800000;
					
					// Additional Claims
					String GivenName = user.getUserName();
					String Surname = user.getUserName();
					String Email = "jrocket@example.com";
					String Role = Integer.toString(user.getIdRoleUser());
					
					String jwt = JWTUtils.createJWT(
							//Standard JWT Claims
							jwtID, // claim = jti
							jwtIssuer, // claim = iss
							jwtSubject, // claim = sub
							jwtAudience, // claim = aud
							jwtTimeToLive, // used to calculate expiration (claim = exp)
							// additional claims
							GivenName,
							Surname,
							Email,
							Role
					);
					
					jsonObj.addProperty("token", jwt);
				}
			} else {
				return JsonErrorBuilder.getJsonObject(400, "user not found");
			}
		} else {
			return JsonErrorBuilder.getJsonObject(400,
					"required field missing or incorrectly formatted, please check the requirements");
		}
		
		return jsonObj;
	
	}
	
}
