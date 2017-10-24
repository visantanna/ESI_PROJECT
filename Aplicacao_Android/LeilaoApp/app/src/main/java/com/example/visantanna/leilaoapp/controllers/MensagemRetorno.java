package com.example.visantanna.leilaoapp.controllers;

/**
 * Created by vis_a on 22-Oct-17.
 */

public class MensagemRetorno {
    private boolean isOk = false;
    private String mensagem = "";

    public MensagemRetorno(boolean isOk,String mensagem){
        this.isOk = isOk;
        this.mensagem = mensagem;
    }
    public MensagemRetorno(){

    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void isOk(boolean ok) {
        isOk = ok;
    }

    public boolean isOk(){
        return isOk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MensagemRetorno that = (MensagemRetorno) o;

        if (isOk != that.isOk) return false;
        return mensagem != null ? mensagem.equals(that.mensagem) : that.mensagem == null;

    }
}
