package com.example.visantanna.leilaoapp.base;

import android.content.Context;

/**
 * Created by vis_a on 09-Nov-17.
 */

public class ContextHolder {

    private static Context AplicationContext;

    public ContextHolder(Context context){
        AplicationContext = context;
    }

    public static Context getAplicationContext() {
        return AplicationContext;
    }

    public static void setAplicationContext(Context aplicationContext) {
        AplicationContext = aplicationContext;
    }
}
