package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Usuario;
import com.example.jgchan.datemypet.entidades.Usuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Usuario usuario;
    private AccessToken datosAlamcenados;

    Call<Usuarios> call;
    Call<Success> call2;
    Button btnEditarUsuario;
    Success success;
    String respuesta;
    apiService service;
    String id_user=null;
    private TokenManager tokenManager;
    EditText txtEditNombreUsuario, txtEditNombreCompleto, txtEditDireccionUsuario, txtEditTelefonoUsuario, txtEditCelularUsuario, txtEditCorreo;
    TextView nombreUsuario, correoUsuario;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        txtEditNombreUsuario=(EditText)findViewById(R.id.txtEditNombreUsuario);
        txtEditNombreCompleto=(EditText)findViewById(R.id.txtEditNombreCompleto);
        txtEditDireccionUsuario=(EditText)findViewById(R.id.txtEditDireccionUsuario);
        txtEditTelefonoUsuario=(EditText)findViewById(R.id.txtEditTelefonoUsuario);
        txtEditCelularUsuario=(EditText)findViewById(R.id.txtEditCelularUsuario);
        txtEditCorreo=(EditText)findViewById(R.id.txtEditCorreo);
        btnEditarUsuario=(Button)findViewById(R.id.btnEditarUsuario);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        btnEditarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualizar();

            }
        });

        traerDatos();
    }

    private void actualizar() {

        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Buscando usuario, por favor espere...");
        progress.setCancelable(false);
        progress.show();

        /*
        @Field("username") String username ,
        @Field("nombre") String nombre,
        @Field("correo") String correo,
        @Field("direccion") String direccion,
        @Field("telefono") String telefono,
        @Field("celular") String celular);
*/
        call2= service.actualizar(
                ""+id_user,
                txtEditNombreUsuario.getText().toString(),
                txtEditNombreCompleto.getText().toString(),
                txtEditCorreo.getText().toString(),
                txtEditDireccionUsuario.getText().toString(),
                txtEditTelefonoUsuario.getText().toString(),
                txtEditCelularUsuario.getText().toString()
                );

        call2.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                //progress.dismiss();
                // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
                //Toast.makeText(EditarPerfilActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                //Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    respuesta=response.body().getSuccess();

                    tokenManager.guardarActualizacion( txtEditNombreUsuario.getText().toString(), txtEditNombreCompleto.getText().toString(),txtEditCorreo.getText().toString());

                    msjExito(respuesta);


                }else{
                    progress.dismiss();
                    Toast.makeText(EditarPerfilActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                //msjErrores("Error en la conexión");
            }
        });


    }


    private void traerDatos() {

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
                //Toast.makeText(EditarPerfilActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                //Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    String tel=null;
                    progress.dismiss();
                    usuario=response.body().getUsuario();

                    txtEditNombreUsuario.setText(usuario.getUsername());
                    txtEditNombreCompleto.setText(usuario.getNombre());
                    txtEditDireccionUsuario.setText(usuario.getDireccion());

                    if(usuario.getTelefono()!=null)tel=usuario.getTelefono();


                    txtEditTelefonoUsuario.setText(tel);
                    txtEditCelularUsuario.setText(usuario.getCelular());
                    txtEditCorreo.setText(usuario.getCorreo());



                }else{
                    progress.dismiss();
                    Toast.makeText(EditarPerfilActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = getIntent();
            setResult(RESULT_OK,i);
            finish();
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
