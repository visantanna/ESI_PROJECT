package com.leilao;

import java.math.BigDecimal;
import java.net.ServerSocket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;

public class Main {
		public static void main(String[]args)throws Exception {
			//criando o ServerSsocket para ouvir na porta 9898
			ServerSocket listener = new ServerSocket(9898);
			
	        try {
	            while (true) {
	                new ClientRequestHandler(listener.accept()).start();
	            }
	        } finally {
	            listener.close();
	        }	
		}
}
