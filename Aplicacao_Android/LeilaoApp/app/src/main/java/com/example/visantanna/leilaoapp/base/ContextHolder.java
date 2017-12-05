package com.example.visantanna.leilaoapp.base;

import android.content.Context;

/**
 * Created by vis_a on 09-Nov-17.
 */

public class ContextHolder {

    private static Context AplicationContext;
    private static int id_user;

    public ContextHolder(Context context){
        AplicationContext = context;
    }

    public static Context getAplicationContext() {
        return AplicationContext;
    }

    public static void setAplicationContext(Context aplicationContext) {
        AplicationContext = aplicationContext;
    }

    public static int getId_user() {
        return id_user;
    }

    public static void setId_user(int id_user) {
        ContextHolder.id_user = id_user;
    }
}
