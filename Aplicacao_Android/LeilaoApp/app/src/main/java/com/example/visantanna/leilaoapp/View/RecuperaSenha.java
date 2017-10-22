package com.example.visantanna.leilaoapp.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.TextView;
import android.view.View;
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

/**
 * Created by daniel on 07/10/2017.
 */

public class RecuperaSenha extends baseActivity {

    private AppCompatEditText email;
    private AppCompatEditText senha;
    private TextView mensagem;
    private Login login = new Login();
    private Socket socket;
    private Mensagem mensagemRetorno;

    @Override
    protected void onCreate(Bundle BundleSavedStance){
        super.onCreate(BundleSavedStance);
        setContentView(R.layout.recupera_senha);
        mContext = getBaseContext();
        jbInit();
    }

    private void jbInit() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        email = (AppCompatEditText) findViewById(R.id.emailTextBoxRecuperarSenha);

        mensagem = (TextView)  findViewById(R.id.mensagemRecuperaSenha);
    }


    public void enviarCodigoClicked(View button){


        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(),"Processo em andamento. Aguarde!", Toast.LENGTH_SHORT).show();
        String strEmail = email.getText().toString();

        boolean emailValido = Validator.validaEmail(strEmail);

        if(emailValido){
            mensagem.setText("processo em andamento");

            try {
                verificaConta(strEmail );
            }catch(Exception e){
                Log.e("Erro-enviaRequest","MENSAGEM ERRO:",e);
                System.out.println(e);
            }

        }else{
            mensagem.setText("Email invalido!");
        }

    }

    //NAO MUDEI NADA AQUI, SO COPIEI, TEM QUE MUDAR!
    private void verificaConta(String email) throws IOException {

        final Mensagem mensagemSocket = new Mensagem();


        mensagemSocket.setCabecalho("codigoRecuperarSenha");
        mensagemSocket.setMensagem(email);

        Thread t = new Thread(new Runnable() {
            public void run() {

                try {

                    try {
                        String ipServidor = getResources().getString(R.string.ipServidor);
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(ipServidor, 9898), 15000);

                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                        Log.e("Teste","ERRO Socket",e1);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Log.e("Teste","ERRO socket",e1);
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

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            }
                        });

                    Log.v("Testejson",jsonIn);


                    mensagemRetorno = gson.fromJson(jsonIn, Mensagem.class);

                    switch (mensagemRetorno.getCabecalho()){
                        case "erro":
                            mensagem.setText(mensagemRetorno.getMensagem());
                            break;
                        case "codigoEnviado":
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(getApplicationContext(),mensagemRetorno.getMensagem(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            TransitionRightExtraId(RedefinirSenha.class, "email", mensagemSocket.getMensagem());
                            break;
                        default:
                            //dando um "print" do json no log
                            Log.w("default","algo errado no switch do cabecalho");
                            break;
                    }
                     }//fim do ifsocket is connected
                    else{
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Falha ao Conectar Com o Servidor", Toast.LENGTH_LONG).show();
                        }
                    });
                }//fim do else socket is coneccted
                } catch (Exception e) {
                    Log.e("Erro-envioJson","MENSAGEM ERRO:",e);
                }finally {
                    //Fechando o socket
                    if(socket.isConnected()) {
                        try {
                            socket.close();
                            Log.v("DesligaSocket","The client is shut down!");
                        } catch (IOException e) {
                            Log.e("FechaSocket","Problema Socket",e);
                        }
                    }//fim do if socket.isConeccted
                }//fim do finally
            }
        });

        t.start();
        try {
            //t.join();
           // findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
        catch (Exception e){
            Log.e("Erro-join","MENSAGEM ERRO:",e);
            System.out.println(e);
        }

    }


    public void possuoCodigo(View v){
        String strEmail = email.getText().toString();
        String strSenha = "nada";
        TransitionRightExtraField(RedefinirSenha.class ,"email",strEmail ,"senha", strSenha );
    }

}
