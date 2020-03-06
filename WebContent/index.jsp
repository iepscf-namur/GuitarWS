<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WebServices</title>
</head>
<body>
    <h1>GuitarWs Web Services</h1>
    <p> Title: 
	<% String myTitle = (String) request.getAttribute("title"); out.println( myTitle); %>
	</p>
    <h3>GET</h3>
    <p>/users <a href="/users/*">Search all users and return a Json array string</a></p>
    <p>/users <a href="/users/">Search all users and return a Json array string</a></p>
    <p>/users <a href="/users">Search all users and return a Json array string</a></p>
    <p>/users/login <a href="/users/login">Search all users corresponding to login and return a Json array string</a></p>
	<h3>POST</h3>
	<p>/users <a href="/users/">Add a user. Requires JSON body with user login, userName, password, idRoleUser fields</a></p>
	<p>Example: [{"login": "titi@hotmail.be","password": "mytiti123","userName": "titi","idRoleUser": 0}]</p>
	<h3>DELETE</h3>
    <p>/users/login <a href="/users/login">Deletes the requested user</a></p>
	<h3>PUT</h3>
    <p>/users/login <a href="/users/login">Update the role specified, requires a JSON body like POST</a></p>
	<h3>AUTHORIZE</h3>
	<p>/users <a href="/users/auth/">Add a user. Requires JSON body with user login, userName, password, idRoleUser fields</a></p>
	<p>/users <a href="/users/auth">Add a user. Requires JSON body with user login, userName, password, idRoleUser fields</a></p>
	<p>Example: [{"login": "titi@hotmail.be","password": "am9obg=="}]</p>
</body>
</html>