package com.example.jgchan.datemypet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import com.example.jgchan.datemypet.adaptadores.mascotasAdapter;
import com.example.jgchan.datemypet.adaptadores.veterinariosAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.Veterinario;
import com.example.jgchan.datemypet.entidades.Veterinarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class ListaVeterinariosActivity extends MenuActivity {

    ListView lista_veterinario;
    List<Veterinario> veterinarios;
    private static final String TAG = "ListaVeterinariosActivity";
    private TextView mensajeVacio, nombreUsuario, correoUsuario;
    private SwipeRefreshLayout lyRefresh;
    private AccessToken datosAlamcenados;

    Call<Veterinarios> call;
    apiService service;
    String id_user = null;
    private TokenManager tokenManager;
    ProgressDialog progress;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_veterinarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        1);
            }
        }

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

        //OBTENIENDO EL PADRE HEADER
        View headerView = navigationView.getHeaderView(0);

        //BUSCANDO CONTROLLERS MEDIANTE EL PADRE QUE LOS CONTIENE
        nombreUsuario = (TextView) headerView.findViewById(R.id.tvNombreCompleto);
        correoUsuario = (TextView) headerView.findViewById(R.id.tvCorreoUsuario);

        //SETEANDO LOS VALORES DE CORREO Y NOMBRE COMPLETO EN EL HEADER BUSCADOR
        nombreUsuario.setText("Bienvenido " + datosAlamcenados.getName_user());
        correoUsuario.setText(datosAlamcenados.getEmail());


        //ANEXANDO EL REFRRESH

        lyRefresh = (SwipeRefreshLayout) findViewById(R.id.refreshListaVeterinario);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        fab = (FloatingActionButton) findViewById(R.id.fabCrearNuevoVeterinario);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListaVeterinariosActivity.this,VeterinarioActivity.class);
                startActivity(i);
                finish();
            }
        });


        mensajeVacio = (TextView) findViewById(R.id.tvVeterinarioVacio); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

        mensajeVacio.setVisibility(View.VISIBLE); //LO HACEMOS VISIBLE

        lista_veterinario = (ListView) findViewById(R.id.lista_veterinario); //DAMOS DE ALTA EL CONTROLADOR LISTVIEW

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user = tokenManager.getToken().getId_user();


        //HABILITAMOS EL ONCLICK PARA CADA ITEM DE LA LISTVIEW
        lista_veterinario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                TextView idVeterinario = (TextView) view.findViewById(R.id.idVeterinario);
                TextView nombreVeterinaria = (TextView) view.findViewById(R.id.tvNombreVet);

                Intent i = new Intent(ListaVeterinariosActivity.this, VerVeterinarioActivity.class);
                i.putExtra("idVeterinario",idVeterinario.getText().toString());
                i.putExtra("nombre",nombreVeterinaria.getText().toString());
                startActivityForResult(i, 1);


            }
        });

        lista_veterinario.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idVeterinario = (TextView) view.findViewById(R.id.idVeterinario);
                final ImageView iconoTel = (ImageView) view.findViewById(R.id.iconoLisVetTel);
                final TextView telefono = (TextView) view.findViewById(R.id.tvTelVet);


             //   Toast.makeText(ListaVeterinariosActivity.this, "" + idVeterinario, Toast.LENGTH_SHORT).show();

                PopupMenu popup = new PopupMenu(ListaVeterinariosActivity.this, iconoTel);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.lista_veterinarios, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.pop_menu_vet_llamar:

                                llamarVeterinario(telefono.getText().toString());

                                break;

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                return true;
            }
        });

        lista_veterinario.setOnScrollListener(new AbsListView.OnScrollListener() {

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

        getVeterinarios(false);


        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        getVeterinarios(true); //Llamando al metodo que busca todas las citas

                    }
                }
        );


    }

    private void getVeterinarios(final boolean estaRefrescando) {

        if (!estaRefrescando) {
            progress = new ProgressDialog(this);
            progress.setTitle("Cargando");
            progress.setMessage("Buscando sus veterinarios, por favor espere...");
            progress.setCancelable(false);
            progress.show();
        }


        call = service.misveterinarios(
                id_user
        );

        call.enqueue(new Callback<Veterinarios>() {
            @Override
            public void onResponse(Call<Veterinarios> call, Response<Veterinarios> response) {


                //  Log.w(TAG, "onResponse: "+response);
                if (response.isSuccessful()) {
                    progress.dismiss();
                    veterinarios = response.body().getVeterinarios();
                    veterinariosAdapter adapter = new veterinariosAdapter(ListaVeterinariosActivity.this, veterinarios);
                    if (veterinarios.size() <= 0) {
                        mensajeVacio.setVisibility(View.VISIBLE);
                    } else {
                        mensajeVacio.setVisibility(View.INVISIBLE);
                    }

                    lista_veterinario.setAdapter(adapter);

                    if (estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh

                } else {
                    progress.dismiss();
                    if (estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh
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
            public void onFailure(Call<Veterinarios> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();

                Toast.makeText(ListaVeterinariosActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            getVeterinarios(false);
        }

    }

    private void llamarVeterinario(String vetTelefono) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + vetTelefono));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);

    }

}
