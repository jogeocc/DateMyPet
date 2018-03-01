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

    private EditText txtNombre, txtDireccion, txtTelefono, txtCelular, txtCorreo;
    String nombre_usuario, contrasenia, nombre,direccion, telefono,celular, correo;
    apiService service;
    Call<ParseoToken> call;
    private static final String TAG = "RegistroActivity";
    ProgressDialog progress;

    TokenManager tokenManager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
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




        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(RegistroUsuarioActivity.this, "Hola", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });*/

      //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
      //          this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
      //  drawer.addDrawerListener(toggle);
      //  toggle.syncState();

      //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registro_usuario, menu);
        return true;
    }*/


    public  void  registrar_click(View v){

            if(validar()){
               progress = new ProgressDialog(this);
                progress.setTitle("Cargando");
                progress.setMessage("Registrando datos, por favor espere...");
                progress.setCancelable(false);
                progress.show();

               registrar();

               // Toast.makeText(this, "Se presiono el botonm", Toast.LENGTH_SHORT).show();
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
                    handleErrors(response.errorBody() );
                }

            }

            @Override
            public void onFailure(Call<ParseoToken> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());
                progress.dismiss();
                Toast.makeText(RegistroUsuarioActivity.this, "Lo sentimos ocurrio un error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mensaje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Felicidades!")
                .setMessage("La cuenta se registró con éxito")
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

    public void msjErrores(String Error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡UPPS!")
                .setMessage("La cuenta no se pudo registrar por los siguientes motivos: \n\n"+Error+"")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void handleErrors(ResponseBody response){
        progress.dismiss();
        String errores="";

        ApiError apiError = Utils.converErrors(response);
      //  Toast.makeText(this, ""+apiError.getErrors(), Toast.LENGTH_LONG).show();

        /*
        *  nombre_usuario,
                nombre,
                correo,
                direccion,
                telefono,
                celular,
                contrasenia);
        * */

        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()){
            if (error.getKey().equals("username")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("nombre")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("correo")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("direccion")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("password")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("celular")){
                errores+="- "+error.getValue().get(0)+"\n";
            }
        }

        msjErrores(errores);


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
