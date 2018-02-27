package com.example.jgchan.datemypet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void login(View v){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }
    public  void registrar(View view){
       Intent intent = new Intent(this,RegistroActivity.class);

        startActivity(intent);
    }
}
