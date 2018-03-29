package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.citasAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Citas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoCitasActivity extends MenuActivity {

    ListView lista_citas;
    List<Cita> citas ;
    private static final String TAG = "InicioActivity";
    private TextView mensajeVacio,nombreUsuario, correoUsuario;
    private SwipeRefreshLayout lyRefresh;
    private AccessToken datosAlamcenados;
    FloatingActionButton fabAgregarCita;

    Call<Citas> call;
    apiService service;
    String id_user=null;
    private TokenManager tokenManager;
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_citas);

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

        fabAgregarCita = (FloatingActionButton)findViewById(R.id.fabAgregarCita);

       mensajeVacio=(TextView)findViewById(R.id.tvCitasVacioIndex); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

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
                Toast.makeText(ListadoCitasActivity.this, "Titulo: "+ tit.getText().toString(), Toast.LENGTH_SHORT).show();

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

        fabAgregarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListadoCitasActivity.this, RegistroCitasActivity.class);
                startActivity(i);
            }
        });

    }

    private  void  citas(boolean estaRefrescando){

        if(!estaRefrescando) {
            progress = new ProgressDialog(ListadoCitasActivity.this);
            progress.setTitle("Cargando");
            progress.setMessage("Buscando citas, por favor espere...");
            progress.setCancelable(false);
            progress.show();
        }


        call= service.miscitascompletas(
                ""+id_user
        );

        call.enqueue(new Callback<Citas>() {
            @Override
            public void onResponse(Call<Citas> call, Response<Citas> response) {

                Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    citas=response.body().getCitas();
                    citasAdapter adapter = new citasAdapter(ListadoCitasActivity.this, citas);
                    if(citas.size()<=0){
                       mensajeVacio.setVisibility(View.VISIBLE);
                    }else{
                       mensajeVacio.setVisibility(View.INVISIBLE);
                    }

                    lista_citas.setAdapter(adapter);

                }else{
                    Toast.makeText(ListadoCitasActivity.this, "Ocurrió un "+ response.message()+"\n "+response.code(), Toast.LENGTH_LONG).show();

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
                Toast.makeText(ListadoCitasActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });
    }


}
