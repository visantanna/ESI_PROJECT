package com.leilao;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class ClientRequestHandler extends Thread{
	private Socket socket;
	private String tipoUsuario;
	
    public ClientRequestHandler(Socket socket) {
        this.socket = socket;
        
        log("New connection at" + socket);
    }
	
    
    
    public void run() {
    	System.out.println("CONECTAMOS");
    	
        Gson gson = new Gson();
        
        Mensagem mensagem = new Mensagem();
        
        Usuario usuario = new Usuario();
        
		
        try {
        	//usaremos "in" para ler o que o cliente mandar
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            
            
           //usamos out para mandar mensagem para o cliente 
           PrintWriter out = new PrintWriter(
                    socket.getOutputStream(),true);
            
            System.out.println("Antes de print json");
            String json = in.readLine();
            
            System.out.println(" PRINT DO JSON"+json);
            
            //vamos char o cabecalho no json
            int index = json.indexOf("cabecalho")+11;
            
            Pattern pattern = Pattern.compile("\"(.*?)\"");
            Matcher matcher = pattern.matcher(json.substring(index));
                       
            //aqui pegamos o cabecalho
            String cabecalho;
            if (matcher.find())
            {
                cabecalho = matcher.group(1);
                
                if(cabecalho.equals("novo_usuario")) {
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
                	
                }else if(cabecalho.equals("login")) {
                	
                	Login login = gson.fromJson(json, Login.class); 
                	
                	mensagem = autenticaUsuario(login);
                	
            		json = gson.toJson(mensagem); 
            		
            		out.println(json);
            		out.write(json);
                    out.flush();
                    
                	
                }else if(cabecalho.equals("nova_instituicao")) {
                	
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
                }else if(cabecalho.equals("codigoRecuperarSenha")) {
                	
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
            		mensagem.setMensagem("Email n�o cadastrado");
        			
            		json = gson.toJson(mensagem); 
            		
            		out.println(json);
            		out.write(json);
                    out.flush();
                	
                }else if(cabecalho.equals("codigoRedefinirSenha")) {
                	
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
                		
            }//fim controle de fluxo
                         

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
							"Seu codigo de verifica��o �: " + String.valueOf(codigo)+cod_usuario+"0");
					
				}else if(tipoUsuario.equals("instituicao")) {
					
					Criteria criteria = session.createCriteria(Instituicao.class);  
					criteria.add( Restrictions.like("email", email) );
					List <Instituicao>results = criteria.list();
					instituicao = results.get(0);
					
					String cod_instituicao = String.valueOf(instituicao.getCod_instituicao());
					
					instituicao.setCodigoRecEmail(String.valueOf(codigo)+cod_instituicao+"1");
					session.update(instituicao);
					SendEmail sendEmail = new SendEmail(email,"Email de Recuperacao de Senha Leiloador",
							"Seu codigo de verifica��o �: " + String.valueOf(codigo)+cod_instituicao+"1");
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