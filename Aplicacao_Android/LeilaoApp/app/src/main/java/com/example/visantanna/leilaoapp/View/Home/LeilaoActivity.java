package com.example.visantanna.leilaoapp.View.Home;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visantanna.leilaoapp.Dao.InteresseDAO;
import com.example.visantanna.leilaoapp.Dao.lanceDAO;
import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.ContextHolder;
import com.example.visantanna.leilaoapp.base.CurrencyTextWatcher;
import com.example.visantanna.leilaoapp.base.baseActivity;
import com.example.visantanna.leilaoapp.controllers.Formatter;
import com.example.visantanna.leilaoapp.controllers.RetornoLance;
import com.example.visantanna.leilaoapp.db_classes.Instituicao;
import com.example.visantanna.leilaoapp.Dao.ItemCard;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by vis_a on 13-Nov-17.
 */

public class LeilaoActivity extends baseActivity implements Observer{
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
    private ProgressBar progressBar;
    private ImageView starImage;
    lanceDAO lanceClass;
    InteresseDAO interesseDAO;

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
        progressBar = (ProgressBar)findViewById(R.id.loadingPanellance);
        lanceClass = new lanceDAO(this , progressBar);
        interesseDAO = new InteresseDAO(this, null);
        starImage = (ImageView)findViewById(R.id.leilao_view_item_star);
        changeStarImage(itemBase.isInteresse());
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
        if(lanceNovo.compareTo(this.calculaLanceMinimoBigDecimal()) >= 0) {
            lanceClass.darLance(itemBase, ContextHolder.getId_user());
        }else{
            Toast mensagemLanceBaixo = Toast.makeText(getBaseContext() , "Lance mínimo é de "+itemBase.getFormattedLance_minimo() , Toast.LENGTH_SHORT);
            mensagemLanceBaixo.show();
            lance_minimo.requestFocus();
        }

    }

    private void atualizaLance(boolean sucessoLance , BigDecimal lanceNovo) {
        itemBase.setLance_atual(lanceNovo);
        valorAtual.setText(itemBase.getValorAtual());
        lance_minimo.setText(calculaLanceMinimo());

        if(sucessoLance) {
            selo.setVisibility(View.VISIBLE);
            botaoLance.setText("NOVO LANCE");
            lance.setEnabled(false);
            Toast mensagemLanceSucesso = Toast.makeText(getBaseContext() , "Seu lance é o mais alto!" , Toast.LENGTH_LONG);
        }else{
            Toast mensagemLanceDesatualizado = Toast.makeText(getBaseContext() , "Ixi, alguem deu um lance enquanto você não estava olhando!",Toast.LENGTH_LONG);
            mensagemLanceDesatualizado.show();
            lance_minimo.requestFocus();
        }
    }


    @Override
    public void update(Observable observable, Object o) {
        if(o instanceof lanceDAO){
            RetornoLance retorno = ((lanceDAO) o).getRetornoLance();
            atualizaLance(retorno.isSucessoLance() , retorno.getLanceAtual());
        }
        if(o instanceof InteresseDAO){
            boolean interesse = ((InteresseDAO)o).isInteresse();
            itemBase.setInteresse(interesse);
            changeStarImage(interesse);
        }
    }
    private void changeStarImage(boolean interesse){
        if(interesse){
            starImage.setImageResource(R.drawable.starfull);
        }else{
            starImage.setImageResource(R.drawable.starempty);
        }
    }
    public void starClicked(View v){
        itemBase.setInteresse(!itemBase.isInteresse());
        changeStarImage(itemBase.isInteresse());
        interesseDAO.atualizaInteresse(itemBase.isInteresse() , itemBase.getCod_item() , ContextHolder.getId_user());
    }
}
