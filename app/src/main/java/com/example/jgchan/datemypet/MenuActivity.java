package com.example.jgchan.datemypet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.citasAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.ParseoToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener {

    ListView lista_citas;
    List<Cita> citas ;
    private static final String TAG = "InicioActivity";
    private TextView mensajeVacio,nombreUsuario, correoUsuario;
    private SwipeRefreshLayout lyRefresh;
    private AccessToken datosAlamcenados;

    Call<Citas> call;
    apiService service;
    String id_user=null;
    private TokenManager tokenManager;
    public ProgressDialog progress;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
         datosAlamcenados= tokenManager.getToken();

        //ANEXANDO EL TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ANEXANDO EL MENU DESPLEGABLE

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

        //ANEXANDO EL REFRRESH

        lyRefresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        mensajeVacio=(TextView)findViewById(R.id.tvCitasVacio); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

        mensajeVacio.setVisibility(View.VISIBLE); //LO HACEMOS VISIBLE

        lista_citas = (ListView)findViewById(R.id.lista_citas); //DAMOS DE ALTA EL CONTROLADOR LISTVIEW

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        //HABILITAMOS EL ONCLICK PARA CADA ITEM DE LA LISTVIEW
        lista_citas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                TextView tit =(TextView)view.findViewById(R.id.idCita);
                Toast.makeText(MenuActivity.this, "Titulo: "+ tit.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        citas(false);


        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        citas(true); //Llamando al metodo que busca todas las citas
                        lyRefresh.setRefreshing(false); //Terminando el refresh
                    }
                }
        );


    }

    private  void  citas(boolean estaRefrescando){

        if(!estaRefrescando) {
            progress = new ProgressDialog(this);
            progress.setTitle("Cargando");
            progress.setMessage("Buscando citas, por favor espere...");
            progress.setCancelable(false);
            progress.show();
        }


        call= service.miscitas(
                id_user
        );

        call.enqueue(new Callback<Citas>() {
            @Override
            public void onResponse(Call<Citas> call, Response<Citas> response) {

                //progress.dismiss();
                // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
                //Toast.makeText(MenuActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                       progress.dismiss();
                    citas=response.body().getCitas();
                    citasAdapter adapter = new citasAdapter(MenuActivity.this, citas);
                    if(citas.size()<=0){
                        mensajeVacio.setVisibility(View.VISIBLE);
                    }else{
                        mensajeVacio.setVisibility(View.INVISIBLE);
                    }

                    lista_citas.setAdapter(adapter);

                }else{
                    progress.dismiss();
                    if (response.code() == 421) {
                        //mensaje();
                        //Toast.makeText(IngresarActivity.this, "Credenciales no correspondientes", Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 420) {
                        //handleErrors(response.errorBody());
                        //Toast.makeText(IngresarActivity.this, "Credenciales no correspondientes", Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 401) {
                        // ApiError apiError = Utils.converErrors(response.errorBody());
                        //handleErrors(response.errorBody());
                        //
                    }
                }

            }

            @Override
            public void onFailure(Call<Citas> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                Toast.makeText(MenuActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {

            if(!(this.getLocalClassName().equalsIgnoreCase("InicioActivity"))){
                Intent i = new Intent(this,InicioActivity.class);
                startActivity(i);
                finish();
            }

        }else if(id == R.id.nav_perfil){

            if(!(this.getLocalClassName().equalsIgnoreCase("VerPerfilActivity"))){
                Intent i = new Intent(this, VerPerfilActivity.class);
                startActivity(i);
                finish();
            }

        }else if (id == R.id.nav_mascota) {

            if(!(this.getLocalClassName().equalsIgnoreCase("ListaMascotasActivity"))){
                Intent i = new Intent(this, ListaMascotasActivity.class);
                startActivity(i);
                finish();
            }

        }else if (id == R.id.nav_veterinarios) {

            if(!(this.getLocalClassName().equalsIgnoreCase("ListaVeterinariosActivity"))){
                Intent i = new Intent(this, ListaVeterinariosActivity.class);
                startActivity(i);
                finish();
            }

        }
        else if (id == R.id.nav_hack) {

            Intent i = new Intent(this,HackActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Oye!!! Mira estoy usando Date My Pet. Decargalo http://date-my-pet-mx.tk/Download");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_send) {

            String email ="contactodatemypet@gmail.com"; /* Your email address here */
            String subject =  "Contactando a Date My Pet";/* Your subject here */
            String body = "Hola Date My Pet ....";/* Your body here */
            String chooserTitle = "Contactando a Date My Pet";/* Your chooser title here */

            Uri uri = Uri.parse("mailto:" + email)
                    .buildUpon()
                    .appendQueryParameter("subject", subject)
                    .appendQueryParameter("body", body)
                    .build();

           // Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
           // startActivity(Intent.createChooser(emailIntent, chooserTitle));


            String mailto = "mailto: contactodatemypet@ate-my-pet-mx.tk"+
                    "?cc=" + "support@date-my-pet-mx.tk" +
                    "&subject=" + Uri.encode(subject) +
                    "&body=" + Uri.encode(body);

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(mailto));

            try {
                startActivity(emailIntent);
            } catch (ActivityNotFoundException e) {
                //TODO: Handle case where no email app is available
            }


        } else if (id == R.id.nav_salir) {
            tokenManager.deleteToken();
            Intent i = new Intent(this, IngresarActivity.class);
            startActivity(i);
            finish();
        }


        if (progress!=null){
            if (progress.isShowing()){
                progress.dismiss();
            }
        }

        if (call!=null){
            if(call.isExecuted()){
                call.cancel();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
