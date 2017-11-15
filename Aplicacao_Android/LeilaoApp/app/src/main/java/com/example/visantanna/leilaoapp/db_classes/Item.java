package com.example.visantanna.leilaoapp.db_classes;

import java.math.BigDecimal;

/**
 * Created by daniel on 06/10/2017.
 */

public class Item {
    private int cod_item;
    private Instituicao instituicao;
    private String nome;
    private String categoria;
    private String descricao;
    private BigDecimal lance_minimo;
    private int quantidade;
    private byte[] foto;

    public Instituicao getInstituicao() {
        return instituicao;
    }
    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
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
   /* public int getCod_item() {
        return cod_item;
    }
    public void setCod_item(int cod_item) {
        this.cod_item = cod_item;
    }
    */
}
