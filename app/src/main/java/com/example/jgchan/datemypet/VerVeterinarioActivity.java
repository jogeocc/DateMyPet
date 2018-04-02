package com.example.jgchan.datemypet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Usuarios;
import com.example.jgchan.datemypet.entidades.Veterinario;
import com.example.jgchan.datemypet.entidades.Veterinarios;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerVeterinarioActivity extends AppCompatActivity {

    String nombre="Prueba",idVeterinario;
    Toolbar toolbar;
    Call<Veterinarios> call;
    Call<Success> call2;
    String respuesta;
    Veterinario veterinario;
    apiService service;
    TextView tvNombreVeterinario,tvTelefonoVeterinario,tvDireccion;
    ProgressDialog progress;
    FloatingActionButton eliminarMascota;
    TextView tvNombreVeterinaria;
    int errores=0;
    boolean elimino=false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_veterinario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

       tvNombreVeterinaria = (TextView)this.findViewById(R.id.tvVerVetNombre);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Bundle extras = getIntent().getExtras();
        idVeterinario=extras.getString("idVeterinario");
        nombre=extras.getString("nombre");

        this.setTitle("");
        tvNombreVeterinaria.setText(nombre);


        tvNombreVeterinario = (TextView)findViewById(R.id.tvVerNombreVeterinario);
        tvTelefonoVeterinario = (TextView)findViewById(R.id.tvVerTelefonoVet);
        tvDireccion = (TextView)findViewById(R.id.tvVerDireccionVet);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEliminarVet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msjConfirmacion();
            }
        });

        FloatingActionButton fabLlamada = (FloatingActionButton) findViewById(R.id.fabLlamarVet);
        fabLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telefono=tvTelefonoVeterinario.getText().toString();
               // Toast.makeText(VerVeterinarioActivity.this, "Entro a llamda", Toast.LENGTH_SHORT).show();
                llamarVeterinario(telefono);
            }
        });

        buscarInfoVet();

    }

    private void llamarVeterinario(String vetTelefono) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + vetTelefono));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //para versiones con android 6.0 o superior.
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        1);
            }


        }

        startActivity(callIntent);

    }


    public void buscarInfoVet(){

        if(errores==0){
            progress = new ProgressDialog(this);
            progress.setTitle("Cargando");
            progress.setMessage("Buscando información del veterinario, por favor espere...");
            progress.setCancelable(false);
            progress.show();
        }

        call= service.infoVeterinario(
                ""+idVeterinario
        );

        call.enqueue(new Callback<Veterinarios>() {
            @Override
            public void onResponse(Call<Veterinarios> call, Response<Veterinarios> response) {

                //progress.dismiss();
                // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
                // Toast.makeText(VerPerfilActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                //Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    String tel;
                    progress.dismiss();
                    veterinario=response.body().getVeterinario();

                    tvNombreVeterinaria.setText(veterinario.getVetNomVeterinaria());
                    tvNombreVeterinario.setText(veterinario.getVetNombre());
                    tvTelefonoVeterinario.setText(veterinario.getVetTelefono());
                    tvDireccion.setText(veterinario.getVetDireccion());


                }else{
                    progress.dismiss();
                    Toast.makeText(VerVeterinarioActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Veterinarios> call, Throwable t) {

                if(!(errores<3)){
                    errores++;
                    buscarInfoVet();
                }else{
                    Toast.makeText(VerVeterinarioActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();
                    errores=0;
                    progress.dismiss();
                }


            }
        });

    }

    public void msjConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Advertencia!")
                .setMessage("¿Seguro que desea eliminar a su veterinario?")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Eliminar veterinario", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliminarVet();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }



    private void eliminarVet() {

        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Eliminando veterinario, por favor espere...");
        progress.setCancelable(false);
        progress.show();



        call2= service.eliminarVeterinario(
                ""+idVeterinario
        );

        call2.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                // Toast.makeText(InfoMascotaActivity.this, "Error vuelva intentarlo mas tarde: " +response , Toast.LENGTH_LONG).show();


                if(response.isSuccessful()){
                    progress.dismiss();
                    respuesta=response.body().getSuccess();
                    elimino=true;
                    msjExito(respuesta);


                }else{
                    progress.dismiss();
                    Toast.makeText(VerVeterinarioActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                Toast.makeText(VerVeterinarioActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

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

                            Intent i = new Intent(VerVeterinarioActivity.this,ListaVeterinariosActivity.class);
                            startActivity(i);
                            finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ver_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, EditarVeterinarioActivity.class);
            i.putExtra("idVeterinario",idVeterinario);
            startActivityForResult(i,1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            buscarInfoVet();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

}
