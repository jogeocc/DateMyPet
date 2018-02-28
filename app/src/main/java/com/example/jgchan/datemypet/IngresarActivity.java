package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    Call<AccessToken> call;
    ProgressDialog progress;



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

    private boolean validar() {

        nombre_usuario=txtUsername.getText().toString();
        contrasenia=txtContrasenia.getText().toString();

        return  true;
    }

    public void  ingresar(){


        call= service.login(
                nombre_usuario,
                contrasenia);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                progress.dismiss();
              // Toast.makeText(IngresarActivity.this, "Entro a errores: "+response.code() , Toast.LENGTH_LONG).show();
               //return;
                Log.w(TAG, "onResponse: "+response );
                if(response.isSuccessful()){
                    progress.dismiss();
                    tokenManager.saveToken(response.body());
                    startActivity(new Intent(IngresarActivity.this, VeterinarioActivity.class));
                    finish();
                }else{

                    if (response.code() == 422) {

                        Toast.makeText(IngresarActivity.this, ""+Utils.converErrors(response.errorBody()), Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 401) {
                       // ApiError apiError = Utils.converErrors(response.errorBody());
                        handleErrors(response.errorBody());
                        //
                    }
                }

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());
            }
        });


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
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
            call = null;
        }
    }
}
