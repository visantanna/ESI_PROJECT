package com.example.visantanna.leilaoapp.View;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;

import com.example.visantanna.leilaoapp.R;
import com.example.visantanna.leilaoapp.base.baseActivity;
import com.example.visantanna.leilaoapp.controllers.Validator;

public class MainActivity extends baseActivity {
    private AppCompatEditText email;
    private AppCompatEditText senha;
    private TextView mensagemEmail;


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
        mensagemEmail = (TextView)  findViewById(R.id.mensagemEmail);
    }


    public void logInClicked(View button){
        String strEmail = email.getText().toString();
        boolean emailValido = Validator.validaEmail(strEmail);
        if(emailValido){
            mensagemEmail.setText("");
            String strSenha = senha.getText().toString();
            verificaConta(strEmail ,strSenha );
        }else{
            mensagemEmail.setText("O E-mail não é valido!");
        }
    }

    private void verificaConta(String email , String senha) {


    }
    public void createAccount(View v){
        String strEmail = email.getText().toString();
        String strSenha = senha.getText().toString();
        TransitionRightExtraField(CreateAccount.class ,"email",strEmail ,"senha", strSenha );
    }
}
