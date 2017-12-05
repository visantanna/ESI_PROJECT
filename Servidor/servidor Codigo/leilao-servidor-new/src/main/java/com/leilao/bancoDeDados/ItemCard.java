package com.leilao.bancoDeDados;

import java.math.BigDecimal;

public class ItemCard {

	/**
	 * Created by daniel on 06/10/2017.
	 */

	private int cod_item;
	private int cod_leilao;
	private int cod_lista_item;
	private Instituicao instituicao;
	private String nome;
	private String categoria;
	private String descricao;
	private BigDecimal lance_minimo;
	private BigDecimal lance_atual;
	private String dataFim;
	private String dataInicio;
	private int quantidade;
	private byte[] foto;
	private boolean interesse;

	public String getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}
	public String getDataFim() {
		return dataFim;
	}
	public void setDataFim(String data) {
		this.dataFim = data;
	}
	public Instituicao getInstituicao() {
		return instituicao;
	}
	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}
	public BigDecimal getLance_atual() {
		return lance_atual;
	}
	public void setLance_atual(BigDecimal lance_atual) {
		this.lance_atual = lance_atual;
	}
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getLance_minimo() {
		return lance_minimo;
	}
	public void setLance_minimo(BigDecimal lance_minimo) {
		this.lance_minimo = lance_minimo;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public void setLance_minimo(String lance_minimo) {
		String valor = lance_minimo.replace("R$","");
		valor = valor.replace(".", "");
		valor = valor.replace( ',' ,'.' );
		valor = valor.trim();
		this.lance_minimo = new BigDecimal(valor);
	}
	public void setLance_atual(String lance_atual) {
		String valor = lance_atual.replace("R$","");
		valor = valor.replace(".", "");
		valor = valor.replace( ',' ,'.' );
		valor = valor.trim();
		this.lance_minimo = new BigDecimal(valor);
	}
	public int getCod_item() {
		return cod_item;
	}
	public void setCod_item(int cod_item) {
		this.cod_item = cod_item;
	}
	public int getCod_leilao() {
		return cod_leilao;
	}
	public void setCod_leilao(int cod_leilao) {
		this.cod_leilao = cod_leilao;
	}
	public int getCod_lista_item() {
		return cod_lista_item;
	}
	public void setCod_lista_item(int cod_lista_item) {
		this.cod_lista_item = cod_lista_item;
	}
	public String getFormattedLance_minimo(){
		return "R$" + this.getLance_minimo().toString().replace('.',',');
	}
	public String getFormattedLance_atual(){
		return "R$" + this.getFormattedLance_atual().replace('.',',');
	}
	public boolean isInteresse() {
		return interesse;
	}
	public void setInteresse(boolean interesse) {
		this.interesse = interesse;
	}
	

}
