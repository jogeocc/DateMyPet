package com.example.jgchan.datemypet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.Utils;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.ApiError;
import com.example.jgchan.datemypet.entidades.ParseoToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername, txtContrasenia;
    String nombre_usuario,contrasenia;
    TokenManager tokenManager;
    apiService service;
    private static final String TAG = "IngresarActivity";
    Call<ParseoToken> call;
    ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txtUsername=(EditText)findViewById(R.id.txtUsername);
        txtContrasenia=(EditText)findViewById(R.id.txtContrasenia);


        service = RetrofitBuilder.createService(apiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));


       if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(LoginActivity.this, VeterinarioActivity.class));
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


        call= service.ingresarfake(
                nombre_usuario,
                contrasenia);

        call.enqueue(new Callback<ParseoToken>() {
            @Override
            public void onResponse(Call<ParseoToken> call, Response<ParseoToken> response) {

                progress.dismiss();
                Toast.makeText(LoginActivity.this, "Entro a errores: "+response, Toast.LENGTH_LONG).show();
                //return;
                Log.w(TAG, "onResponse: "+response );
                if(response.isSuccessful()){
                    progress.dismiss();
                    tokenManager.saveToken(response.body());
                    startActivity(new Intent(LoginActivity.this, VeterinarioActivity.class));
                    finish();
                }else{


                    if (response.code() == 422) {
                        handleErrors(response.errorBody());
                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.converErrors(response.errorBody());
                        Toast.makeText(LoginActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ParseoToken> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());
                progress.dismiss();
                Toast.makeText(LoginActivity.this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
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

        }

        msjErrores(errores);


    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Presiono regresar", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
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

