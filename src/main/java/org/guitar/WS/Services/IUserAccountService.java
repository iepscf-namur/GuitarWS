package org.guitar.WS.Services;

import com.google.gson.JsonObject;

public interface IUserAccountService {
	
	public JsonObject verifyCredentials(JsonObject credentials);

}
