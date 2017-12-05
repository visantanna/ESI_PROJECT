package com.example.visantanna.leilaoapp.Dao;

import android.app.Activity;
import android.content.Context;
import android.widget.ProgressBar;

import com.example.visantanna.leilaoapp.base.BaseDAO;
import com.example.visantanna.leilaoapp.base.ContextHolder;
import com.example.visantanna.leilaoapp.controllers.MessageSender;
import com.example.visantanna.leilaoapp.controllers.Receiver;
import com.example.visantanna.leilaoapp.controllers.RetornoLance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Observable;

/**
 * Created by vinicius on 04/12/17.
 */

public class lanceDAO extends BaseDAO implements Receiver {



    public lanceDAO(Activity act , ProgressBar pb){
        super(act , pb);
    }

    private RetornoLance retornoLance;
    public void darLance(ItemCard item , int idUser){
        String mensagem = "";
        if(item.getLance_atual() != null ) {
            mensagem = item.getCod_lista_item() + "/" + item.getLance_atual()+"/"+idUser;
        }
        MessageSender transmissaoServidor = new MessageSender("darLance", mensagem,this.getActivity(),this.getProgressBar(),this);
        transmissaoServidor.start();
    }

    @Override
    public void receive(Object obj) {
        if(obj instanceof MessageSender) {
            MessageSender retorno = (MessageSender) obj;
            String json = retorno.getRetorno();
            GsonBuilder builder = new GsonBuilder();
            builder.disableHtmlEscaping();
            Gson gson = builder.create();
            retornoLance = (RetornoLance) gson.fromJson(json, RetornoLance.class);
            setChanged();
            notifyObservers();
        }
    }
    public RetornoLance getRetornoLance() {
        return retornoLance;
    }

    public void setRetornoLance(RetornoLance retornoLance) {
        this.retornoLance = retornoLance;
    }
}
