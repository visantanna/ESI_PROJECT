package com.example.visantanna.leilaoapp.View;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.baseActivity;
import com.example.visantanna.leilaoapp.controllers.Validator;
import com.example.visantanna.leilaoapp.db_classes.Instituicao;
import com.example.visantanna.leilaoapp.db_classes.Mensagem;
import com.example.visantanna.leilaoapp.db_classes.Usuario;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import butterknife.BindView;


public class CreateAccountInst extends baseActivity {

    private AppCompatEditText email;
    private AppCompatEditText senha;
    private AppCompatEditText repeteSenha;
    private AppCompatEditText nomeCompleto;
    private AppCompatEditText cep;
    private AppCompatEditText complemento;
    private AppCompatEditText bairro;
    private AppCompatEditText rua;
    private AppCompatEditText numero;
    private AppCompatEditText telefone;
    private AppCompatEditText cnpj;
    private AppCompatEditText cidade;



    private TextView alertaEmail;
    private TextView alertaSenha;
    private TextView alertaNome;
    private TextView alertaCep;
    private TextView alertaCidade;
    private TextView alertaCnpj;
    private TextView alertaBairro;
    private TextView alertaRua;
    private TextView alertaNumero;
    private TextView alertaComplemento;
    private TextView alertaTelefone;
    private TextView alertaEstado;

    private String[] arraySpinner;
    private Spinner s;

    private Instituicao usuario = new Instituicao();
    private Socket socket;

    @Override
    protected void onCreate(Bundle BundleSavedStance){
        super.onCreate(BundleSavedStance);
        setContentView(R.layout.new_account_inst);
        mContext = getBaseContext();
        this.arraySpinner = new String[] {
                "Selecione um Estado", "AC", "AL", "AP", "AM", "BA","CE","DF","ES","GO","MA","MT","MS","MG",
                "PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"
        };

         jbInit();
    }

    private void jbInit() {

        email = (AppCompatEditText)findViewById(R.id.emailTextBoxNewAccount1);
        senha = (AppCompatEditText)findViewById(R.id.SenhaTextBoxNewAccount1);
        repeteSenha = (AppCompatEditText)findViewById(R.id.RepeteSenhaTextBoxNewAccount1);
        nomeCompleto =(AppCompatEditText)findViewById(R.id.NomeCompletoTextBoxNewAccount1);
        cep = (AppCompatEditText)findViewById(R.id.CepTextBoxNewAccount1);
        complemento = (AppCompatEditText)findViewById(R.id.ComplementoTextBoxNewAccount1);
        bairro = (AppCompatEditText)findViewById(R.id.BairroTextBoxNewAccount1);
        rua = (AppCompatEditText)findViewById(R.id.RuaTextBoxNewAccount1);
        numero = (AppCompatEditText)findViewById(R.id.NumeroTextBoxNewAccount1);
        telefone = (AppCompatEditText)findViewById(R.id.TelefoneTextBoxNewAccount1);
        cnpj = (AppCompatEditText)findViewById(R.id.cnpjTextBoxNewAccount1);
        cidade = (AppCompatEditText)findViewById(R.id.CidadeTextBoxNewAccount1);
        
        
        alertaEmail = (TextView) findViewById(R.id.AlertaEmail1);
        alertaSenha = (TextView) findViewById(R.id.AlertaSenha1);
        alertaNome = (TextView) findViewById(R.id.AlertaNomeCompleto1);
        alertaCep = (TextView) findViewById(R.id.AlertaCep1);
        alertaCidade = (TextView) findViewById(R.id.AlertaCidade1);
        alertaCnpj = (TextView) findViewById(R.id.AlertaCNPJ);
        alertaBairro = (TextView) findViewById(R.id.AlertaBairro1);
        alertaRua = (TextView) findViewById(R.id.AlertaRua1);
        alertaNumero = (TextView) findViewById(R.id.AlertaNumero1);
        alertaComplemento = (TextView) findViewById(R.id.AlertaComplemento1);
        alertaTelefone = (TextView) findViewById(R.id.AlertaTelefone1);
        alertaEstado = (TextView) findViewById(R.id.AlertaEstado);

        s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);


