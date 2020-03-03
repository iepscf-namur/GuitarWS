package org.guitar.DAO.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
//import java.util.Date;
import java.util.Properties;

public class GetPropertyValues {

	String result = "";
	String driverClassName = "";
	String url = "";
	String username = "";
	String password = "";
	String devSchema = "";
	String userSchema = "";

	InputStream inputStream;

	public void getPropValues() throws IOException {

		try {
			
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// Not currently use - kept for future version
			// Date time = new Date(System.currentTimeMillis());

			// get the property value
			this.driverClassName = prop.getProperty("db.driverClassName");
			this.url = prop.getProperty("db.url");
			this.username = prop.getProperty("db.username");
			this.password = prop.getProperty("db.password");
			this.devSchema = prop.getProperty("db.devSchema");
			this.userSchema = prop.getProperty("db.userSchema");

			this.result = "driverClassName = " + driverClassName + " , " + url + " , " + username + " , " + password;
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
	}

	public String getResult() {
		return this.result;
	}

	public String getDriverClassName() {
		return this.driverClassName;
	}

	public String getUrl() {
		return this.url;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getDevSchema() {
		return this.devSchema;
	}

	public String getUserSchema() {
		return this.userSchema;
	}
	
}