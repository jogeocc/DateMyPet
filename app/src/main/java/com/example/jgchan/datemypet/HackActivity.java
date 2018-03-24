package com.example.jgchan.datemypet;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HackActivity extends AppCompatActivity implements View.OnClickListener{

    Button arriba, abajo, izquierda, derecha,select,start;
    FloatingActionButton  A, B;
    String codigo="";
    Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hack);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        arriba = (Button)findViewById(R.id.btnArriba);
        abajo  = (Button)findViewById(R.id.btnAbajo);
        izquierda  = (Button)findViewById(R.id.btnIzq);
        derecha  = (Button)findViewById(R.id.btnDer);
        select  = (Button)findViewById(R.id.btnSelect);
        start  = (Button)findViewById(R.id.btnStart);
        A  = (FloatingActionButton) findViewById(R.id.btnA);
        B  = (FloatingActionButton) findViewById(R.id.btnB);


        arriba.setOnClickListener(this);
        abajo.setOnClickListener(this);
        izquierda .setOnClickListener(this);
        derecha.setOnClickListener(this);
        select.setOnClickListener(this);
        start.setOnClickListener(this);
        A.setOnClickListener(this);
        B.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnArriba:
                codigo+="UP";
                break;
            case R.id.btnAbajo:
                codigo+="DOWN";
                break;
            case R.id.btnIzq:
                codigo+="LEFT";
                break;
            case R.id.btnDer:
                codigo+="RIGHT";
                break;
            case R.id.btnA:
                codigo+="A";
                break;
            case R.id.btnB:
                codigo+="B";
                break;
            case R.id.btnSelect:
                codigo+="SELECT";
                break;
            case R.id.btnStart:
                codigo+="START";
                break;
        }

        vibe.vibrate(100);
        Toast.makeText(this, "Ingreso: "+codigo, Toast.LENGTH_SHORT).show();

    }
}