        email.setText(getIntent().getExtras().getString("email"));
        senha.setText(getIntent().getExtras().getString("senha"));
    }
    public void createNewAccount(View v){

        
        boolean isEmailValido = validaEmail(email.getText().toString());
        boolean isSenhaValida = validaSenha(senha.getText().toString() , repeteSenha.getText().toString());
        boolean isNomeValido  = validaNome(nomeCompleto.getText().toString() );
        boolean isDadosValidos  = validaDados();

        if (isDadosValidos){
            alertaCep.setText("");
            usuario.setCEP(cep.getText().toString());
            alertaCnpj.setText("");
            usuario.setCnpj(cnpj.getText().toString());
            alertaCidade.setText("");
            usuario.setCidade(cidade.getText().toString());
            alertaRua.setText("");
            usuario.setRua(rua.getText().toString());
            alertaBairro.setText("");
            usuario.setBairro(bairro.getText().toString());
            alertaNumero.setText("");
            usuario.setNumero(Integer.valueOf(numero.getText().toString()));
            alertaTelefone.setText("");
            usuario.setTelefone(telefone.getText().toString());
            alertaComplemento.setText("");
            usuario.setComplemento(complemento.getText().toString());
            alertaEstado.setText("");
            usuario.setEstado(s.getSelectedItem().toString());

        }
                
        if(isEmailValido && isSenhaValida && isNomeValido && isDadosValidos ){
            try {
                enviaRequestNewAccount();
            }catch(Exception e){
                Log.e("Erro-enviaRequest","MENSAGEM ERRO:",e);
                System.out.println(e);
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Problemas de Validação", Toast.LENGTH_SHORT).show();
    }

    private void enviaRequestNewAccount() throws IOException {

        //usado para decidir como tratar a mensagem no servidor
        usuario.setCabecalho("nova_instituicao");


        new Thread(new Runnable() {
            public void run() {

                try {

                    try {
                        String ipServidor = getResources().getString(R.string.ipServidor);
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(ipServidor, 9898), 10000);
                    } catch (UnknownHostException e1) {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(), "Falha ao Conectar Com o Servidor", Toast.LENGTH_LONG).show();
                            }
                        });
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
                    String json = gson.toJson(usuario);

                    //enviando o json para o servidor
                    out.println(json);
                    out.write(json);
                    out.flush();

                    //dando um "print" do json no log
                    Log.w("JSON!!!",json);


                    String jsonIn = in.readLine();
                    final Mensagem mensagemRetorno = gson.fromJson(jsonIn, Mensagem.class);

                    Log.w("RETORNOACCOUNTINST",mensagemRetorno.getMensagem());

                    switch (mensagemRetorno.getCabecalho()){
                        case "erro":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), mensagemRetorno.getMensagem(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case "sucesso":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), mensagemRetorno.getMensagem(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            TransitionRightExtraField(LoginActivity.class,"email", usuario.getEmail(), "senha", usuario.getSenha());
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
        }).start();
    }

    private boolean validaSenha(String senha1, String senha2) {
        boolean isOk = true;
        if((Validator.allTrim(senha1).isEmpty())|| (Validator.allTrim(senha2).isEmpty())){
            isOk = false;
            alertaSenha.setText("Senha não foi preenchida!");
        }
        if(isOk){
            if(!senha1.equals(senha2)){
                isOk = false;
                alertaSenha.setText("As senhas são diferentes!");
            }
        }
        if(isOk){
            if(senha1.length() < 8 ){
                isOk = false;
                alertaSenha.setText("A senha deve ter 8 ou mais caracteres!");
            }
        }
        if(isOk){
            alertaSenha.setText("");

            usuario.setSenha(senha1);
        }
        return isOk;
    }


    private boolean validaNome(String nomeCompleto){

        if(Validator.allTrim(nomeCompleto).isEmpty()){
            alertaNome.setText("O campo Nome está vazio!");
            return false;
        }
        else{
            alertaNome.setText("");
            usuario.setNome(nomeCompleto);
        }
        return true;
    }

    private boolean validaEmail(String email) {

        if(Validator.allTrim(email).isEmpty()){
            alertaEmail.setText("O campo de E-mail está vazio!");
            return  false;
        }
        else {
            if(!Validator.validaEmail(email)){
                alertaEmail.setText("E-mail inválido!");
                return false;
            }
            else {
                alertaEmail.setText("");
                usuario.setEmail(email);
                usuario.setLogin(email);
            }

        }
        return true;
    }
    public boolean validaDados(){
        boolean isOK = true;
        if((Validator.allTrim(cep.getText().toString()).isEmpty())){
            isOK = false;
            alertaCep.setText("Cep não foi preenchido!");
        }
        if((Validator.allTrim(cidade.getText().toString()).isEmpty())){
            isOK = false;
            alertaCidade.setText("Cidade não foi preenchida!");
        }
        if((Validator.allTrim(bairro.getText().toString()).isEmpty())){
            isOK = false;
            alertaBairro.setText("Bairro não foi preenchido!");
        }
        if((Validator.allTrim(numero.getText().toString()).isEmpty())){
            isOK = false;
            alertaNumero.setText("Numero não foi preenchido!");
        }
        if((Validator.allTrim(cnpj.getText().toString()).isEmpty())){
            isOK = false;
            alertaCnpj.setText("Cnpj não foi preenchido!");
        }
        if((Validator.allTrim(rua.getText().toString()).isEmpty())){
            isOK = false;
            alertaRua.setText("Rua não foi preenchida!");
        }
        if((Validator.allTrim(telefone.getText().toString()).isEmpty())){
            isOK = false;
            alertaTelefone.setText("Telefone não foi preenchido!");
        }
        if(s.getSelectedItemPosition() == 0){
            isOK = false;
            alertaEstado.setText("Estado não selecionado!");
        }
        // passar por tudo e esta ok -> return true
        return isOK;
    }
    public void setNewImage(View v){

    }

}

/*
Se alguem quiser usar toast dentro de trhead usa isso aqui:

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "Socket", Toast.LENGTH_SHORT).show();
                        }
                    });

*/