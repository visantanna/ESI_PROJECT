package com.leilao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "lista_compras")
public class Lista_Compras {

	@Id
	@Column(name = "cod_compra", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int cod_compra;
	
	//chave estrangeira
	private int cod_item;
	
	//chave estrangeira
	private int cod_usuario;
	
	//chave estrangeira
	private int cod_instituicao;
	
	private double preco;
	private Date data_compra;
	private boolean pago;
	private boolean entregue;
	
	
	public int getCod_compra() {
		return cod_compra;
	}
	public void setCod_compra(int cod_compra) {
		this.cod_compra = cod_compra;
	}
	public int getCod_item() {
		return cod_item;
	}
	public void setCod_item(int cod_item) {
		this.cod_item = cod_item;
	}
	public int getCod_usuario() {
		return cod_usuario;
	}
	public void setCod_usuario(int cod_usuario) {
		this.cod_usuario = cod_usuario;
	}
	public int getCod_instituicao() {
		return cod_instituicao;
	}
	public void setCod_instituicao(int cod_instituicao) {
		this.cod_instituicao = cod_instituicao;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	public Date getData_compra() {
		return data_compra;
	}
	public void setData_compra(Date data_compra) {
		this.data_compra = data_compra;
	}
	public boolean isPago() {
		return pago;
	}
	public void setPago(boolean pago) {
		this.pago = pago;
	}
	public boolean isEntregue() {
		return entregue;
	}
	public void setEntregue(boolean entregue) {
		this.entregue = entregue;
	}
	
}
