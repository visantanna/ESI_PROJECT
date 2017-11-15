package com.leilao;

import java.net.ServerSocket;




public class Main {
		public static void main(String[]args)throws Exception {
			//criando o ServerSsocket para ouvir na porta 9898
			DirectConnection dir = new DirectConnection();
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
