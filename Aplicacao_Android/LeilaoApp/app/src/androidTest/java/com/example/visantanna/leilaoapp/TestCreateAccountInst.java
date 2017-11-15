package com.example.visantanna.leilaoapp;


import android.content.Context;
import android.content.Intent;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatEditText;

import android.widget.Spinner;
import android.widget.TextView;

import com.example.visantanna.leilaoapp.View.Login.CreateAccountInst;



import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.Semaphore;


import static junit.framework.Assert.assertEquals;
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
    public void cepNaoPreenchido(){
        String campo = "cep";
        String campoAlerta = "alertaCep";
        String resultadoEsperado = "Cep não foi preenchido!";

        assertEquals(pegarMensagemDeErro(campo, campoAlerta) , resultadoEsperado);

    }
    @Test
    public void bairroNaoPreenchido(){
        String campo = "bairro";
        String campoAlerta = "alertaBairro";
        String resultadoEsperado = "Bairro não foi preenchido!";

        assertEquals(pegarMensagemDeErro(campo, campoAlerta) , resultadoEsperado);
    }
    @Test
    public void ruaNaoPreenchido(){
        String campo = "rua";
        String campoAlerta = "alertaRua";
        String resultadoEsperado = "Rua não foi preenchida!";

        assertEquals(pegarMensagemDeErro(campo, campoAlerta) , resultadoEsperado);
    }
    @Test
    public void numeroNaoPreenchido(){
        String campo = "numero";
        String campoAlerta = "alertaNumero";
        String resultadoEsperado = "Numero não foi preenchido!";

        assertEquals(pegarMensagemDeErro(campo, campoAlerta) , resultadoEsperado);
    }
    @Test
    public void telefoneNaoPreenchido(){
        String campo = "telefone";
        String campoAlerta = "alertaTelefone";
        String resultadoEsperado = "Telefone não foi preenchido!";

        assertEquals(pegarMensagemDeErro(campo, campoAlerta) , resultadoEsperado);
    }
    @Test
    public void cnpjNaoPreenchido(){
        String campo = "cnpj";
        String campoAlerta = "alertaCnpj";
        String resultadoEsperado = "Cnpj não foi preenchido!";

        assertEquals(pegarMensagemDeErro(campo, campoAlerta) , resultadoEsperado);
    }
    @Test
    public void cidadeNaoPreenchido(){
        String campo = "cidade";
        String campoAlerta = "alertaCidade";
        String resultadoEsperado = "Cidade não foi preenchida!";

        assertEquals(pegarMensagemDeErro(campo, campoAlerta) , resultadoEsperado);
    }


    private String pegarMensagemDeErro(String campo, String campoAlerta) {
        try {

            sobrescreveCampo(campo, "");
            final Semaphore mutex = new Semaphore(0);
            Tela.runOnUiThread( new Thread(){
                @Override
                public void run() {
                    Tela.validaDados();
                    mutex.release();
                }
            });
            mutex.acquire();
            return getMensagem(campoAlerta);

        }catch(NoSuchFieldException | IllegalAccessException e1){
            return "erro";
        }catch(Exception e) {
            return "erro";
        }
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
