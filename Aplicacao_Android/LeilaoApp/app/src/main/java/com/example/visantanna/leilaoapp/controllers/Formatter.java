package com.example.visantanna.leilaoapp.controllers;

/**
 * Created by vis_a on 30-Nov-17.
 */

public class Formatter {
    public static String formatPhone(String tel){
        if(tel.length() > 11) {
            if(tel.length() > 12){
                return tel.substring(0, 2) + " " + tel.substring(2, 4)+" "+ tel.substring(4, 9) + "-" + tel.substring(9, tel.length());
            }else{
                return tel.substring(0, 2) + " " + tel.substring(2, 4)+" "+ tel.substring(4, 8) + "-" + tel.substring(8, tel.length());
            }

        }
        return tel;
    }
}
