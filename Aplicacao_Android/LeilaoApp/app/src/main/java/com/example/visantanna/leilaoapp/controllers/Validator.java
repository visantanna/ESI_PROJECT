package com.example.visantanna.leilaoapp.controllers;

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

    public static boolean validaSenha(String senha){
        if(senha.length() < 8 ){
           return false;
        }
        return true;
    }
}
