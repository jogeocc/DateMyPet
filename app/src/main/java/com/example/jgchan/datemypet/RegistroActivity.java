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
    String nombre_usuario, contrasenia, rcontrasenia, nombre="", correo="", direccion="", telefono="", celular="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombreUusario =(EditText)findViewById(R.id.txtNombreUusario);
        txtContrasenia =(EditText)findViewById(R.id.txtContrasenia);
        txtConfirmarContrasenia =(EditText)findViewById(R.id.txtConfirmarContrasenia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void continuar_click(View view){
        if(validar()){

            Intent i = new Intent(getApplicationContext(), RegistroUsuarioActivity.class);
            i.putExtra("nombre_usuario",nombre_usuario);
            i.putExtra("contrasenia",contrasenia);
            i.putExtra("nombre",nombre);
            i.putExtra("correo",correo);
            i.putExtra("direccion",direccion);
            i.putExtra("telefono",telefono);
            i.putExtra("celular",celular);

            //startActivity(i);
            startActivityForResult(i,1);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




        if(requestCode == 1 && resultCode == RESULT_OK){
            nombre = data.getExtras().getString("nombre");
            correo = data.getExtras().getString("correo");
            direccion = data.getExtras().getString("direccion");
            telefono = data.getExtras().getString("telefono");
            celular = data.getExtras().getString("celular");
        }


    }

    private boolean validar() {

        nombre_usuario=txtNombreUusario.getText().toString();
        contrasenia=txtContrasenia.getText().toString();
        rcontrasenia=txtConfirmarContrasenia.getText().toString();

       //Toast.makeText(this, "contrasenia: "+rcontrasenia, Toast.LENGTH_SHORT).show();

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

        if(!(contrasenia.toString().equals(rcontrasenia.toString()))){
            Toast.makeText(this, "Las contraseñas no concuerdan", Toast.LENGTH_SHORT).show();
            return  false;
        }

        return  true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
