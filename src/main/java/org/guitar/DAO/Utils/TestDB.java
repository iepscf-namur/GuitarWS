package org.guitar.DAO.Utils;

import java.sql.* ;
import java.io.* ;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public  class TestDB {

	public  static  void main (String[] args) {
		try {
    	 
			System.out.println("Debut du main.");
			
			// chargement de la classe par son nom
			Class c = Class.forName("com.mysql.cj.jdbc.Driver");
			Driver pilote = (Driver)c.newInstance() ;
			
			// enregistrement du pilote auprès du DriverManager
			DriverManager.registerDriver(pilote);
			
			// Protocole de connexion
			String protocole =  "jdbc:mysql:" ;
			
			// Adresse IP de l’hôte de la base et port
			String ip =  "localhost" ;  // dépend du contexte
			String port =  "3306" ;  // port MySQL par défaut
			
			// Nom de la base ;
			String nomBase =  "guitar" ;  // dépend du contexte
			
			// Chaîne de connexion
			String conString = protocole +  "//" + ip +  ":" + port +  "/" + nomBase ;
			
			// Identifiants de connexion et mot de passe
			String nomConnexion =  "root" ;  // dépend du contexte
			String motDePasse =  "cefalu" ;  // dépend du contexte
			
			System.out.println(conString);
			// Connexion
			Connection con = DriverManager.getConnection(conString, nomConnexion, motDePasse) ;

			// Envoi d’un requête générique
			String sql =  "select * from users" ;
			Statement smt = con.createStatement() ;
			ResultSet rs = smt.executeQuery(sql) ;
			while (rs.next()) {
				System.out.println(rs.getString("login")) ;
			}
		}  catch (Exception e) {
			System.out.println("Erreur: " + e);
			// gestion des exceptions
		}
	}
}