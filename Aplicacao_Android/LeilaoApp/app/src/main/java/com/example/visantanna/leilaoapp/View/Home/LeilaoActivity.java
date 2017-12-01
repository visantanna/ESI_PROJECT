package com.example.visantanna.leilaoapp.View.Home;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.CurrencyTextWatcher;
import com.example.visantanna.leilaoapp.base.baseActivity;
import com.example.visantanna.leilaoapp.controllers.Formatter;
import com.example.visantanna.leilaoapp.controllers.Validator;
import com.example.visantanna.leilaoapp.db_classes.Instituicao;
import com.example.visantanna.leilaoapp.Dao.ItemCard;
import com.google.gson.Gson;

import java.math.BigDecimal;


/**
 * Created by vis_a on 13-Nov-17.
 */

public class LeilaoActivity extends baseActivity {
    private ItemCard itemBase;
    private TextView descricao;
    private TextView valorAtual;
    private TextView nomeInstituicao;
    private TextView telefone;
    private TextView email;
    private TextView localizacao;
    private TextView lance_minimo;
    private TextView dataInicio;
    private TextView dataFim;
    private ImageView selo;
    private Button botaoLance;
    private EditText lance;

    public LeilaoActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leilao_view);
        Gson gson = new Gson();
        if(itemBase == null) {
            itemBase = gson.fromJson(getIntent().getStringExtra("item"), ItemCard.class);
        }
        jbInit();
    }

    private void jbInit() {
        ImageView imagemItem = (ImageView)findViewById(R.id.leilao_view_item_image);
        if(itemBase.getFoto() != null){
            try{
                Bitmap imagemProduto = BitmapFactory.decodeByteArray(itemBase.getFoto() , 0 , itemBase.getFoto().length);
                imagemItem.setImageBitmap(imagemProduto);
            }catch(Exception e){

            }
        }

        setMascaraInMeuLance();

        Instituicao inst = itemBase.getInstituicao();
        valorAtual = (TextView)findViewById(R.id.leilao_view_valorAtual);
        valorAtual.setText(itemBase.getValorAtual());
        descricao = (TextView)findViewById(R.id.leilao_view_descricao);
        descricao.setText(itemBase.getDescricao());
        nomeInstituicao = (TextView)findViewById(R.id.leilao_view_instituicao);
        nomeInstituicao.setText(inst.getNome());
        telefone = (TextView)findViewById(R.id.leilao_view_telefone);
        telefone.setText(Formatter.formatPhone(inst.getTelefone()));
        email = (TextView)findViewById(R.id.leilao_view_email);
        email.setText(inst.getEmail());
        localizacao = (TextView)findViewById(R.id.leilao_view_localizacao);
        localizacao.setText(inst.getEstado() + " - " + inst.getCidade());
        lance_minimo = (TextView)findViewById(R.id.leilao_view_minimo);
        lance_minimo.setText( calculaLanceMinimo());
        dataInicio = (TextView)findViewById(R.id.leilao_view_de);
        dataInicio.setText("De: "+itemBase.getDataInicio());
        dataFim = (TextView)findViewById(R.id.leilao_view_ate);
        dataFim.setText("Até: " +itemBase.getDataFim());
        selo = (ImageView) findViewById(R.id.leilao_view_selo_imagem);
        botaoLance = (Button) findViewById(R.id.leilao_view_buttonLance);
        lance = (EditText)findViewById(R.id.leilao_view_meu_lance);
    }

    private void setMascaraInMeuLance() {
        EditText meuLance = (EditText)findViewById(R.id.leilao_view_meu_lance);
        CurrencyTextWatcher mascara = new CurrencyTextWatcher(meuLance);
    }


    private String calculaLanceMinimo() {
        if(itemBase.getLance_atual() != null){
            BigDecimal mult = new BigDecimal(1.05);
            BigDecimal apostaMinima = itemBase.getLance_atual().multiply(mult).setScale(2,BigDecimal.ROUND_CEILING);
            return "min: R$ "+ apostaMinima.toString().replace('.',',');
        }else{
            return itemBase.getFormattedLance_minimo();
        }
    }

    private BigDecimal calculaLanceMinimoBigDecimal() {
        if(itemBase.getLance_atual() != null){
            BigDecimal mult = new BigDecimal(1.05);
            BigDecimal apostaMinima = itemBase.getLance_atual().multiply(mult).setScale(2,BigDecimal.ROUND_CEILING);
            return apostaMinima;
        }else{
            return itemBase.getLance_minimo();
        }
    }


    public void darLanceClicked(View v){
        EditText lance = (EditText)findViewById(R.id.leilao_view_meu_lance);
        BigDecimal lanceNovo = new BigDecimal(lance.getText().toString());
        switch (botaoLance.getText().toString().toUpperCase()) {
            case "DAR LANCE":
                darLanceClicado(lance, lanceNovo);
                break;
            case "NOVO LANCE":
                novoLanceClicado();
        }
    }

    private void novoLanceClicado() {
        botaoLance.setText("DAR LANCE");
        lance.setEnabled(true);
    }

    private void darLanceClicado(EditText lance, BigDecimal lanceNovo) {
        if(lanceNovo.compareTo(this.calculaLanceMinimoBigDecimal()) >= 0){
            selo.setVisibility(View.VISIBLE);
            botaoLance.setText("NOVO LANCE");
            lance.setEnabled(false);

            itemBase.setLance_atual(lanceNovo);
            TextView valorAtual = (TextView)findViewById(R.id.leilao_view_valorAtual);
            valorAtual.setText(itemBase.getValorAtual());
            TextView lance_minimo = (TextView)findViewById(R.id.leilao_view_minimo);
            lance_minimo.setText(calculaLanceMinimo());
        }else{
            Toast mensagemLanceBaixo = Toast.makeText(getBaseContext() , "Lance mínimo é de "+itemBase.getFormattedLance_minimo() , Toast.LENGTH_SHORT);
            mensagemLanceBaixo.show();
            lance_minimo.requestFocus();
        }
    }
}
