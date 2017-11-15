package com.leilao.bancoDeDados;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "usuario")
public class Usuario {
	
	
	@Id
	@Column(name = "cod_usuario", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cod_usuario;
	
	
	//vamos usar esta string para dizer o proposito da mensagem que vier pelos sockets
	@Transient
	private String cabecalho;
	
	private String nome;
	private String sobrenome;
	private String apelido;	
	private String sexo;
	private String cpf;
	private String rg;
	private String telefone_fixo;
	private String telefone_movel;
	private String email;
	private String login;
	private String senha;
	private String estado;
	private String cidade;
	private String bairro;
	private String rua;
	private int numero;
	private String complemento;
	private String CEP;
	private int pontuacao;
	private String codigoRecEmail; 
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public String getApelido() {
		return apelido;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	public int getCod_usuario() {
		return cod_usuario;
	}
	public void setCod_usuario(int cod_usuario) {
		this.cod_usuario = cod_usuario;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getRg() {
		return rg;
	}
	public void setRg(String rg) {
		this.rg = rg;
	}
	public String getTelefone_fixo() {
		return telefone_fixo;
	}
	public void setTelefone_fixo(String telefone_fixo) {
		this.telefone_fixo = telefone_fixo;
	}
	public String getTelefone_movel() {
		return telefone_movel;
	}
	public void setTelefone_movel(String telefone_movel) {
		this.telefone_movel = telefone_movel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getCEP() {
		return CEP;
	}
	public void setCEP(String cEP) {
		CEP = cEP;
	}
	public int getPontuacao() {
		return pontuacao;
	}
	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}	
	public String getCabecalho() {
		return cabecalho;
	}
	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	
	public String getCodigoRecEmail() {
		return codigoRecEmail;
	}
	public void setCodigoRecEmail(String codigoRecEmail) {
		this.codigoRecEmail = codigoRecEmail;
	}
	
}
