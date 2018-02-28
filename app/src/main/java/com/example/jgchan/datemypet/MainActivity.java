package com.example.jgchan.datemypet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TokenManager tokenManager;
    apiService service;
    private static final String TAG = "IngresarActivity";
    Call<AccessToken> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        service = RetrofitBuilder.createService(apiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));



    }


    public void login(View v){
        Intent i = new Intent(this,IngresarActivity.class);
        startActivity(i);
    }
    public  void registrar(View view){
       Intent intent = new Intent(this,RegistroActivity.class);

        startActivity(intent);
    }

}
