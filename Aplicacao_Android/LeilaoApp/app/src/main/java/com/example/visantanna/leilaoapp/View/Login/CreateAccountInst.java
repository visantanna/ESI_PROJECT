package com.example.visantanna.leilaoapp.View.Login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.baseActivity;
import com.example.visantanna.leilaoapp.controllers.MensagemRetorno;
import com.example.visantanna.leilaoapp.controllers.Validator;
import com.example.visantanna.leilaoapp.controllers.imageController;
import com.example.visantanna.leilaoapp.db_classes.Instituicao;
import com.example.visantanna.leilaoapp.db_classes.Mensagem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;


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
    private ImageView fotoPerfil;



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
    private final static int RESULT_LOAD_IMAGE = 1;
    private Bitmap imagemPerfil;

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
        fotoPerfil = (ImageView)findViewById(R.id.UserPhoto1);
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

        boolean isDadosValidos = false;

        MensagemRetorno emailRetorno = Validator.validaEmailCadastro(email.getText().toString());
        if(emailRetorno.isOk()){
            usuario.setLogin(email.getText().toString());
            usuario.setEmail(email.getText().toString());
        }else{
            alertaEmail.setText(emailRetorno.getMensagem());
        }

        MensagemRetorno senhaRetorno = Validator.validaSenhaCadastro(senha.getText().toString() , repeteSenha.getText().toString() );
        if(senhaRetorno.isOk()){
            usuario.setSenha(senha.getText().toString());
        }else{
            alertaSenha.setText(senhaRetorno.getMensagem());
        }

        MensagemRetorno nomeRetorno  = Validator.validaNomeCadastro(nomeCompleto.getText().toString() );
        if(nomeRetorno.isOk()){
            usuario.setNome(nomeCompleto.getText().toString());
        }else{
            alertaNome.setText(nomeRetorno.getMensagem());
        }

        isDadosValidos = validaDados();


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
                
        if(emailRetorno.isOk() && senhaRetorno.isOk() && nomeRetorno.isOk() && isDadosValidos ){
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Falha ao Conectar Com o Servidor", Toast.LENGTH_LONG).show();
                            }
                        });
                        e1.printStackTrace();
                        Log.e("Teste", "ERRO Socket", e1);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Log.e("Teste", "ERRO socket", e1);
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                        Log.w("JSON!!!", json);


                        String jsonIn = in.readLine();
                        final Mensagem mensagemRetorno = gson.fromJson(jsonIn, Mensagem.class);

                        Log.w("RETORNOACCOUNTINST", mensagemRetorno.getMensagem());

                        switch (mensagemRetorno.getCabecalho()) {
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
                                TransitionRightExtraField(LoginActivity.class, "email", usuario.getEmail(), "senha", usuario.getSenha());
                                break;
                            default:
                                //dando um "print" do json no log
                                Log.w("default", "algo errado no switch do cabecalho");
                                break;

                        }
                    }//fim do ifsocket is connected
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
        }).start();
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
    public void setPerfilImage(View v){
        Intent buscarImagem = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        buscarImagem.setType("image/*");
        startActivityForResult(buscarImagem , RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode , Intent data){
        super.onActivityResult(requestCode , resultCode , data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri image = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            try {
                InputStream imageStream = getContentResolver().openInputStream(image);
                Bitmap fotoBitMap = BitmapFactory.decodeStream(imageStream);

                if (fotoBitMap != null) {
                    ExifInterface exif = null;
                    //rotaciona imagem
                    try{
                        exif = new ExifInterface(image.getPath());
                        if(exif != null){
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION ,
                                    ExifInterface.ORIENTATION_UNDEFINED);
                            fotoBitMap = imageController.adjustOrientation(orientation,fotoBitMap);
                        }
                    }catch(Exception e){
                        Log.w("error" , e+"");
                    }
                    //deixa a imagem quadrada
                    Bitmap squareImage = imageController.cutEdges(fotoBitMap);
                    //corta as bordas
                    Bitmap croppedImage = imageController.getCroppedBitmap(squareImage);
                    fotoPerfil.setImageBitmap(croppedImage);
                    imagemPerfil = fotoBitMap;
                }
            }catch(Exception e){
                Log.e("error" , e+"");
            }
        }
    }


}

