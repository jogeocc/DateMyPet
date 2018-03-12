package com.example.jgchan.datemypet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Usuarios;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoMascotaActivity extends AppCompatActivity {

    String nombre="Prueba",idMascota;
    Toolbar toolbar;
    Call<Mascotas> call;
    Call<Success> call2;
    apiService service;
    ImageView fotoMascota;
    String respuesta;
    TextView tvVerMasTipo,tvVerMasSexo,tvVerMasEdad,tvVerMasSenPart,tvVerMasHobbie;
    ProgressDialog progress;

    Switch compartirPerfil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_mascota);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fotoMascota=(ImageView)findViewById(R.id.imgVerMascota);
        tvVerMasTipo=(TextView)findViewById(R.id.tvVerMasTipo);
        tvVerMasSexo=(TextView)findViewById(R.id.tvVerMasSexo);
        tvVerMasEdad=(TextView)findViewById(R.id.tvVerMasEdad);
        tvVerMasSenPart=(TextView)findViewById(R.id.tvVerMasSenasPart);
        tvVerMasHobbie=(TextView)findViewById(R.id.tvVerMasHobie);
        compartirPerfil =(Switch)findViewById(R.id.verMasCompPer);

        compartirPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(InfoMascotaActivity.this, "Se presiono "+compartirPerfil.getText(), Toast.LENGTH_SHORT).show();
                compartirPerfil();

            }
        });



        Bundle extras = getIntent().getExtras();
        idMascota=extras.getString("idMascota");
        nombre=extras.getString("nombre");

        this.setTitle(nombre);


        //Toast.makeText(this, "El id es " + idMascota, Toast.LENGTH_SHORT).show();
        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getInfoMascota();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public  void  getInfoMascota(){



        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Buscando mascota, por favor espere...");
        progress.setCancelable(false);
        progress.show();



        call= service.infoMascota(
               ""+idMascota
        );

        call.enqueue(new Callback<Mascotas>() {
            @Override
            public void onResponse(Call<Mascotas> call, Response<Mascotas> response) {

                //progress.dismiss();
                 //Toast.makeText(InfoMascotaActivity.this, "Codigo: "+response.body() , Toast.LENGTH_LONG).show();
                // Toast.makeText(InfoMascotaActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                Log.w("VER ANIMAL", "onResponse: "+response.body());
                if(response.isSuccessful()){
                    Mascota mas = response.body().getMascota();
                    progress.dismiss();

                    String url = "http://date-my-pet-mx.tk/"+mas.getMasFoto();
                    Picasso.get().load(url).into(fotoMascota);


                    tvVerMasTipo.setText(tvVerMasTipo.getText()+" "+mas.getMasTipo());
                    tvVerMasSexo.setText(tvVerMasSexo.getText()+" "+mas.getMasSexo());
                    tvVerMasEdad.setText(tvVerMasEdad.getText()+" "+mas.getMasEdad());
                    tvVerMasSenPart.setText(tvVerMasSenPart.getText()+mas.getMasSenaPart());
                    tvVerMasHobbie.setText(tvVerMasHobbie.getText()+mas.getMasHobbie());

                    if(mas.getMasCompPerf()==1)
                        compartirPerfil.setChecked(true);
                    else
                        compartirPerfil.setChecked(false);

                }else{
                    progress.dismiss();
                    Toast.makeText(InfoMascotaActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Mascotas> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                Toast.makeText(InfoMascotaActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

            }
        });

    }

    public  void  compartirPerfil(){



        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Compartiendo información, por favor espere...");
        progress.setCancelable(false);
        progress.show();



        call2= service.compartirPerfil(
                ""+idMascota
        );

        call2.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                //progress.dismiss();
                //Toast.makeText(InfoMascotaActivity.this, "Codigo: "+response.body() , Toast.LENGTH_LONG).show();
                // Toast.makeText(InfoMascotaActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                Log.w("VER ANIMAL", "onResponse: "+response.body());
                if(response.isSuccessful()){

                    respuesta=response.body().getSuccess();
                    msjExito(respuesta);

                }else{
                    progress.dismiss();
                    Toast.makeText(InfoMascotaActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                Toast.makeText(InfoMascotaActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

            }
        });
    }

    public void msjExito(String respuesta) {
        progress.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
