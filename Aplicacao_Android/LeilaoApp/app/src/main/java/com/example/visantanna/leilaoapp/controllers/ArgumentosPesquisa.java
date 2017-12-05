package com.example.visantanna.leilaoapp.controllers;

import com.example.visantanna.leilaoapp.Enum.Categorias;

/**
 * Created by vinicius on 03/12/17.
 */

public class ArgumentosPesquisa {
    private Categorias categoria;
    private int posicaoTab;
    private String textoPesquisa;

    public ArgumentosPesquisa(Categorias categoria, int posicaoTab, String textoPesquisa) {
        this.categoria = categoria;
        this.posicaoTab = posicaoTab;
        this.textoPesquisa = textoPesquisa;
    }

    public ArgumentosPesquisa() {
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public int getPosicaoTab() {
        return posicaoTab;
    }

    public void setPosicaoTab(int posicaoTab) {
        this.posicaoTab = posicaoTab;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }
    public String getTextoPesquisa() {
        return textoPesquisa;
    }

    public void setTextoPesquisa(String textoPesquisa) {
        this.textoPesquisa = textoPesquisa;
    }
}
