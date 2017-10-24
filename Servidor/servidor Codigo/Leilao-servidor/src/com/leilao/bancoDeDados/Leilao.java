package com.leilao.bancoDeDados;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "leilao")
public class Leilao {
	
	@Id
	@Column(name = "cod_leilao", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)		
	private int cod_leilao;
	
	//chave estrangeira
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_instituicao",referencedColumnName = "cod_instituicao")
	private Instituicao cod_instituicao;
	
	//chave estrangeira
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cod_lista",referencedColumnName = "cod_lista")
	private Lista_Itens cod_lista; 
	
	private boolean ativo;
	private String nome;
	private Date data_inicio;
	private Date data_termino;
	
	
	public Lista_Itens getCod_lista() {
		return cod_lista;
	}
	public void setCod_lista(Lista_Itens cod_lista) {
		this.cod_lista = cod_lista;
	}
	public Instituicao getCod_instituicao() {
		return cod_instituicao;
	}
	public void setCod_instituicao(Instituicao cod_instituicao) {
		this.cod_instituicao = cod_instituicao;
	}
	public int getCod_leilao() {
		return cod_leilao;
	}
	public void setCod_leilao(int cod_leilao) {
		this.cod_leilao = cod_leilao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getData_inicio() {
		return data_inicio;
	}
	public void setData_inicio(Date data_inicio) {
		this.data_inicio = data_inicio;
	}
	public Date getData_termino() {
		return data_termino;
	}
	public void setData_termino(Date data_termino) {
		this.data_termino = data_termino;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
}
