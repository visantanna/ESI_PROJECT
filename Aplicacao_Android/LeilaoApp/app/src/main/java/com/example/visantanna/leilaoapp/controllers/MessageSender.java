package com.example.visantanna.leilaoapp.controllers;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.ContextHolder;
import com.example.visantanna.leilaoapp.db_classes.Mensagem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by vis_a on 08-Nov-17.
 */

public class MessageSender extends Thread {
    private Socket socket;
    private Mensagem mensagem = new Mensagem();
    private Activity activity;
    private View loadingView;
    private String status = "falha";
    private String retorno = "";
    private Receiver rec;

    public MessageSender(String cabecalho, String mensagem, Activity currentActivity, View loadingView , Receiver rec ) {
        this.mensagem.setCabecalho(cabecalho);
        this.mensagem.setMensagem(mensagem);
        this.loadingView = loadingView;
        activity = currentActivity;
        this.rec = rec;
    }


    public void run() {
        setLoadingVisible(true);

        try {
            try {
                String ipServidor = ContextHolder.getAplicationContext().getResources().getString(R.string.ipServidor);
                socket = new Socket();
                socket.connect(new InetSocketAddress(ipServidor, 9898), 10000);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
                Log.e("Teste", "ERRO Socket", e1);
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e("Teste", "ERRO socket", e1);
            } catch (Exception e) {
                if(activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), "Falha ao Conectar Com o Servidor", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            if (socket.isConnected()) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);

                Gson gson = new Gson();
                String json = gson.toJson(mensagem);

                //enviando o json para o servidor
                out.println(json);
                out.write(json);
                out.flush();

                //dando um "print" do json no log
                Log.w("JSON!!!", json);


                String jsonIn = in.readLine();
                final Mensagem mensagemRetorno = gson.fromJson(jsonIn, Mensagem.class);
                setLoadingVisible(false);

                Log.w("RETORNOACCOUNT", mensagemRetorno.getMensagem());

                switch (mensagemRetorno.getCabecalho()) {
                    case "erro":
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity.getApplicationContext(), mensagemRetorno.getMensagem(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        status = "falha";
                        break;
                    case "sucesso":
                        if (activity != null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity.getApplicationContext(), mensagemRetorno.getMensagem(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        status = "sucesso";
                        retorno = mensagemRetorno.getMensagem();
                        break;
                    default:
                        //dando um "print" do json no log
                        Log.w("default", "algo errado no switch do cabecalho");
                        status = "falha";
                        break;

                }
            }//fim do ifsocket is connected
            else {
                setLoadingVisible(false);
                if(activity != null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), "Falha ao Conectar Com o Servidor", Toast.LENGTH_LONG).show();
                        }
                    });
            }//fim do else socket is coneccted

        } catch (Exception e) {
            Log.e("Erro-envioJson", "MENSAGEM ERRO:", e);
        } finally {
            setLoadingVisible(false);
            //Fechando o socket
            if (socket.isConnected()) {
                try {
                    socket.close();
                    Log.v("DesligaSocket", "The client is shut down!");
                } catch (IOException e) {
                    Log.e("FechaSocket", "Problema Socket", e);
                }
            }//fim do if socket.isConeccted
            rec.receive(this);
        }//fim do finally
    }

    private void setLoadingVisible(final boolean visible) {
        if(loadingView!= null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (visible) {
                        loadingView.setVisibility(View.VISIBLE);
                    } else {
                        loadingView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

}


