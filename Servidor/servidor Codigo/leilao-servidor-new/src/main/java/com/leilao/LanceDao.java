package com.leilao;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.leilao.bancoDeDados.Leilao;
import com.leilao.bancoDeDados.Lista_Itens;

public class LanceDao {

	public static void darLance(Gson gson, Mensagem mensagem, PrintWriter out, Session session) {
		String[] argumentos = mensagem.getMensagem().split("/");
		int codListaItem = Integer.parseInt(argumentos[1]);
		BigDecimal lance = new BigDecimal(argumentos[2]);
		int idUser = Integer.parseInt(argumentos[3]);
				
		Lista_Itens lista = session.get(Lista_Itens.class, codListaItem);
		Leilao leilao = session.get(Leilao.class, lista.getCod_leilao());
		Timestamp dataFim = leilao.getData_termino();
		
		Mensagem msgRetorno = new Mensagem();
		RetornoLance conteudoMensagem = new RetornoLance();
		
		if((lista != null) && (dataFim.after(new Date()))){
			if(lista.getValor_atual() < lance.doubleValue()) {
				lista.setValor_atual(lance.doubleValue());
				lista.setId_usuario_ml5(lista.getId_usuario_ml4());
				lista.setId_usuario_ml4(lista.getId_usuario_ml3());
				lista.setId_usuario_ml3(lista.getId_usuario_ml2());
				lista.setId_usuario_ml2(lista.getId_usuario_ml1());
				lista.setId_usuario_ml1(idUser);
				conteudoMensagem.setLanceAtual(lance);
				conteudoMensagem.setSucessoLance(true);
			}else {
				conteudoMensagem.setLanceAtual(new BigDecimal(lista.getValor_atual()));
				conteudoMensagem.setSucessoLance(false);
			}	
			msgRetorno.setCabecalho("sucesso");
		}
		String json = gson.toJson(msgRetorno);
		
		out.write(json);
		out.flush();
	}

}
