package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.citasAdapter;
import com.example.jgchan.datemypet.adaptadores.mascotasAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Mascotas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaMascotasActivity extends MenuActivity{


    ListView lista_mascota;
    List<Mascota> mascotas ;
    private static final String TAG = "ListasMascotasctivity";
    Call<Mascotas> call;
    apiService service;
    private SwipeRefreshLayout lyRefresh;
    String id_user=null;
    private TokenManager tokenManager;
    private TextView mensajeVacio,nombreUsuario, correoUsuario;
    private AccessToken datosAlamcenados;
    ProgressDialog progress;
    FloatingActionButton fab;
    Letrero objLetrero = new Letrero(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mascotas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();


        fab = (FloatingActionButton) findViewById(R.id.fabCrearNuevaMascota);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(ListaMascotasActivity.this,CrearMascotactivity.class);
                    startActivity(i);
                    finish();
            }
        });

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

        lyRefresh = (SwipeRefreshLayout)findViewById(R.id.refreshListaMascota);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        mensajeVacio=(TextView)findViewById(R.id.tvMascotaVacio); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

        mensajeVacio.setVisibility(View.VISIBLE); //LO HACEMOS VISIBLE

        lista_mascota = (ListView)findViewById(R.id.lista_mascotas); //DAMOS DE ALTA EL CONTROLADOR LISTVIEW

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        //HABILITAMOS EL ONCLICK PARA CADA ITEM DE LA LISTVIEW
        lista_mascota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                TextView idMascota =(TextView)view.findViewById(R.id.listIdMasc);
                TextView nombreMascota =(TextView)view.findViewById(R.id.listNomMas);

                String nombre = nombreMascota.getText().toString();

                //Toast.makeText(ListaMascotasActivity.this, ""+idMascota.getText(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ListaMascotasActivity.this, InfoMascotaActivity.class);
                i.putExtra("idMascota",idMascota.getText());
                i.putExtra("nombre",nombre);
                i.putExtra("donde",1);
                startActivity(i);
                finish();

            }
        });

        lista_mascota.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0)
                    // Puedes ocultarlo simplemente
                    //fab.hide();
                    // o añadir la animación deseada
                    fab.animate().translationY(fab.getHeight() +
                            getResources().getDimension(R.dimen.fab_margin))
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(100); // Cambiar al tiempo deseado
                else if (firstVisibleItem == 0)
                    //fab.show();
                    fab.animate().translationY(0)
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(100); // Cambiar al tiempo deseado
            }

        });


        lista_mascota.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

               final TextView idMascota =(TextView)view.findViewById(R.id.listIdMasc);
               final TextView nombreMascota =(TextView)view.findViewById(R.id.listNomMas);
                TextView letrero =(TextView)view.findViewById(R.id.tvLetreroItemNomMas);



                PopupMenu popup = new PopupMenu(ListaMascotasActivity.this, letrero);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_popup_vac_y_his, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i;

                        switch (item.getItemId()) {
                            case R.id.popup_vacunas:
                                    i = new Intent(ListaMascotasActivity.this, VerVacunasActivity.class);
                                    i.putExtra("idMascota",idMascota.getText().toString());
                                    i.putExtra("donde",1);
                                    startActivity(i);
                            break;

                            case R.id.popup_historial:

                                    i = new Intent(ListaMascotasActivity.this, HistorialMedicoActivity.class);
                                    i.putExtra("idMascota",idMascota.getText().toString());
                                    i.putExtra("nombreMascota",nombreMascota.getText().toString());
                                    i.putExtra("donde",1);
                                    startActivity(i);

                                break;

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                return true;
            }
        });

        getMascotas(false);


        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        getMascotas(true); //Llamando al metodo que busca todas las citas

                    }
                }
        );


    }

    public  void  getMascotas(final boolean estaRefrescando){

        if(!estaRefrescando) {
            progress = objLetrero.msjCargando("Buscando mascotas, por favor espere...");
            progress.show();
        }


        call= service.mismascotas(
                id_user
        );

        call.enqueue(new Callback<Mascotas>() {
            @Override
            public void onResponse(Call<Mascotas> call, Response<Mascotas> response) {


                Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    mascotas=response.body().getMascotas();
                    mascotasAdapter adapter = new mascotasAdapter(ListaMascotasActivity.this, mascotas);
                    if(mascotas.size()<=0){
                        mensajeVacio.setVisibility(View.VISIBLE);
                    }else{
                        mensajeVacio.setVisibility(View.INVISIBLE);
                    }

                    lista_mascota.setAdapter(adapter);

                    if(estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh

                }else{
                    objLetrero.msjErrorCarga(progress);
                    if(estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh
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
            public void onFailure(Call<Mascotas> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());

                objLetrero.msjErrorCarga(progress);
            }
        });
    }


}
