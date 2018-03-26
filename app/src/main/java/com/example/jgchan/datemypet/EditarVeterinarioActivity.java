package com.example.jgchan.datemypet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.Utils;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.ApiError;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Veterinario;
import com.example.jgchan.datemypet.entidades.Veterinarios;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarVeterinarioActivity extends MenuActivity {

    String nombre="Prueba",idVeterinario;
    private TokenManager tokenManager;
    Toolbar toolbar;
    Call<Veterinarios> call;
    Call<Success> call2;
    String respuesta;
    Veterinario veterinario;
    private AccessToken datosAlamcenados;
    Button btnEditarVet;
    apiService service;
    EditText edtEditNomVete,edtEditNomVet,edtEditTel,edtEditDir;
    ProgressDialog progress;
    TextView nombreUsuario, correoUsuario;
    int errores=0,erroresAct=0;
    boolean elimino=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_veterinario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        //HABILITAMOS EL SERVICIO DE PETICION
        service = RetrofitBuilder.createService(apiService.class);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //OBTENIENDO EL PADRE HEADER
        View headerView = navigationView.getHeaderView(0);

        //BUSCANDO CONTROLLERS MEDIANTE EL PADRE QUE LOS CONTIENE
        nombreUsuario=(TextView) headerView.findViewById(R.id.tvNombreCompleto);
        correoUsuario=(TextView) headerView.findViewById(R.id.tvCorreoUsuario);

        //SETEANDO LOS VALORES DE CORREO Y NOMBRE COMPLETO EN EL HEADER BUSCADOR
        nombreUsuario.setText("Bienvenido " +datosAlamcenados.getName_user());
        correoUsuario.setText(datosAlamcenados.getEmail());

        Bundle extras = getIntent().getExtras();
        idVeterinario=extras.getString("idVeterinario");


        edtEditNomVete = (EditText)findViewById(R.id.edtEditNomVet);
        edtEditNomVet= (EditText)findViewById(R.id.edtEditVetNomVet);
        edtEditTel= (EditText)findViewById(R.id.edtEditarVetTel);
        edtEditDir= (EditText)findViewById(R.id.edtEditarVetDir);
        btnEditarVet = (Button)findViewById(R.id.btnEditarVet);

        btnEditarVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarVeterinario();
            }
        });

        buscarInfoVet();

    }

    private void actualizarVeterinario() {
        if(errores==0){
            progress = new ProgressDialog(this);
            progress.setTitle("Cargando");
            progress.setMessage("Buscando información del veterinario, por favor espere...");
            progress.setCancelable(false);
            progress.show();
        }

        call2= service.actualizarVeterinario(
                ""+idVeterinario,

                edtEditNomVete.getText().toString(),
                edtEditNomVet.getText().toString(),
                edtEditDir.getText().toString(),
                edtEditTel.getText().toString()
        );

        call2.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                if(response.isSuccessful()){
                    String tel;
                    progress.dismiss();
                    respuesta=response.body().getSuccess();
                    msjExito(respuesta);



                }else{
                    progress.dismiss();
                    if(response.code()==401){
                        handleErrors(response.errorBody() );

                        Log.e("Error Server",response.errorBody()+ " "+response.code());

                        Log.e("Error Server",response.message()+ " "+response.code());

                    }else {
                        Log.e("Error Server",response.message()+ " "+response.code());
                    }
                   // Toast.makeText(EditarVeterinarioActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {

                if(!(errores<3)){
                    errores++;
                    buscarInfoVet();
                }else{
                    Toast.makeText(EditarVeterinarioActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();
                    errores=0;
                    progress.dismiss();
                }


            }
        });
    }


    public void buscarInfoVet() {

        if (erroresAct == 0) {
            progress = new ProgressDialog(this);
            progress.setTitle("Cargando");
            progress.setMessage("Actualizando información del veterinario, por favor espere...");
            progress.setCancelable(false);
            progress.show();
        }

        call = service.infoVeterinario(
                "" + idVeterinario
        );

        call.enqueue(new Callback<Veterinarios>() {
            @Override
            public void onResponse(Call<Veterinarios> call, Response<Veterinarios> response) {

                if (response.isSuccessful()) {
                    String tel;
                    progress.dismiss();
                    veterinario = response.body().getVeterinario();
                    edtEditNomVete.setText(veterinario.getVetNomVeterinaria());
                    edtEditNomVet.setText(veterinario.getVetNombre());
                    edtEditTel.setText(veterinario.getVetTelefono());
                    edtEditDir.setText(veterinario.getVetDireccion());

                } else {
                    progress.dismiss();
                    Toast.makeText(EditarVeterinarioActivity.this, "Error vuelva intentarlo mas tarde", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Veterinarios> call, Throwable t) {

                if (!(erroresAct < 3)) {
                    erroresAct++;
                    buscarInfoVet();
                } else {
                    Toast.makeText(EditarVeterinarioActivity.this, "Error vuelva intentarlo mas tarde", Toast.LENGTH_LONG).show();
                    erroresAct = 0;
                    progress.dismiss();
                }


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

            Log.e("Error Server",""+error.getValue());

            if (error.getKey().equals("vetNomVeterinaria")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("vetNombre")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("vetTelefono")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("vetDireccion")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("vetNomVeterinaria")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

        }

        msjErrores(errores);


    }



    public void msjExito(String respuesta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = getIntent();
                        setResult(RESULT_OK,i);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



}





