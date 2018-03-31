package com.example.jgchan.datemypet;



import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.Utils;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.ApiError;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.ParseoToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;


public class RegistroUsuarioActivity extends AppCompatActivity {

    Letrero objLetrero;

    private EditText txtNombre, txtDireccion, txtTelefono, txtCelular, txtCorreo;
    String nombre_usuario, contrasenia, nombre,direccion, telefono,celular, correo;
    apiService service;
    Call<ParseoToken> call;
    private static final String TAG = "RegistroActivity";
    final String msjDialogError = "La cuenta no se pudo crear";
    ProgressDialog progress;

    TokenManager tokenManager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        objLetrero = new Letrero(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtNombre=(EditText)findViewById(R.id.txtNombre);
        txtDireccion=(EditText)findViewById(R.id.txtDireccion);
        txtTelefono=(EditText)findViewById(R.id.txtTelefono);
        txtCelular=(EditText)findViewById(R.id.txtCelular);
        txtCorreo=(EditText)findViewById(R.id.txtCorreo);

        Bundle extras = getIntent().getExtras();
        nombre_usuario=extras.getString("nombre_usuario");
        contrasenia=extras.getString("contrasenia");
        nombre=extras.getString("nombre");
        correo=extras.getString("correo");
        direccion=extras.getString("direccion");
        telefono=extras.getString("telefono");
        celular=extras.getString("celular");


        if(nombre.length()>0) txtNombre.setText(nombre);
        if(correo.length()>0) txtCorreo.setText(correo);
        if(direccion.length()>0) txtDireccion.setText(direccion);
        if(telefono.length()>0) txtTelefono.setText(telefono);
        if(celular.length()>0) txtCelular.setText(celular);


        service = RetrofitBuilder.createService(apiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));


    }


    public  void  registrar_click(View v){

            if(validar()){
              progress= objLetrero.msjCargando("Registrando datos, por favor espere...");
              progress.show();
              registrar();
            }
    }

    private boolean validar() {

        nombre=txtNombre.getText().toString();
        direccion=txtDireccion.getText().toString();;
        telefono=txtTelefono.getText().toString();;
        celular=txtCelular.getText().toString();;
        correo=txtCorreo.getText().toString();;

        return  true;
    }


    public void  registrar(){


        call= service.registrar(
                nombre_usuario,
                nombre,
                correo,
                direccion,
                telefono,
                celular,
                contrasenia);

        call.enqueue(new Callback<ParseoToken>() {
            @Override
            public void onResponse(Call<ParseoToken> call, Response<ParseoToken> response) {

                Log.w(TAG, "onResponse: "+response );
                if(response.isSuccessful()){
                        tokenManager.saveToken(response.body());
                        progress.dismiss();
                        mensaje();

                }else{
                    try{
                        objLetrero.handleErrors(response.errorBody(),msjDialogError,progress );
                    }catch (Exception e){
                        Toast.makeText(RegistroUsuarioActivity.this, "Codigo: "+response.code()+" mensaje: "+response.message(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ParseoToken> call, Throwable t) {
                    objLetrero.msjErrorCarga(progress);
            }
        });
    }

    public void mensaje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Felicidades!")
                .setMessage("La cuenta se registró con éxito \n por favor active su cuenta")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {

        validar();
        Intent i = getIntent();
        i.putExtra("nombre_usuario",nombre_usuario);
        i.putExtra("contrasenia",contrasenia);
        i.putExtra("nombre",nombre);
        i.putExtra("correo",correo);
        i.putExtra("direccion",direccion);
        i.putExtra("telefono",telefono);
        i.putExtra("celular",celular);
        setResult(RESULT_OK,i);
        finish();
        return true;
    }



}
