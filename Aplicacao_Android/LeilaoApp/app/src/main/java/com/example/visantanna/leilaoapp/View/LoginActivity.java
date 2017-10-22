package com.example.visantanna.leilaoapp.View;
import android.content.Intent;
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

public class LoginActivity extends baseActivity {
    private AppCompatEditText email;
    private AppCompatEditText senha;
    private TextView mensagem;
    private Login login = new Login();
    private Socket socket;
    private Mensagem mensagemRetorno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();
        jbInit();
    }

    private void jbInit() {
        email = (AppCompatEditText) findViewById(R.id.emailTextBox);
        senha = (AppCompatEditText) findViewById(R.id.senhaTextBox);
        mensagem = (TextView)  findViewById(R.id.mensagem);
    }

    public void logInClicked(View button){
        String strEmail = email.getText().toString();
        String strSenha = senha.getText().toString();

        boolean emailValido = Validator.validaEmail(strEmail);
        boolean senhaValida = Validator.validaSenha(strSenha);

        if(emailValido&&senhaValida){
            mensagem.setText("");

            try {
                verificaConta(strEmail ,strSenha );
            }catch(Exception e){
                Log.e("Erro-enviaRequest","MENSAGEM ERRO:",e);
                System.out.println(e);
            }

        }else{
            mensagem.setText("Dados invalidos!");
        }
    }

    private void verificaConta(String email , String senha) throws IOException {

            //usado para decidir como tratar a mensagem no servidor
            login.setCabecalho("login");
            //coloca as informacoes do login validadas
            login.setSenha(senha);
            login.setEmail(email);

            Thread t = new Thread(new Runnable() {
                public void run() {

                    try {

                        try {
                            String ipServidor = getResources().getString(R.string.ipServidor);
                            socket = new Socket();
                            socket.connect(new InetSocketAddress(ipServidor, 9898), 10000);

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
                        String json = gson.toJson(login);

                        //enviando o json para o servidor
                        out.println(json);
                        out.write(json);
                        out.flush();


                        String jsonIn = in.readLine();

                        Log.v("Testejson",jsonIn);
                        mensagemRetorno = gson.fromJson(jsonIn, Mensagem.class);

                        switch (mensagemRetorno.getCabecalho()){
                            case "erro":
                                mensagem.setText(mensagemRetorno.getMensagem());
                                break;
                            case "instituicaoAutenticada":
                                mensagem.setText(mensagemRetorno.getMensagem());
                                TransitionRightExtraId(HomeActivity.class, "email", login.getEmail());
                                break;
                            case "usuarioAutenticado":
                                mensagem.setText(mensagemRetorno.getMensagem());
                                TransitionRightExtraId(HomeActivity.class, "email", login.getEmail());
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
            t.join();
        }
        catch (InterruptedException e){
            Log.e("Erro-join","MENSAGEM ERRO:",e);
            System.out.println(e);
        }

    }
    public void createAccountInstituicao(View v){
        String strEmail = email.getText().toString();
        String strSenha = senha.getText().toString();
        TransitionRightExtraField(CreateAccountInst.class ,"email",strEmail ,"senha", strSenha );
    }

    public void createAccount(View v){
        String strEmail = email.getText().toString();
        String strSenha = senha.getText().toString();
        TransitionRightExtraField(CreateAccount.class ,"email",strEmail ,"senha", strSenha );
    }

    public void esqueciSenha(View v){
        String strEmail = email.getText().toString();
        String strSenha = senha.getText().toString();
        TransitionRightExtraField(RecuperaSenha.class ,"email",strEmail ,"senha", strSenha );
    }
}
