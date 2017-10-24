package com.example.visantanna.leilaoapp;

import com.example.visantanna.leilaoapp.controllers.MensagemRetorno;
import com.example.visantanna.leilaoapp.controllers.Validator;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by vis_a on 23-Oct-17.
 */

public class testaValidacao {
    @Test
    public void testaSenhaIgualMenor8(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false , "A senha deve ter 8 ou mais caracteres!");
        String senha1 = "senhat";
        String senha2 = "senhat";

        assertEquals(Validator.validaSenhaCadastro(senha1,senha2) , mensagemEsperada);
    }
    @Test
    public void testaSenhaDiferente(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "As senhas são diferentes!");
        String senha1 = "DarkHollow";
        String senha2 = "HollowDark";

        assertEquals(Validator.validaSenhaCadastro(senha1,senha2) , mensagemEsperada);
    }
    @Test
    public void testaSenha1Vazia(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "Senha não foi preenchida!");
        String senha1 = "";
        String senha2 = "HollowDark";

        assertEquals(Validator.validaSenhaCadastro(senha1,senha2) , mensagemEsperada);
    }
    @Test
    public void testaSenha2Vazia(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "Senha não foi preenchida!");
        String senha1 = "DarkHollow";
        String senha2 = "";

        assertEquals(Validator.validaSenhaCadastro(senha1,senha2) , mensagemEsperada);
    }
    @Test
    public void testaSenhaMaiorIgual8(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(true, "");
        String senha1 = "DarkHollo";
        String senha2 = "DarkHollo";

        assertEquals(Validator.validaSenhaCadastro(senha1,senha2) , mensagemEsperada);
    }
    @Test
    public void testaSenhaMaiorMaior8(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(true, "");
        String senha1 = "DarkHollow";
        String senha2 = "DarkHollow";

        assertEquals(Validator.validaSenhaCadastro(senha1,senha2) , mensagemEsperada);
    }
    @Test
    public void testaNomePreenchido(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(true, "");
        String nome = "DarkHollow";

        assertEquals(Validator.validaNomeCadastro(nome) , mensagemEsperada);
    }
    @Test
    public void testaNomeVazio(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "O campo Nome está vazio!");
        String nome = "";

        assertEquals(Validator.validaNomeCadastro(nome) , mensagemEsperada);
    }
    @Test
    public void testaEmailVazio(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "O campo de E-mail está vazio!");
        String email = "";

        assertEquals(Validator.validaEmailCadastro(email) , mensagemEsperada);
    }
    @Test
    public void testaEmailPreenchido(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(true, "");
        String email = "teste@usp.br";

        assertEquals(Validator.validaNomeCadastro(email) , mensagemEsperada);
    }
    @Test
    public void testaEmailPreenchido2(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(true, "");
        String email = "teste@gmail.com";

        assertEquals(Validator.validaEmailCadastro(email) , mensagemEsperada);
    }
    @Test
    public void testaEmailSemPontoNofinal(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "E-mail inválido!");
        String email = "teste@gmail";

        assertEquals(Validator.validaEmailCadastro(email) , mensagemEsperada);
    }
    @Test
    public void testaEmailSemArroba(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "E-mail inválido!");
        String email = "testegmail.com";

        assertEquals(Validator.validaEmailCadastro(email) , mensagemEsperada);
    }
    @Test
    public void testaEmailSemNadaDepoisDoArroba(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "E-mail inválido!");
        String email = "teste@";

        assertEquals(Validator.validaEmailCadastro(email) , mensagemEsperada);
    }
    @Test
    public void testaEmailSemNadaAntesDoArroba(){
        MensagemRetorno mensagemEsperada = new MensagemRetorno(false, "E-mail inválido!");
        String email = "@gmail.com";

        assertEquals(Validator.validaEmailCadastro(email) , mensagemEsperada);
    }
    @Test
    public void testeAllTrimEsquerda(){
        String espacoEsquerda = "  Tempo";

        assertEquals(Validator.allTrim(espacoEsquerda), "Tempo");
    }
    @Test
    public void testeAllTrimDireita(){
        String espacoDireito = "  Tempo  ";

        assertEquals(Validator.allTrim(espacoDireito), "Tempo");
    }
    @Test
    public void testeAllTrimMeio(){
        String espacoMeio = "  Tem po  ";

        assertEquals(Validator.allTrim(espacoMeio), "Tempo");
    }
    @Test
    public void testeAlltrimNull(){
        String espacoMeio = null;

        assertEquals(Validator.allTrim(espacoMeio), "");
    }
}
