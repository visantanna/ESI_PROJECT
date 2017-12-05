package com.leilao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

public class InteresseDAO {
	public static void criaAtualizaInteresse(String mensagem , Gson gson ,PrintWriter  out) {
		Mensagem msgRetorno = new Mensagem();
		String[] argumentos =  mensagem.split("/");
		boolean interesse = Boolean.parseBoolean(argumentos[0]);
		int userId = Integer.parseInt(argumentos[1]);
		int itemId = Integer.parseInt(argumentos[2]);
		
		try {
			int interesseId = buscarInteresse(userId , itemId);
			if(interesseId == -1) {
				criaInteresse(interesse , userId , itemId);
			}else {
				atualizaInteresse(interesse , userId , itemId);
			}
			msgRetorno.setCabecalho("sucesso");
		    msgRetorno.setMensagem(""+interesse);
		     
		   
		}catch(Exception e) {
			msgRetorno.setCabecalho("sucesso");
			msgRetorno.setMensagem(""+!interesse);
		}finally {
			String json = gson.toJson(msgRetorno);
		    out.println(json);
		    out.write(json);
		    out.flush();
		}
	}

	private static void atualizaInteresse(boolean interesse, int userId, int itemId) throws SQLException {
		String query = "UPDATE INTERESSE SET INTERESSE = "+interesse + " WHERE USERID = "+userId + "ITEMID = "+itemId;
		Connection con = DirectConnection.getConnection();
	    Statement st = con.createStatement();
	    st.executeUpdate(query);
	}

	private static void criaInteresse(boolean interesse, int userId, int itemId) throws SQLException {
		String query = "INSERT INTO INTERESSE (USERID , ITEMID , INTERESSE) VALUES ("+ userId +","+itemId+","+interesse+")";
		Connection con = DirectConnection.getConnection();
	    Statement st = con.createStatement();
	    st.executeUpdate(query);
	}

	private static int buscarInteresse(int userId, int itemId) throws SQLException {
		int interesseId = -1;
		String query = "SELECT INTERESSEID FROM INTERESSE "
		+ "WHERE USERID = " + userId + " ITEMID = " + itemId;
		
	    Connection con = DirectConnection.getConnection();
	    Statement st = con.createStatement();
	    ResultSet rs = st.executeQuery(query);
	    	 
	    if(rs.next()){
	    	interesseId = rs.getInt("INTERESSEID");
	    }
		return interesseId;
	}
}
