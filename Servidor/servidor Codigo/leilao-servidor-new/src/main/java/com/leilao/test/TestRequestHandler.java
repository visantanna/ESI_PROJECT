package com.leilao.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.leilao.ClientRequestHandler;
import com.leilao.Mensagem;
import com.leilao.bancoDeDados.Usuario;

public class TestRequestHandler {
	ClientRequestHandler classeTest = null;
	SessionFactory sessionFactory = null; 
	Session session = null;
	Gson gson = null;
    Mensagem mensagem = null;
    PrintWriter out = null;
    Usuario usuario = null;
    ClientRequestHandler clientTest  = null;

	
	@Before
	public void setUp(){
		ServerSocket listener = null;
		
		try{
			out = new PrintWriter(new OutputStream() {
				
				@Override
				public void write(int b) throws IOException {
					// TODO Auto-generated method stub
					
				}
			});
			sessionFactory = new Configuration().configure().buildSessionFactory();
			session = sessionFactory.openSession();
			listener = new ServerSocket(9899);
			gson = new Gson();
			mensagem = new Mensagem();
			usuario = new Usuario();
			clientTest =  new ClientRequestHandler();
		}catch(Exception e){
			System.out.println(e + "");
			assertTrue(false);
		}
	}
	
	@Test
	public void testNovoUsuario(){
		Usuario novoUsuario = new Usuario();
		novoUsuario.setLogin("testNovoUsuario@test.com");
		novoUsuario.setEmail("testNovoUsuario@test.com");
		novoUsuario.setSenha("senha430");
		novoUsuario.setNome("testeServidorNovoUsuario");
		try {
			Method mtdVerificaUsuario;
			mtdVerificaUsuario = ClientRequestHandler.class.getDeclaredMethod("verificaUsuario",String.class);
			mtdVerificaUsuario.setAccessible(true);
			
			Method mtdNovoUsuario;
			mtdNovoUsuario = ClientRequestHandler.class.getDeclaredMethod("novoUsuario",Gson.class , Mensagem.class , PrintWriter.class , String.class );
			mtdNovoUsuario.setAccessible(true);

			if((boolean) mtdVerificaUsuario.invoke(clientTest, novoUsuario.getEmail())){
				session.beginTransaction();
	    		
				String selectQuery = "SELECT U.email FROM Usuario U WHERE U.email = :usernameParam";
				
				//Session session = sessionFactory.getCurrentSession();
		
				Query query = session.createQuery(selectQuery);
				query.setParameter("usernameParam", novoUsuario.getEmail());
				List<Object> retorno_usuario = query.list();
				
				novoUsuario =  (Usuario) retorno_usuario.get(0);
				Query q = session.createQuery("delete Entity where id U.email = :usernameParam");
				query.setParameter("usernameParam", novoUsuario.getEmail());
				q.executeUpdate();
			}
			mtdNovoUsuario.invoke(clientTest, gson , mensagem ,out , gson.toJson(novoUsuario));
			boolean contaCriada = (boolean) mtdVerificaUsuario.invoke(clientTest, novoUsuario.getEmail());
			assertTrue(contaCriada);
			
		} catch (IllegalAccessException  | IllegalArgumentException |  InvocationTargetException | NoSuchMethodException | SecurityException  e ) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}
