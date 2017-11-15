package com.leilao.bancoDeDados;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


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
	@OneToMany(mappedBy = "cod_leilao" , targetEntity = Lista_Itens.class , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private List<Lista_Itens> listaItens; 
	
	private boolean ativo;
	private String nome;
	
	private Timestamp data_inicio;
	
	private Timestamp data_termino;
	
	
	public List<Lista_Itens> getCod_lista() {
		return listaItens;
	}
	public void setCod_lista(List<Lista_Itens> listaItens) {
		this.listaItens = listaItens;
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
	public Timestamp getData_inicio() {
		return data_inicio;
	}
	public void setData_inicio(Timestamp data_inicio) {
		this.data_inicio = data_inicio;
	}
	public Timestamp getData_termino() {
		return data_termino;
	}
	public void setData_termino(Timestamp data_termino) {
		this.data_termino = data_termino;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
