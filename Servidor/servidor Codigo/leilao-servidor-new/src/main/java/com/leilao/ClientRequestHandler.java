package com.leilao;



import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leilao.bancoDeDados.Instituicao;
import com.leilao.bancoDeDados.Item;
import com.leilao.bancoDeDados.ItemCard;
import com.leilao.bancoDeDados.Leilao;
import com.leilao.bancoDeDados.Login;
import com.leilao.bancoDeDados.Usuario;

import Dao.ItemCardDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientRequestHandler extends Thread{
	private Socket socket;
	private String tipoUsuario;
	
    public ClientRequestHandler(Socket socket) {
        this.socket = socket;
        
        log("New connection at" + socket);
    }
    //for testing
	public ClientRequestHandler(){
		
	}
    
    
    public void run() {
    	System.out.println("CONECTAMOS");
    	
    	GsonBuilder builder = new GsonBuilder();
    	builder.disableHtmlEscaping();
    	
        Gson gson = builder.create();

        Mensagem mensagem = new Mensagem();
        
        Usuario usuario = new Usuario();
        
		
        try {
        	//usaremos "in" para ler o que o cliente mandar
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            
            
           //usamos out para mandar mensagem para o cliente 
           PrintWriter out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(),StandardCharsets.UTF_8),true);
            
            System.out.println("Antes de print json");
            String json = in.readLine();
            
            System.out.println(" PRINT DO JSON"+json);
            
            mensagem = gson.fromJson(json, Mensagem.class);
            
            switch(mensagem.getCabecalho()){
                case "novo_usuario" :
                	novoUsuario(gson, mensagem, out, json);
                	break;
                case "login" :
                	login(gson, out, json);
                	break;
                case "nova_instituicao" :
                	novaInstituicao(gson, mensagem, out, json);
                	break;
                case "codigoRecuperarSenha":
                	recuperarSenha(gson, mensagem, out, json);
                	break;
                case "codigoRedefinirSenha":
                	redefinirSenha(gson, out, json);
                	break;
                case "Item":
                	retornaItens(gson,mensagem , out);
            }
          
                         

        } catch (IOException e) {
            log("Error handling client:" + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log("Couldn't close a socket");
            }
            log("Connection with client closed");
        }
    }



	private void  retornaItens(Gson gson, Mensagem mensagem , PrintWriter out ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		 String[] argumento = mensagem.getMensagem().split("/");
		 int qtdItens = Integer.parseInt(argumento[0]);
		 int offset   = Integer.parseInt(argumento[1]);
		 String categoria = argumento[2];
		 
		 ItemCardDao itemDao = new ItemCardDao();
		 List<ItemCard> lista = itemDao.buscarCardItens(offset, qtdItens, categoria);
	     Mensagem msgRetorno = new Mensagem();
		 msgRetorno.setCabecalho("sucesso");
	     String jsonMensagem = gson.toJson(lista);
	     msgRetorno.setMensagem(jsonMensagem);
	     String json = gson.toJson(msgRetorno);
	     out.write(json);
	     out.flush();
	     
	}
	
	
	private void redefinirSenha(Gson gson, PrintWriter out, String json) {
		Mensagem mensagem;
		Login redefinirSenha = gson.fromJson(json, Login.class); 
		
			
			mensagem = alterarSenha(redefinirSenha);
			
			if(mensagem.getCabecalho().equals("senhaAlterada")) {
			
				mensagem.setMensagem("Senha Alterada Com Sucesso");
				//retorna ok para o cliente
				json = gson.toJson(mensagem); 

			}else {
		    	//retorna mensagem de erro
		    	mensagem.setCabecalho("erro");
				mensagem.setMensagem("Codigo Invalido");
			}
				
		json = gson.toJson(mensagem); 
		
		out.println(json);
		out.write(json);
		out.flush();
	}



	private void recuperarSenha(Gson gson, Mensagem mensagem, PrintWriter out, String json) {
		Mensagem recuperaSenha = gson.fromJson(json, Mensagem.class); 
		
		//verificando se o email inserido pelo usuario esta disponivel
		boolean usuario_existe = verificaUsuario(recuperaSenha.getMensagem());
			
		//se o email estiver disponivel, salva no banco de dados
		if(usuario_existe){	
			
			mensagem = salvarCodigoRecuperacao(recuperaSenha.getMensagem());
			
			mensagem.setMensagem("Codigo Enviado com Sucesso");
				
			//retorna ok para o cliente
			json = gson.toJson(mensagem); 
			
			out.println(json);
			out.write(json);
		    out.flush();
		}
			
		//retorna mensagem de erro
		mensagem.setCabecalho("erro");
		mensagem.setMensagem("Email não cadastrado");
		
		json = gson.toJson(mensagem); 
		
		out.println(json);
		out.write(json);
		out.flush();
	}



	private void novaInstituicao(Gson gson, Mensagem mensagem, PrintWriter out, String json) {
		Instituicao instituicao = gson.fromJson(json, Instituicao.class); 
			
		//verificando se o email inserido pelo usuario esta disponivel
		boolean usuario_existe = verificaUsuario(instituicao.getEmail());
			
		//se o email estiver disponivel, salva no banco de dados
		if(!usuario_existe){	
			
			mensagem = salvarNoBanco(instituicao);
			
				
			//retorna ok para o cliente
			json = gson.toJson(mensagem); 
			
			out.println(json);
			out.write(json);
		    out.flush();
		}
			
		//retorna mensagem de erro
		mensagem.setCabecalho("erro");
		mensagem.setMensagem("Email nao disponivel");
		
		//retorna ok para o cliente
		json = gson.toJson(mensagem); 
		
		out.println(json);
		out.write(json);
		out.flush();
	}



	private void login(Gson gson, PrintWriter out, String json) {
		Mensagem mensagem;
		Login login = gson.fromJson(json, Login.class); 
		
		mensagem = autenticaUsuario(login);
		
		json = gson.toJson(mensagem); 
		
		out.println(json);
		out.write(json);
		out.flush();
	}



	private void novoUsuario(Gson gson, Mensagem mensagem, PrintWriter out, String json) {
		Usuario usuario;
		usuario = gson.fromJson(json, Usuario.class); 
			
		//verificando se o email inserido pelo usuario esta disponivel
		boolean usuario_existe = verificaUsuario(usuario.getEmail());
			
		//se o email estiver disponivel, salva no banco de dados
		if(!usuario_existe){	
			
			mensagem = salvarNoBanco(usuario);
			
			mensagem.setMensagem("Conta criada com sucesso");
				
			//retorna ok para o cliente
			json = gson.toJson(mensagem); 
			
			out.println(json);
			out.write(json);
		    out.flush();
		}
			
		//retorna mensagem de erro
		mensagem.setCabecalho("erro");
		mensagem.setMensagem("Email nao disponivel");
		
		//retorna ok para o cliente
		json = gson.toJson(mensagem); 
		
		out.println(json);
		out.write(json);
		out.flush();
	}
    
    //-------------------------------------------------------------
    
    public Mensagem autenticaUsuario(Login login) {
    	
    	SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory(); 
    	Session session = sessionFactory.openSession();
    	
    	Mensagem mensagem = new Mensagem();
    	
    	try {
    					
    			session.beginTransaction();
    		
    			String selectQuery = "SELECT U.email,U.senha FROM Usuario U WHERE U.email = :usernameParam AND U.senha = :userPasswordParam";
    			
    			//Session session = sessionFactory.getCurrentSession();
    	
    			Query query = session.createQuery(selectQuery);
    			query.setParameter("usernameParam", login.getEmail());
    			query.setParameter("userPasswordParam", login.getSenha());
    			@SuppressWarnings("unchecked")
    			List<Usuario> retorno_usuario = query.list();
    			
    			System.out.println(retorno_usuario);
    			
    			if(!retorno_usuario.isEmpty()) {
    				mensagem.setCabecalho("usuarioAutenticado");
    				mensagem.setMensagem("Usuario autenticado com sucesso!");
    				return mensagem;
    			}else {
    				//verificando se o usuario e senha sao de uma instituicao
    				selectQuery = "SELECT I.email,I.senha FROM Instituicao I WHERE I.email = :usernameParam AND I.senha = :userPasswordParam";
        			query = session.createQuery(selectQuery);
        			query.setParameter("usernameParam", login.getEmail());
        			query.setParameter("userPasswordParam", login.getSenha());
    				
    				List<Instituicao> retorno_instituicao = query.list();
    				
    				if(!retorno_instituicao.isEmpty()) {
        				mensagem.setCabecalho("instituicaoAutenticada");
        				mensagem.setMensagem("Instituicao autenticada com sucesso!");
        				return mensagem;
    				}else {
        				mensagem.setCabecalho("erro");
        				mensagem.setMensagem("Email ou senha incorretos!");
        				return mensagem;
    				}
    				
    			}//fim do else
    				
    			
    	}catch(HibernateException e){
    		
    		session.getTransaction().rollback();
    		e.printStackTrace();
    		
    		mensagem.setCabecalho("erro");
    		mensagem.setMensagem(e.toString());
    		return mensagem;	
    		
    	}finally {
    		session.close();
    		sessionFactory.close();
    	}    	   	
    }//fim do metodo autenticaUsuario
    
    //-------------------------------------------------------------
    
    private boolean verificaUsuario(String email) {
    	
    	SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory(); 
    	Session session = sessionFactory.openSession();
    
    	try {
    					 		
			session.beginTransaction();
    		
			String selectQuery = "SELECT U.email FROM Usuario U WHERE U.email = :usernameParam";
			
			//Session session = sessionFactory.getCurrentSession();
	
			Query query = session.createQuery(selectQuery);
			query.setParameter("usernameParam", email);
			@SuppressWarnings("unchecked")
			List<Object> retorno_usuario = query.list();
			
			System.out.println(retorno_usuario);
			
			//se um usuario ja esta usando este email, retorna true
			if(!retorno_usuario.isEmpty()) {
				tipoUsuario = "usuario";
				return true;
			}else {
				//verificando se o usuario e senha sao de uma instituicao
				selectQuery = "SELECT I.email FROM Instituicao I WHERE I.email = :usernameParam";
    			query = session.createQuery(selectQuery);
    			query.setParameter("usernameParam", email);
    			
				
				List<Object> retorno_instituicao = query.list();
				
				//email ja em uso
				if(!retorno_instituicao.isEmpty()) {
					tipoUsuario = "instituicao";
    				return true;
				}else {
					//email disponivel
    				return false;
				}
				
			}//fim do else			
    	}catch(HibernateException e){
    		session.getTransaction().rollback();
    		e.printStackTrace();
    		return true;
    	}finally {
    		session.close();
    		sessionFactory.close();
    	}
    
  
    }
    
    //-------------------------------------------------------------
    
    private Mensagem salvarNoBanco(Object objeto) {
    	
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();	    	
    	
		Mensagem mensagem = new Mensagem();
		
        try {       								
				//devemos usar transacoes para operacoes de insercao, remocao e modificacao.
				session.beginTransaction();
				//salva o usuario no banco
				session.save(objeto);
				
				session.getTransaction().commit();
				            	
		} catch (HibernateException e) {
			
			session.getTransaction().rollback();
					
	        mensagem.setCabecalho("erro");
	        mensagem.setMensagem(e.toString());
	        
	        e.printStackTrace();
	        
	        return mensagem;		
			
		} finally {
			session.close();
			sessionFactory.close();
		}
        
		mensagem.setCabecalho("sucesso");
		mensagem.setMensagem("Conta criada com sucesso");
		return mensagem;  
        
    }//fim do metodo SalvarNoBanco
    
    //-------------------------------------------------------------
    
    private Mensagem salvarCodigoRecuperacao(String email) {
    	
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();	    	
    	
		Mensagem mensagem = new Mensagem();
		Usuario usuario = new Usuario();
		Instituicao instituicao = new Instituicao();
		
		//gerando um codigo aleatorio de 8 digitos
		Random r = new Random();
		int Low = 100;
		int High = 999;
		int codigo = r.nextInt(High-Low) + Low;
		
		
        try {       								
				//devemos usar transacoes para operacoes de insercao, remocao e modificacao.
				session.beginTransaction();
				
				if(tipoUsuario.equals("usuario")) {
					//recuperando os dados do banco para poder adicionar o codigo de recuperacao
					Criteria criteria = session.createCriteria(Usuario.class);  
					criteria.add( Restrictions.like("email", email) );
					List <Usuario>results = criteria.list();
					usuario = results.get(0);
					//usado para o numero so servir para aquele usuario
					String cod_usuario = String.valueOf(usuario.getCod_usuario());
					usuario.setCodigoRecEmail(String.valueOf(codigo)+cod_usuario+"0");
					session.update(usuario);
					SendEmail sendEmail = new SendEmail(email,"Email de Recuperacao de Senha Leiloador",
							"Seu codigo de verificação é: " + String.valueOf(codigo)+cod_usuario+"0");
					
				}else if(tipoUsuario.equals("instituicao")) {
					
					Criteria criteria = session.createCriteria(Instituicao.class);  
					criteria.add( Restrictions.like("email", email) );
					List <Instituicao>results = criteria.list();
					instituicao = results.get(0);
					
					String cod_instituicao = String.valueOf(instituicao.getCod_instituicao());
					
					instituicao.setCodigoRecEmail(String.valueOf(codigo)+cod_instituicao+"1");
					session.update(instituicao);
					SendEmail sendEmail = new SendEmail(email,"Email de Recuperacao de Senha Leiloador",
							"Seu codigo de verificação é: " + String.valueOf(codigo)+cod_instituicao+"1");
				}
				
				session.getTransaction().commit();
			
				//System.out.println(email);
				mensagem.setCabecalho("codigoEnviado");
				
		} catch (HibernateException e) {
			
			session.getTransaction().rollback();
					
	        mensagem.setCabecalho("erro");
	        mensagem.setMensagem(e.toString());
	        
	        e.printStackTrace();
	        
	        return mensagem;		
			
		} finally {
			session.close();
			sessionFactory.close();
		}
        
		mensagem.setCabecalho("codigoEnviado");
		mensagem.setMensagem("Numero Enviado Com Sucesso");
		return mensagem;  
        
    }//fim do metodo
    
    //-------------------------------------------------------------
    
    
    
    //-------------------------------------------------------------
    
    private Mensagem alterarSenha(Login login) {
    	
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();	    	
		
		Mensagem mensagem = new Mensagem();
		Usuario usuario = new Usuario();
		Instituicao instituicao = new Instituicao();
		
		//verificando se o codigo tem so numeros
		if ( !login.getEmail().matches("[0-9]+") ){
			mensagem.setCabecalho("erro");
			return mensagem;
		}
		
		
		if(login.getEmail().equals("0")) {
			mensagem.setCabecalho("erro");
			return mensagem;
		}
		
		if(login.getEmail().length() < 5 || login.getEmail().length() > 12) {
			mensagem.setCabecalho("erro");
			return mensagem;
		}
		
		
		String cod_usuario = login.getEmail().substring(3,login.getEmail().length()-1);
		int codigo_usuario = Integer.parseInt(cod_usuario);
		String tipo_usuario = login.getEmail().substring(login.getEmail().length()-1);
		
		System.out.println("Codigo usuario:" + codigo_usuario);
		System.out.println("tipo usuario:"+ tipo_usuario);
		
		
		session.beginTransaction();
		
        try {       	
        	if(tipo_usuario.equals("0")) {
				
					//recuperando os dados do banco para poder adicionar o codigo de recuperacao
					Criteria criteria = session.createCriteria(Usuario.class);  
					criteria.add( Restrictions.eq("cod_usuario",codigo_usuario) );
					List <Usuario>results = criteria.list();
					
					if(results.isEmpty()) {
						mensagem.setCabecalho("erro");
						return mensagem;
					}
					
					usuario = results.get(0);
					String codigo_database = usuario.getCodigoRecEmail();
					
					if(codigo_database.equals(login.getEmail())) {
						usuario.setSenha(login.getSenha());
						usuario.setCodigoRecEmail("0");
						mensagem.setCabecalho("senhaAlterada");
						
						session.update(usuario);
					}
					else {
						mensagem.setCabecalho("erro");
					}
					
					
				}else if(tipo_usuario.equals("1")) {
					
					Criteria criteria = session.createCriteria(Instituicao.class);  
					criteria.add( Restrictions.eq("cod_instituicao", codigo_usuario) );
					List <Instituicao>results = criteria.list();
					
					
					if(results.isEmpty()) {
						mensagem.setCabecalho("erro");
						return mensagem;
					}
					
					instituicao = results.get(0);
					String codigo_database = instituicao.getCodigoRecEmail();
					
					if(codigo_database.equals(login.getEmail())) {
						instituicao.setSenha(login.getSenha());
						instituicao.setCodigoRecEmail("0");
						mensagem.setCabecalho("senhaAlterada");
						
						session.update(instituicao);
						
					}else {
						mensagem.setCabecalho("erro");
					}
					

				}else {
					mensagem.setCabecalho("erro");
				}
				
				session.getTransaction().commit();
				
		} catch (HibernateException e) {
			
			session.getTransaction().rollback();
					
	        mensagem.setCabecalho("erro");
	        mensagem.setMensagem(e.toString());
	        
	        e.printStackTrace();
	        
	        return mensagem;		
			
		} finally {
			session.close();
			sessionFactory.close();
		}
        
		return mensagem;  
        
    }//fim do metodo SalvarNoBanco  
    
    
    //-------------------------------------------------------------
    
    private void log(String message) {
        System.out.println(message);
    } 
    
    
    
    
}
