package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.Utils;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.ApiError;
import com.example.jgchan.datemypet.entidades.ParseoToken;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngresarActivity extends AppCompatActivity {

    private EditText txtUsername, txtContrasenia;
    String nombre_usuario,contrasenia;
    TokenManager tokenManager;
    apiService service;
    private static final String TAG = "IngresarActivity";
    Call<ParseoToken> call;
    ProgressDialog progress=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);


        txtUsername=(EditText)findViewById(R.id.txtUsername);
        txtContrasenia=(EditText)findViewById(R.id.txtContrasenia);


        service = RetrofitBuilder.createService(apiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));


       if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(IngresarActivity.this, VeterinarioActivity.class));
            finish();
        }

        if(tokenManager.getToken().getName_user()!=null){
            txtUsername.setText(tokenManager.getToken().getName_user());
        }

    }


    public void ingresar(View v){


        if(validar()){

            progress = new ProgressDialog(this);
            progress.setTitle("Cargando");
            progress.setMessage("Validando datos, por favor espere...");
            progress.setCancelable(false);
            progress.show();
            ingresar();
        }
    }

    public void mensaje() {
        Snackbar.make(getWindow().getDecorView().getRootView(), "Usuario no registrado", Snackbar.LENGTH_LONG)
                .setAction("Registrar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            Intent intent = new Intent(IngresarActivity.this,RegistroActivity.class);
                            startActivity(intent);
                    }
                }).show();
    }

    private boolean validar() {

        nombre_usuario=txtUsername.getText().toString();
        contrasenia=txtContrasenia.getText().toString();

        return  true;
    }

    public void  ingresar(){


        call= service.login(
                nombre_usuario,
                contrasenia);

        call.enqueue(new Callback<ParseoToken>() {
            @Override
            public void onResponse(Call<ParseoToken> call, Response<ParseoToken> response) {

                progress.dismiss();
               // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
                Toast.makeText(IngresarActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
               //return;
                Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    tokenManager.saveToken(response.body());
                    startActivity(new Intent(IngresarActivity.this, MenuActivity.class));
                    finish();
                }else{

                    if (response.code() == 421) {
                         mensaje();
                        //Toast.makeText(IngresarActivity.this, "Credenciales no correspondientes", Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 420) {
                        handleErrors(response.errorBody());
                        //Toast.makeText(IngresarActivity.this, "Credenciales no correspondientes", Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 401) {
                       // ApiError apiError = Utils.converErrors(response.errorBody());
                        handleErrors(response.errorBody());
                        //
                    }
                    if (response.code() == 403) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(IngresarActivity.this);
                        builder.setTitle("¡OOPS!")
                                .setMessage("Su cuenta esta desactivada,¿Desea Activarla?")
                                .setCancelable(false)
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })
                                .setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ParseoToken> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                msjErrores("Error en la conexión");
            }
        });


    }

    public void msjErrores(String Error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡OOPS!")
                .setMessage("No pudo ingresar por los siguientes motivos: \n\n"+Error+"")
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


        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()){
            if (error.getKey().equals("username")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("password")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("auto")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

        }

        msjErrores(errores);


    }



    @Override
    public void onBackPressed() {

        if(progress!=null){

            if(progress.isShowing()){
                if(call != null){
                    call.cancel();
                    call = null;
                }
                progress.dismiss();
            }

        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
            call = null;
        }
    }
}
