package com.leilao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DirectConnection {
	
	private static Connection connection = null;
	
	public DirectConnection(){
		try{	
			Class.forName("org.postgresql.Driver");
			setConnection(DriverManager.getConnection("jdbc:postgresql://localhost:5432/leilao" , "postgres" , "bdappleilao"));
			
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
	}

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		DirectConnection.connection = connection;
	}

}
