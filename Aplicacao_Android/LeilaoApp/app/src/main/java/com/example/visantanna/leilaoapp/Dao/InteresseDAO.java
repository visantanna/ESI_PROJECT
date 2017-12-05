package com.example.visantanna.leilaoapp.Dao;

import android.app.Activity;
import android.widget.ProgressBar;

import com.example.visantanna.leilaoapp.base.BaseDAO;
import com.example.visantanna.leilaoapp.controllers.MessageSender;
import com.example.visantanna.leilaoapp.controllers.Receiver;

/**
 * Created by vinicius on 05/12/17.
 */

public class InteresseDAO extends BaseDAO implements Receiver{

    private boolean interesse = false;

    public InteresseDAO(Activity activity , ProgressBar progressBar) {
        super(activity,progressBar);
    }

    public void atualizaInteresse(boolean interesse , int userId , int itemId){
        MessageSender transmissaoServidor = new MessageSender("interesse", Boolean.toString(interesse)+"/" + userId+"/"+itemId,this.getActivity(),this.getProgressBar(),this);
        transmissaoServidor.start();
    }

    @Override
    public void receive(Object obj) {
        if(obj instanceof MessageSender){
            MessageSender m = (MessageSender) obj;
            interesse = Boolean.getBoolean(m.getRetorno());
            setChanged();
            notifyObservers();
        }
    }

    public boolean isInteresse() {
        return interesse;
    }

    public void setInteresse(boolean interesse) {
        this.interesse = interesse;
    }
}
