package com.example.visantanna.leilaoapp.Dao;

import com.example.visantanna.leilaoapp.Enum.Categorias;
import com.example.visantanna.leilaoapp.controllers.MessageSender;
import com.example.visantanna.leilaoapp.controllers.Receiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Observable;

/**
 * Created by vis_a on 08-Nov-17.
 */

public class ItemDAO extends Observable implements Receiver {
    List<ItemCard> listaItens;

    public void buscarItens(int qtdItens , int offset , Categorias tipo){
        String mensagem = qtdItens+"/"+offset+"/"+tipo.name();
        MessageSender transmissaoServidor = new MessageSender("Item", mensagem,null,null,this);
        transmissaoServidor.start();
    }

    @Override
    public void receive(Object obj) {
        if(obj instanceof MessageSender){
            MessageSender retorno = (MessageSender)obj;
            String json = retorno.getRetorno();
            Gson gson = new Gson();
            Type type = new TypeToken<List<ItemCard>>(){}.getType();
            listaItens = gson.fromJson(json , type);
            setChanged();
            notifyObservers();
        }
    }
    public List<ItemCard> getListaItens(){
        return listaItens;
    }

}
