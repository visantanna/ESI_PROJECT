package com.example.visantanna.leilaoapp.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.baseActivity;
import com.example.visantanna.leilaoapp.controllers.Validator;
import com.example.visantanna.leilaoapp.db_classes.Login;
import com.example.visantanna.leilaoapp.db_classes.Mensagem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RedefinirSenha  extends baseActivity {

    private AppCompatEditText codigo;
    private AppCompatEditText senha;
    private AppCompatEditText confirmaSenha;
    private TextView mensagem;
    private Login login = new Login();
    private Socket socket;
    private Mensagem mensagemRetorno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        mContext = getBaseContext();
        jbInit();
    }


    private void jbInit() {
        codigo = (AppCompatEditText) findViewById(R.id.codigoTextBoxRedefinirSenha);
        senha = (AppCompatEditText) findViewById(R.id.senhaRedefinirSenhaTextBox);
        confirmaSenha = (AppCompatEditText) findViewById(R.id.confirmarRedefinirSenhaTextBox);

        mensagem = (TextView)  findViewById(R.id.mensagemRedefinirSenha);
    }


    public void enviarCodigoClicked(View button){

        String strCodigo = codigo.getText().toString();
        String strSenha = senha.getText().toString();
        String strConfirmaSenha = confirmaSenha.getText().toString();


        boolean validacaoSenha = validaSenha(strSenha, strConfirmaSenha);
        if(validacaoSenha){
            mensagem.setText("");

            try {
                verificaConta(strCodigo,strSenha);
            }catch(Exception e){
                Log.e("Erro-enviaRequest","MENSAGEM ERRO:",e);
                System.out.println(e);
            }

        }

    }

    //NAO MUDEI NADA AQUI, SO COPIEI, TEM QUE MUDAR!
    private void verificaConta(String codigo,String senha) throws IOException {

        final Login mensagemSocket = new Login();

        mensagemSocket.setCabecalho("codigoRedefinirSenha");
        mensagemSocket.setEmail(codigo);
        mensagemSocket.setSenha(senha);

        Thread t = new Thread(new Runnable() {
            public void run() {

                try {

                    try {
                        String ipServidor = getResources().getString(R.string.ipServidor);
                        socket = new Socket();
                                socket.connect(new InetSocketAddress(ipServidor, 9898), 10000);

                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                        Log.e("Teste", "ERRO Socket", e1);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Log.e("Teste", "ERRO socket", e1);
                    }catch(Exception e) {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(), "Falha ao Conectar Com o Servidor", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (socket.isConnected()) {

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));


                        PrintWriter out = new PrintWriter(
                                socket.getOutputStream(), true);

                        Gson gson = new Gson();
                        String json = gson.toJson(mensagemSocket);

                        //enviando o json para o servidor
                        out.println(json);
                        out.write(json);
                        out.flush();


                        String jsonIn = in.readLine();

                        Log.v("Testejson", jsonIn);
                        mensagemRetorno = gson.fromJson(jsonIn, Mensagem.class);

                        switch (mensagemRetorno.getCabecalho()) {
                            case "erro":
                                mensagem.setText(mensagemRetorno.getMensagem());
                                break;
                            case "senhaAlterada":
                                mensagem.setText(mensagemRetorno.getMensagem());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), mensagemRetorno.getMensagem(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                TransitionRightExtraField(LoginActivity.class, "email", login.getEmail(), "senha", login.getSenha());
                                break;
                            default:
                                //dando um "print" do json no log
                                Log.w("default", "algo errado no switch do cabecalho");
                                break;
                        }
                    }//fim do ifsocket is connected
                    else{
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(), "Falha ao Conectar Com o Servidor", Toast.LENGTH_LONG).show();
                            }
                        });
                    }//fim do else socket is coneccted
                } catch (Exception e) {
                    Log.e("Erro-envioJson", "MENSAGEM ERRO:", e);
                } finally {
                    //Fechando o socket
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                            Log.v("DesligaSocket", "The client is shut down!");
                        } catch (IOException e) {
                            Log.e("FechaSocket", "Problema Socket", e);
                        }
                    }//fim do if socket.isConeccted
                }//fim do finally
            }
        });

        t.start();
        try {
            t.join();
        }
        catch (InterruptedException e){
            Log.e("Erro-join","MENSAGEM ERRO:",e);
            System.out.println(e);
        }

    }

    private boolean validaSenha(String senha1, String senha2) {
        boolean isOk = true;
        if((Validator.allTrim(senha1).isEmpty())|| (Validator.allTrim(senha2).isEmpty())){
            isOk = false;
            mensagem.setText("Senha não foi preenchida!");
        }
        if(isOk){
            if(!senha1.equals(senha2)){
                isOk = false;
                mensagem.setText("As senhas são diferentes!");
            }
        }
        if(isOk){
            if(senha1.length() < 8 ){
                isOk = false;
                mensagem.setText("A senha deve ter 8 ou mais caracteres!");
            }
        }

        return isOk;
    }

}
