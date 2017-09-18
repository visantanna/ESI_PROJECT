package com.example.visantanna.leilaoapp.View;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.baseActivity;
import com.example.visantanna.leilaoapp.controllers.Validator;

/**
 * Created by vis_a on 17-Sep-17.
 */

public class CreateAccount extends baseActivity{
    private AppCompatEditText email;
    private AppCompatEditText senha;
    private AppCompatEditText repeteSenha;
    private AppCompatEditText nomeCompleto;
    private TextView alertaEmail;
    private TextView alertaSenha;
    private TextView alertaNome;

    @Override
    protected void onCreate(Bundle BundleSavedStance){
        super.onCreate(BundleSavedStance);
        setContentView(R.layout.new_account);
        mContext = getBaseContext();
        jbInit();
    }

    private void jbInit() {
        email = (AppCompatEditText)findViewById(R.id.emailTextBoxNewAccount);
        senha = (AppCompatEditText)findViewById(R.id.SenhaTextBoxNewAccount);
        repeteSenha = (AppCompatEditText)findViewById(R.id.RepeteSenhaTextBoxNewAccount);
        nomeCompleto =(AppCompatEditText)findViewById(R.id.NomeCompletoTextBoxNewAccount);
        alertaEmail = (TextView) findViewById(R.id.AlertaEmail);
        alertaSenha = (TextView) findViewById(R.id.AlertaSenha);
        alertaNome = (TextView) findViewById(R.id.AlertaNomeCompleto);

        email.setText(getIntent().getExtras().getString("email"));
        senha.setText(getIntent().getExtras().getString("senha"));
    }
    public void createNewAccount(View v){
        boolean isEmailValido = validaEmail(email.getText().toString());
        boolean isSenhaValida = validaSenha(senha.getText().toString() , repeteSenha.getText().toString());
        boolean isNomeValido  = validaNome(nomeCompleto.getText().toString() );

        if(isEmailValido && isSenhaValida && isNomeValido){
            enviaRequestNewAccount();
        }
    }

    private void enviaRequestNewAccount() {

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
                alertaSenha.setText("A senha deve ter 8 ou mais caracteres!");
            }
        }
        if(isOk){
            alertaSenha.setText("");
        }
        return isOk;
    }


    private boolean validaNome(String nomeCompleto){
        boolean isOk = true;
        if(Validator.allTrim(nomeCompleto).isEmpty()){
            isOk = false;
            alertaNome.setText("O campo Nome está vazio!");
        }
        if(isOk){
            alertaNome.setText("");
        }
        return isOk;
    }

    private boolean validaEmail(String email) {
        boolean isOk = true;
        if(Validator.allTrim(email).isEmpty()){
            isOk = false;
            alertaEmail.setText("O campo de E-mail está vazio!");
        }
        if(isOk){
            isOk = Validator.validaEmail(email);
            if(!isOk){
                alertaEmail.setText("E-mail inválido!");
            }
        }
        if(isOk){
            alertaEmail.setText("");
        }
        return isOk;
    }
    public void setNewImage(View v){

    }


}
