package com.example.visantanna.leilaoapp.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.View.Home.LeilaoActivity;
import com.example.visantanna.leilaoapp.base.ContextHolder;
import com.example.visantanna.leilaoapp.Dao.ItemCard;
import com.google.gson.Gson;

/**
 * Created by vis_a on 06-Nov-17.
 */

public class ItemHolder extends RecyclerView.ViewHolder{

    private View view;
    private Activity activity;

    //Views do card
    TextView nomeProduto;
    TextView preco;
    ImageView fotoProduto;
    TextView dataFim;
    TextView local;

    //Dados Card
    ItemCard item;


    public ItemHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Gson gson = new Gson();
                Intent intent = new Intent(ContextHolder.getAplicationContext(), LeilaoActivity.class);
                intent.putExtra("item", gson.toJson(item));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        fotoProduto = (ImageView) itemView.findViewById(R.id.picture);
        preco = (TextView) itemView.findViewById(R.id.preco);
        nomeProduto = (TextView) itemView.findViewById(R.id.nameProduto);
        dataFim = (TextView) itemView.findViewById(R.id.date);
        local = (TextView) itemView.findViewById(R.id.localizacao);
    }
    public void bindItem(ItemCard item){
        this.item = item;
        setImage(item);
        preco.setText(item.getValorAtual());
        nomeProduto.setText(item.getNome());
        dataFim.setText(item.getDataFim());
        local.setText(item.getInstituicao().getEstado() + " - " + item.getInstituicao().getCidade() );
    }


    private void setImage(ItemCard item) {
        if(item.getFoto() != null){
            try{
                Bitmap imagemProduto = BitmapFactory.decodeByteArray(item.getFoto() , 0 , item.getFoto().length);
                fotoProduto.setImageBitmap(Bitmap.createScaledBitmap(imagemProduto, fotoProduto.getWidth(),
                        fotoProduto.getHeight(), false));
            }catch(Exception e){

            }
        }
    }
    public void setActivity(Activity activity){
        this.activity = activity;
    }

}
