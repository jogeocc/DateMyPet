package com.example.jgchan.datemypet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombreUusario, txtContrasenia, txtConfirmarContrasenia;
    private Button btnContinuar;
    String nombre_usuario, contrasenia, rcontrasenia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombreUusario =(EditText)findViewById(R.id.txtNombreUusario);
        txtContrasenia =(EditText)findViewById(R.id.txtContrasenia);
        txtConfirmarContrasenia =(EditText)findViewById(R.id.txtConfirmarContrasenia);

    }

    public void continuar_click(View view){
        if(validar()){

            Intent i = new Intent(getApplicationContext(), RegistroUsuarioActivity.class);
            i.putExtra("nombre_usuario",nombre_usuario);
            i.putExtra("contrasenia",contrasenia);
            startActivity(i);

        }
    }

    private boolean validar() {

        nombre_usuario=txtNombreUusario.getText().toString();
        contrasenia=txtContrasenia.getText().toString();
        rcontrasenia=txtConfirmarContrasenia.getText().toString();

        Toast.makeText(this, "contrasenia: "+rcontrasenia, Toast.LENGTH_SHORT).show();

        if(nombre_usuario.length()==0){
            Toast.makeText(this, "No ingresó el nombre de usuario", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if(contrasenia.length()==0){
            Toast.makeText(this, "No ingresó la contraseña", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if(rcontrasenia.length()==0){
            Toast.makeText(this, "Confirmó la contraseña", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if(rcontrasenia==contrasenia){
            Toast.makeText(this, "Las contraseñas no concuerdan", Toast.LENGTH_SHORT).show();
            return  false;
        }

        return  true;

    }
}
