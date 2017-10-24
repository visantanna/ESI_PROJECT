package com.example.visantanna.leilaoapp.controllers;

import com.example.visantanna.leilaoapp.db_classes.Mensagem;
import com.example.visantanna.leilaoapp.db_classes.Usuario;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vis_a on 17-Sep-17.
 */

public class Validator {
    public static boolean validaEmail(String email){
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex , Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
    public static String allTrim(String texto){
        if(texto == null ){
            return "";
        }else{
            return texto.replace(" ", "");
        }
    }
    public static boolean validaSenha (String senha ){
        boolean isOk = true;
        if(isOk){
            if(senha.length() < 8){
                isOk = false;
            }
        }
        return isOk;
    }
    public static MensagemRetorno validaSenhaCadastro(String senha1, String senha2) {
        MensagemRetorno retorno = new MensagemRetorno();
        retorno.isOk(true);
        if((Validator.allTrim(senha1).isEmpty())|| (Validator.allTrim(senha2).isEmpty())){
            retorno.isOk(false);
            retorno.setMensagem("Senha não foi preenchida!");
        }
        if(retorno.isOk()){
            if(!senha1.equals(senha2)){
                retorno.isOk(false);
                retorno.setMensagem("As senhas são diferentes!");
            }
        }
        if(retorno.isOk()){
            if(senha1.length() < 8 ){
                retorno.isOk(false);
                retorno.setMensagem("A senha deve ter 8 ou mais caracteres!");
            }
        }
        if(retorno.isOk()){
           retorno.isOk(true);
        }
        return retorno;
    }
    public static MensagemRetorno validaNomeCadastro(String nomeCompleto){
        MensagemRetorno retorno = new MensagemRetorno();
        retorno.setMensagem("");
        if(Validator.allTrim(nomeCompleto).isEmpty()){
            retorno.setMensagem("O campo Nome está vazio!");
            retorno.isOk(false);
        }
        else{
            retorno.isOk(true);
        }
        return retorno;
    }
    public static MensagemRetorno validaEmailCadastro(String email) {
        MensagemRetorno retorno = new MensagemRetorno();
        retorno.isOk(true);
        if(Validator.allTrim(email).isEmpty()){
            retorno.setMensagem("O campo de E-mail está vazio!");
            retorno.isOk(false);
        }else {
            if(!Validator.validaEmail(email)){
                retorno.setMensagem("E-mail inválido!");
                retorno.isOk(false);
            }
            else {
                retorno.setMensagem("");
            }
        }
        return  retorno;
    }

}

