package com.example.visantanna.leilaoapp.controllers;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.Dao.ItemCard;

import java.util.List;

/**
 * Created by vis_a on 05-Nov-17.
 */

public class ItemCardAdapter extends RecyclerView.Adapter<ItemHolder> {
    List<ItemCard> listaItens;
    Activity activity;

    public ItemCardAdapter(List<ItemCard> listaItens){
        this.listaItens = listaItens;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home ,parent ,false);

        inflatedView.setAlpha(0f);
        inflatedView.animate().alpha(1f).setDuration(200);
        ItemHolder itemHolder = new ItemHolder(inflatedView);
        itemHolder.setActivity(this.activity);
        return itemHolder ;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        ItemCard itemInformacao = listaItens.get(position);
        holder.bindItem(itemInformacao);
    }

    @Override
    public int getItemCount() {
        return listaItens.size();
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }
}
