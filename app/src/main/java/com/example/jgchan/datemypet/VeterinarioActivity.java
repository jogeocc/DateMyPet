package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
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
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.ParseoToken;
import com.example.jgchan.datemypet.entidades.Success;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.exit;

public class VeterinarioActivity extends MenuActivity {

    private TokenManager tokenManager;
    apiService service;
    String id_user=null;
    private static final String TAG = "VeterinarioActivity";
    Call<Success> call;
    private AccessToken datosAlamcenados;
    TextView nombreUsuario, correoUsuario;
    EditText edtCreaNomVet,edtCrearVetNomVet,edtCrearVetDir, edtCrearVetTel;
    Button btnRegistrarVeterinario;
    private String respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados = tokenManager.getToken();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        // Agregando controllers de la plantilla

            edtCreaNomVet = (EditText)findViewById(R.id.edtCreaNomVet);
            edtCrearVetNomVet  = (EditText)findViewById(R.id.edtCrearVetNomVet);
            edtCrearVetDir  = (EditText)findViewById(R.id.edtCrearVetDir);
            edtCrearVetTel  = (EditText)findViewById(R.id.edtCrearVetTel);
            btnRegistrarVeterinario = (Button)findViewById(R.id.btnRegVet);

        //_______________________________________________

        //Agregando el metodo de escucha

            btnRegistrarVeterinario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrarVeterinario();
                }
            });

        //--------

        //BUSCANDO CONTROLLERS MEDIANTE EL PADRE QUE LOS CONTIENE
        nombreUsuario = (TextView) headerView.findViewById(R.id.tvNombreCompleto);
        correoUsuario = (TextView) headerView.findViewById(R.id.tvCorreoUsuario);

        //SETEANDO LOS VALORES DE CORREO Y NOMBRE COMPLETO EN EL HEADER BUSCADOR
        nombreUsuario.setText("Bienvenido " + datosAlamcenados.getName_user());
        correoUsuario.setText(datosAlamcenados.getEmail());

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION
        id_user = tokenManager.getToken().getId_user();


    }


    private void registrarVeterinario(){

        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Registrando Veterinario...");
        progress.setCancelable(false);
        progress.show();

        String idUsuario = ""+id_user;
        String nomVeterinaria = edtCreaNomVet.getText().toString();
        String nomVeterinario = edtCrearVetNomVet.getText().toString();
        String direccionVet = edtCrearVetDir.getText().toString();
        String telefono = edtCrearVetTel.getText().toString();

       call= service.registrarVeterinario(
               idUsuario,
               nomVeterinaria,
               nomVeterinario,
               direccionVet,
               telefono

        );

        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                progress.dismiss();
                // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
                //Toast.makeText(EditarPerfilActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                //Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
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


                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                Toast.makeText(VeterinarioActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();
            }
        });


    }



    public void msjExito(String respuesta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(VeterinarioActivity.this,ListaVeterinariosActivity.class);
                        startActivity(i);
                        finish();
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


    public void msjErrores(String Error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡UPPS!")
                .setMessage("El veterinario no se pudo registrar por los siguientes motivos: \n\n"+Error+"")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
