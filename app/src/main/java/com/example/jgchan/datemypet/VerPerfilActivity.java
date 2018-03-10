package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.citasAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Usuario;
import com.example.jgchan.datemypet.entidades.Usuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerPerfilActivity extends MenuActivity {
    Usuario usuario;
    List<Usuarios> usuarios;
    private AccessToken datosAlamcenados;

    Call<Usuarios> call;
    Call<Success> call2;
    apiService service;
    String id_user=null;
    private TokenManager tokenManager;
    TextView txtNombreUsuario,txtVerPerfilNombreUsuario, txtDireccionUsuario,txtTelefonoUsuario,txtCelularUsuario, txtCorreoUsuario;
    TextView nombreUsuario, correoUsuario;
    ProgressDialog progress;
    private SwipeRefreshLayout lyRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        txtNombreUsuario = (TextView) findViewById(R.id.txtNombreUsuario);
        txtVerPerfilNombreUsuario=(TextView) findViewById(R.id.txtVerPerfilNombreUsuario);
        txtDireccionUsuario=(TextView) findViewById(R.id.txtDireccionUsuario);
        txtTelefonoUsuario=(TextView) findViewById(R.id.txtTelefonoUsuario);
        txtCelularUsuario=(TextView) findViewById(R.id.txtCelularUsuario);
        txtCorreoUsuario=(TextView) findViewById(R.id.txtCorreoUsuario);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msjConfirmacion();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //ANEXANDO EL REFRRESH

        lyRefresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        //OBTENIENDO EL PADRE HEADER
        View headerView = navigationView.getHeaderView(0);

        //BUSCANDO CONTROLLERS MEDIANTE EL PADRE QUE LOS CONTIENE
        nombreUsuario=(TextView) headerView.findViewById(R.id.tvNombreCompleto);
        correoUsuario=(TextView) headerView.findViewById(R.id.tvCorreoUsuario);

        //SETEANDO LOS VALORES DE CORREO Y NOMBRE COMPLETO EN EL HEADER BUSCADOR
        nombreUsuario.setText("Bienvenido " +datosAlamcenados.getName_user());
        correoUsuario.setText(datosAlamcenados.getEmail());

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();

        usuarios();

        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        usuarios();
                        lyRefresh.setRefreshing(false); //Terminando el refresh
                    }
                }
        );


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
            Intent i = new Intent(this, EditarPerfilActivity.class);
            startActivityForResult(i,1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public  void  usuarios(){



            progress = new ProgressDialog(this);
            progress.setTitle("Cargando");
            progress.setMessage("Buscando usuario, por favor espere...");
            progress.setCancelable(false);
            progress.show();



        call= service.usuario(
                id_user
        );

        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {

                //progress.dismiss();
                // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
               // Toast.makeText(VerPerfilActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                //Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    String tel;
                    progress.dismiss();
                    usuario=response.body().getUsuario();

                    txtNombreUsuario.setText(usuario.getUsername());
                    txtVerPerfilNombreUsuario.setText(usuario.getNombre());
                    txtDireccionUsuario.setText(usuario.getDireccion());

                    if(usuario.getTelefono()!=null)tel=usuario.getTelefono();
                    else tel= "-----------";

                    txtTelefonoUsuario.setText(tel);
                    txtCelularUsuario.setText(usuario.getCelular());
                    txtCorreoUsuario.setText(usuario.getCorreo());



                }else{
                    progress.dismiss();
                     Toast.makeText(VerPerfilActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                //msjErrores("Error en la conexión");
            }
        });

    }

    public void msjConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Advertencia!")
                .setMessage("¿Seguro que desea eliminar su cuenta?")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Eliminar cuenta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliminar(id_user);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public  void eliminar(String id_user){


        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Buscando usuario, por favor espere...");
        progress.setCancelable(false);
        progress.show();



        call2= service.eliminarCuenta(
                id_user
        );

        call2.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                progress.dismiss();
                // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
                Toast.makeText(VerPerfilActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                //Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(VerPerfilActivity.this);
                    builder.setTitle("¡Vuelva Pronto!")
                            .setMessage("Su cuenta se dio de baja con éxito")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    tokenManager.eliminoCuenta();
                                    Intent i = new Intent(VerPerfilActivity.this, IngresarActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();


                }else{
                    progress.dismiss();
                    Toast.makeText(VerPerfilActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());
                Toast.makeText(VerPerfilActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                progress.dismiss();
                //msjErrores("Error en la conexión");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK){
            usuarios();
        }


    }

}
