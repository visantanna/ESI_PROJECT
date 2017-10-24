package com.example.visantanna.leilaoapp;


import android.content.Context;
import android.content.Intent;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatEditText;

import android.widget.Spinner;
import android.widget.TextView;

import com.example.visantanna.leilaoapp.View.CreateAccountInst;



import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


import static junit.framework.Assert.assertTrue;

/**
 * Created by vis_a on 23-Oct-17.
 */
public class TestCreateAccountInst  {
    @Rule
    public ActivityTestRule<CreateAccountInst> activityRule  = new ActivityTestRule<>(CreateAccountInst.class , true ,false);
    public CreateAccountInst Tela;

    @Before
    public void PreparaCamposParaTeste(){
        try {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent( context ,CreateAccountInst.class);
            intent.putExtra("email", "teste@teste.com.br");
            intent.putExtra("senha", "senha123");
            Tela = activityRule.launchActivity(intent);

            sobrescreveCampo("email" , "teste@teste.com");
            sobrescreveCampo("senha" , "senha123");
            sobrescreveCampo("repeteSenha" , "senha123");
            sobrescreveCampo("nomeCompleto" , "Alcides");
            sobrescreveCampo("cep" , "04643213");
            sobrescreveCampo("complemento" , "apto 72 bloco F");
            sobrescreveCampo("bairro" , "Vila Mariana");
            sobrescreveCampo("rua" , "Rua Marechal Caetano Mendes");
            sobrescreveCampo("numero" , "1032");
            sobrescreveCampo("telefone" , "927742103");
            sobrescreveCampo("cnpj" , "3392103210");
            sobrescreveCampo("cidade","São Paulo");

            Field campoS  = Tela.getClass().getDeclaredField("s");
            campoS.setAccessible(true);
            final Spinner s = (Spinner)campoS.get(Tela);
            Tela.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    s.setSelection(1);
                }
            });
        }catch (NoSuchFieldException e){
            assertTrue(false);
        }catch (IllegalAccessException e1){
            assertTrue(false);
        }catch (Exception e){
            System.out.println(e + "");
        }
    }
    @Test
    public void testaValidaDadosOk(){
        Tela.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertTrue(Tela.validaDados());
            }
        });
    }
    @Test
    public void camposNaoPreenchidos(){
        String[] campos = new String [] { "cep" ,"bairro","rua" ,"numero",
                "telefone", "cnpj" , "cidade"    };
        String[] camposAlerta = new String[]{"alertaCep", "alertaBairro","alertaRua","alertaNumero"
                ,"alertaTelefone","alertaCnpj","alertaCidade"};

        ArrayList<String> listaResultadosEsperados = new ArrayList<String>();
        ArrayList<String> listaResultados = new ArrayList<String>();



        listaResultadosEsperados.add("Cep não foi preenchido!");
        listaResultadosEsperados.add("Bairro não foi preenchido!");
        listaResultadosEsperados.add("Rua não foi preenchida!");
        listaResultadosEsperados.add("Numero não foi preenchido!");
        listaResultadosEsperados.add("Telefone não foi preenchido!");
        listaResultadosEsperados.add("Cnpj não foi preenchido!");
        listaResultadosEsperados.add("Cidade não foi preenchida!");

        try {
            for(int i = 0 ; i < campos.length ; i ++)
            {
                sobrescreveCampo(campos[i], "");
                final Semaphore mutex = new Semaphore(0);
                Tela.runOnUiThread( new Thread(){
                    @Override
                    public void run() {
                        Tela.validaDados();
                        mutex.release();
                    }
                });
                mutex.acquire();
                listaResultados.add(getMensagem(camposAlerta[i]));
            }
        }catch(NoSuchFieldException e1){
            assertTrue(false);
        }catch(IllegalAccessException e2){
            assertTrue(false);
        }catch(Exception e) {
            assertTrue(false);
        }
        assertTrue(listaResultados.equals(listaResultadosEsperados));
    }
    public void sobrescreveCampo(String campo , final String novoValor) throws NoSuchFieldException, IllegalAccessException {
        Field campoView = Tela.getClass().getDeclaredField(campo);
        campoView.setAccessible(true);
        final AppCompatEditText editTextEmail = (AppCompatEditText)campoView.get(Tela);
        Tela.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editTextEmail.setText(novoValor);
            }
        });
    }
    public String getMensagem(String campo) throws NoSuchFieldException, IllegalAccessException{
        Field campoView = Tela.getClass().getDeclaredField(campo);
        campoView.setAccessible(true);
        TextView editTextEmail = (TextView)campoView.get(Tela);
        return editTextEmail.getText().toString();
    }
}
