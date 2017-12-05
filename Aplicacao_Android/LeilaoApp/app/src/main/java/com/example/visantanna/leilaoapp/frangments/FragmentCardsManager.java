package com.example.visantanna.leilaoapp.frangments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.visantanna.leilaoapp.Dao.ItemDAO;
import com.example.visantanna.leilaoapp.Enum.Categorias;
import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.controllers.ArgumentosPesquisa;
import com.example.visantanna.leilaoapp.controllers.ItemCardAdapter;
import com.example.visantanna.leilaoapp.Dao.ItemCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by vis_a on 05-Nov-17.
 */

public class FragmentCardsManager extends Fragment implements Observer {

    private View view;
    private List<ItemCard> listaItens = new ArrayList<ItemCard>();
    private RecyclerView recyclerView;
    private Activity activityUI;

    public FragmentCardsManager() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragmento_itens,container,false);
        criaCards(listaItens);
        return view;
    }

    private void criaCards(List<ItemCard> listaItens) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ArgumentosPesquisa args = new ArgumentosPesquisa(Categorias.NENHUM , -1, "");
        buscarCards(false, true , args );
        atualizaCardView();
    }
    //quando updateList for true serão carregados mais cards (se tiver)
    private void buscarCards(boolean updateList , boolean firstLoad , ArgumentosPesquisa args) {
        if(firstLoad || updateList ){
            ItemDAO itemDao = new ItemDAO();
            itemDao.addObserver(this);
            itemDao.buscarItens(20,listaItens.size(), args.getCategoria() , args.getTextoPesquisa());
        }
    }

    private void atualizaCardView(){
        activityUI.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ItemCardAdapter adapter = new ItemCardAdapter(listaItens);
                adapter.setActivity(activityUI);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void setListItens(List<ItemCard> listaItens ){
        this.listaItens = listaItens;

    }

    @Override
    public void update(Observable o, Object arg) {
        List<ItemCard> lista = ((ItemDAO)o).getListaItens();
        setListItens(lista);
        atualizaCardView();
    }

    public Activity getActivityUI() {
        return activityUI;
    }

    public void setActivityUI(Activity activityUI) {
        this.activityUI = activityUI;
    }

    public void changeSearch(ArgumentosPesquisa args){
        buscarCards( true , false , args);
    }
}
