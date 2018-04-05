package com.example.jgchan.datemypet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.mascotasAdapter;
import com.example.jgchan.datemypet.adaptadores.vacunasAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Vacuna;
import com.example.jgchan.datemypet.entidades.Vacunas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerVacunasActivity extends MenuActivity {

    FloatingActionButton fab;

    private TextView mensajeVacio,nombreUsuario, correoUsuario;
    private SwipeRefreshLayout lyRefresh;
    private AccessToken datosAlamcenados;
    apiService service;
    String id_user=null;
    private TokenManager tokenManager;
    public ProgressDialog progress;
    Call<Vacunas> callListaVacunas;
    Call<Success> callEliminarVacuna;

    Dialog customDialog = null;

    List<Vacuna> vacunas;
    ListView lista_vacunas;
    private Letrero objLetrero = new Letrero(this);
    String idMascota,respuesta;
    int donde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_vacunas);


        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idMascota=extras.getString("idMascota");
            donde=extras.getInt("donde");
        }

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


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(VerVacunasActivity.this,RegistroVacunasActivity.class);
              i.putExtra("idMascota",idMascota);
              startActivityForResult(i,1);
            }
        });


        mensajeVacio=(TextView)findViewById(R.id.tvVacunasVacioIndex); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

        mensajeVacio.setVisibility(View.VISIBLE); //LO HACEMOS VISIBLE

        lista_vacunas = (ListView)findViewById(R.id.lista_vacunas); //DAMOS DE ALTA EL CONTROLADOR LISTVIEW

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        //HABILITAMOS EL ONCLICK PARA CADA ITEM DE LA LISTVIEW
        lista_vacunas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                TextView idVacuna =(TextView)view.findViewById(R.id.idVacuna);
                TextView titulo =(TextView)view.findViewById(R.id.tvLisNomVac);
                TextView fecha =(TextView)view.findViewById(R.id.tvLisFechaAplicVac);
                TextView nota =(TextView)view.findViewById(R.id.tvNotaVac);

               // con este tema personalizado evitamos los bordes por defecto
                customDialog = new Dialog(VerVacunasActivity.this,R.style.Theme_Dialog_Translucent);
                //deshabilitamos el título por defecto
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //obligamos al usuario a pulsar los botones para cerrarlo
                customDialog.setCancelable(false);
                //establecemos el contenido de nuestro dialog
                customDialog.setContentView(R.layout.dialog_container);

                TextView titDialog = (TextView) customDialog.findViewById(R.id.titulo);
                titDialog.setText(titulo.getText().toString());

                TextView fechaDialog = (TextView) customDialog.findViewById(R.id.tvVerVacunaFecha);
                fechaDialog.setText(fecha.getText().toString());

                TextView contenido = (TextView) customDialog.findViewById(R.id.contenido);
                contenido.setText(nota.getText().toString());

                ((Button) customDialog.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });


                customDialog.show();
            }

        });

        lista_vacunas.setOnScrollListener(new AbsListView.OnScrollListener() {

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

        lista_vacunas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView idVacuna = (TextView) view.findViewById(R.id.idVacuna);
                TextView titulo = (TextView) view.findViewById(R.id.tvLisNomVac);
                TextView fecha = (TextView) view.findViewById(R.id.tvLisFechaAplicVac);
                TextView nota = (TextView) view.findViewById(R.id.tvNotaVac);


                PopupMenu popup = new PopupMenu(VerVacunasActivity.this, titulo);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.ver_vacunas, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i;

                        switch (item.getItemId()) {
                            case R.id.popup_menu_editar_vac:

                                    i = new Intent(VerVacunasActivity.this, EditarVacunaActivity.class);
                                    i.putExtra("idMascota",idMascota);
                                    i.putExtra("idVacuna",idVacuna.getText().toString());
                                    startActivityForResult(i,0);

                                break;

                            case R.id.popup_menu_eliminar_vac:

                                msjValidacion(idVacuna.getText().toString());

                                break;

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu

                return true;
            }

        });


        getVacunas(false);

        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getVacunas(true);
                    }
                }
        );

    }

    private void msjValidacion(final String idVacuna){

        AlertDialog.Builder builder = objLetrero.msjConfirmacion("Seguro que desea eliminar la vacuna");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    eliminarVacuna(idVacuna);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void eliminarVacuna(String idVacuna) {

        callEliminarVacuna= service.eliminarVacuna(
                idVacuna
        );

        callEliminarVacuna.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                if(response.isSuccessful()){

                    respuesta=response.body().getSuccess();
                    objLetrero.msjExitoSinRet(respuesta,progress);
                    getVacunas(false);

                }else{
                    Log.e("Error Server","Error: "+response.message()+ " codigo: "+response.code());
                    objLetrero.msjErrorCarga(progress);
                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                objLetrero.msjErrorCarga(progress);
            }
        });

    }

    private void getVacunas(final boolean estaRefrescando) {

        if(!estaRefrescando) {
            progress = objLetrero.msjCargando("Buscando las vacunas de tu mascota, por favor espere...");
            progress.show();
        }

        callListaVacunas= service.verListadoVacuna(
            idMascota
        );

        callListaVacunas.enqueue(new Callback<Vacunas>() {
            @Override
            public void onResponse(Call<Vacunas> call, Response<Vacunas> response) {
                if(response.isSuccessful()){
                    progress.dismiss();
                    vacunas=response.body().getVacunas();
                    vacunasAdapter adapter = new vacunasAdapter(VerVacunasActivity.this, vacunas);
                    if(vacunas.size()<=0){
                        mensajeVacio.setVisibility(View.VISIBLE);
                    }else{
                        mensajeVacio.setVisibility(View.INVISIBLE);
                    }

                    lista_vacunas.setAdapter(adapter);

                    if(estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh

                }else{
                    objLetrero.msjErrorCarga(progress);
                    if(estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh
                }

            }

            @Override
            public void onFailure(Call<Vacunas> call, Throwable t) {
               objLetrero.msjErrorCarga(progress);
               if(estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0 && resultCode == RESULT_OK){
                getVacunas(false);
        }

        if(requestCode == 1 && resultCode == RESULT_OK){
            getVacunas(false);
        }


    }

}
