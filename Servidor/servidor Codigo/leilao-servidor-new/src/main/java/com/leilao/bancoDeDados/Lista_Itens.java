package com.leilao.bancoDeDados;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table( name = "lista_de_itens" )
public class Lista_Itens {

	@Id
	@Column(name = "cod_lista", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)		
	private int cod_lista;
	
	//lembrar de diminuir a quantidade da tabela "item" ao alterar aqui.
	private int quantidade;
	private double valor_atual;
	
	//chave estrangeira
	private int id_usuario_ml1;
	private int id_usuario_ml2;
	private int id_usuario_ml3;
	private int id_usuario_ml4;
	private int id_usuario_ml5;
	
	@ManyToOne
	private Leilao leilao;
	
	//chave estrangeira
	@OneToOne(fetch = FetchType.LAZY )
	@PrimaryKeyJoinColumn
	private Item cod_item;

	@Column (name = "cod_leilao")
	@PrimaryKeyJoinColumn
	private int cod_leilao;
	
	public int getCod_leilao() {
		return cod_leilao;
	}

	public void setCod_leilao(int cod_leilao) {
		this.cod_leilao = cod_leilao;
	}

	public int getCod_lista() {
		return cod_lista;
	}

	public void setCod_lista(int cod_lista) {
		this.cod_lista = cod_lista;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getValor_atual() {
		return valor_atual;
	}

	public void setValor_atual(double valor_atual) {
		this.valor_atual = valor_atual;
	}

	public int getId_usuario_ml1() {
		return id_usuario_ml1;
	}

	public void setId_usuario_ml1(int id_usuario_ml1) {
		this.id_usuario_ml1 = id_usuario_ml1;
	}

	public int getId_usuario_ml2() {
		return id_usuario_ml2;
	}

	public void setId_usuario_ml2(int id_usuario_ml2) {
		this.id_usuario_ml2 = id_usuario_ml2;
	}

	public int getId_usuario_ml3() {
		return id_usuario_ml3;
	}

	public void setId_usuario_ml3(int id_usuario_ml3) {
		this.id_usuario_ml3 = id_usuario_ml3;
	}

	public int getId_usuario_ml4() {
		return id_usuario_ml4;
	}

	public void setId_usuario_ml4(int id_usuario_ml4) {
		this.id_usuario_ml4 = id_usuario_ml4;
	}

	public int getId_usuario_ml5() {
		return id_usuario_ml5;
	}

	public void setId_usuario_ml5(int id_usuario_ml5) {
		this.id_usuario_ml5 = id_usuario_ml5;
	}

	public Item getCod_item() {
		return cod_item;
	}

	public void setCod_item(Item cod_item) {
		this.cod_item = cod_item;
	}

	public Leilao getLeilao() {
		return leilao;
	}

	public void setLeilao(Leilao leilao) {
		this.leilao = leilao;
	}

}
