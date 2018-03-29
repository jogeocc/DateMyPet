package com.example.jgchan.datemypet;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class UnderTailActivity extends AppCompatActivity {

    ImageView circulo, derecha, izquierda, arriba , abajo;
    Vibrator vibe;
    MediaPlayer mpfa,mpdo,mpla, mpre, mpsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_tail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        circulo = (ImageView)findViewById(R.id.imgCirculo);
        derecha= (ImageView)findViewById(R.id.imgDerecha);
        izquierda= (ImageView)findViewById(R.id.imgIzquierda);
        arriba= (ImageView)findViewById(R.id.imgArriba);
        abajo= (ImageView)findViewById(R.id.imgAbajo);

        circulo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    circulo.setImageResource(R.drawable.cirpress);
                    reproducir("fa");
                    vibe.vibrate(100);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    circulo.setImageResource(R.drawable.cirnormal);
                }
                return true;
            }
        });

        derecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    derecha.setImageResource(R.drawable.derpress);
                    vibe.vibrate(100);
                    reproducir("si");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    derecha.setImageResource(R.drawable.dernomal);
                }
                return true;
            }
        });


        izquierda.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    izquierda.setImageResource(R.drawable.izqpress);
                    vibe.vibrate(100);
                    reproducir("re");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    izquierda.setImageResource(R.drawable.izqnormal);
                }
                return true;
            }
        });


        arriba.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    arriba.setImageResource(R.drawable.uppress);
                    reproducir("do");
                    vibe.vibrate(100);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    arriba.setImageResource(R.drawable.upnormal);
                }
                return true;
            }
        });


        abajo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    abajo.setImageResource(R.drawable.downpress);
                    vibe.vibrate(100);
                    reproducir("la");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    abajo.setImageResource(R.drawable.downnomal);
                }
                return true;
            }
        });
    }

    private void reproducir(String nota) {

        if(mpfa!=null){
            mpfa.release();
        }

        if(mpsi!=null){
            mpsi.release();
        }

        if(mpdo!=null){
            mpdo.release();
        }

        if(mpla!=null){
            mpla.release();
        }

        if(mpre!=null){
            mpre.release();
        }

        switch (nota){
            case "fa":
                mpfa = MediaPlayer.create(this, R.raw.fa);
                mpfa.start();
            break;

            case "do":
                mpdo = MediaPlayer.create(this, R.raw.doo);
                mpdo.start();
                break;

            case "si":
                mpsi = MediaPlayer.create(this, R.raw.si);
                mpsi.start();
                break;

            case "la":
                mpla = MediaPlayer.create(this, R.raw.la);
                mpla.start();
                break;

            case "re":
                mpre = MediaPlayer.create(this, R.raw.re);
                mpre.start();
                break;
        }


    }


}
