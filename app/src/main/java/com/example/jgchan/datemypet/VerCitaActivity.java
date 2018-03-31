package com.example.jgchan.datemypet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Veterinario;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerCitaActivity extends MenuActivity {

    String id_user=null;
    private TokenManager tokenManager;
    private TextView nombreUsuario, correoUsuario;
    private AccessToken datosAlamcenados;
    Letrero ObjLetrero;

    ProgressDialog progress;
    Call<Citas> callDatosCita;
    Call<Success> callEliminarCita;
    Cita cita;
    Mascota mas;
    Veterinario vet;
    String idCita=null;
    int donde=-1;

    //Controllers de Cita
    TextView tvFechaCita,
             tvHoraCita,
             tvTipoCita,
             tvNotaCita;
    FloatingActionButton fabEliminarCita;

    //Controllers de item_mascota
    ImageView fotoMascota;
    TextView  tvNombreMascota;
    TextView tvSexMas;
    TextView tvEdadMas;
    TextView tvRazaView;

    //Controllers de item_veterinario
    TextView  tvNombreVeterinaria;
    TextView tvNombreVet;
    TextView tvTelVet;
    TextView idVeterinario;
    final private String urlBase="http://date-my-pet-mx.tk/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cita);

        ObjLetrero = new Letrero(this);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


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


        //Area donde se inflan los controladores a usar

        //Controllers de cita
        tvFechaCita = (TextView) findViewById(R.id.tvVerFechaCita);
        tvHoraCita = (TextView) findViewById(R.id.tvVerHoraCita);
        tvTipoCita = (TextView) findViewById(R.id.tvVerTipoCita);
        tvNotaCita = (TextView) findViewById(R.id.tvVerNotaCita);
        fabEliminarCita = (FloatingActionButton) findViewById(R.id.fabEliminarCita);

        fabEliminarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarCita();
            }
        });

        //Controllers de item_mascota
        fotoMascota = (ImageView) findViewById(R.id.lisImgMascota);
        tvNombreMascota= (TextView) findViewById(R.id.listNomMas);
        tvSexMas= (TextView) findViewById(R.id.listSexMas);
        tvEdadMas= (TextView) findViewById(R.id.listEdadMas);
        tvRazaView= (TextView) findViewById(R.id.listRazaMas);

        //Controllers de item_veterinario
        tvNombreVeterinaria= (TextView) findViewById(R.id.tvNombreVet);
        tvNombreVet= (TextView) findViewById(R.id.tvNombreVeterinario);
        tvTelVet= (TextView) findViewById(R.id.tvTelVet);
        idVeterinario= (TextView) findViewById(R.id.idVeterinario);


        //REcuperar idCita
        Bundle extras = getIntent().getExtras();
        idCita=extras.getString("idCita");
        donde=extras.getInt("donde");

        getInfoCita();
    }

    private void confirmarCita() {

        android.support.v7.app.AlertDialog.Builder builder = ObjLetrero.msjConfirmacion("Seguro que desea eliminar la cita");
         builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                eliminarCita();
             }
         });

        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();

    }

    private void eliminarCita() {
        progress = ObjLetrero.msjCargando("Eliminando cita ...");
        progress.show();

        callEliminarCita = service.eliminarCita(
                idCita
        );

        callEliminarCita.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                if(response.isSuccessful()){
                    progress.dismiss();
                    ObjLetrero.msjExito(response.body().getSuccess(),progress);


                }else{

                    ObjLetrero.msjErrorCarga(progress);
                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                ObjLetrero.msjErrorCarga(progress);
            }
        });
    }

    private void getInfoCita() {

        progress = ObjLetrero.msjCargando("Buscando informacion de la cita ...");
        progress.show();

        callDatosCita = service.visualizarCita(
                idCita
        );

        callDatosCita.enqueue(new Callback<Citas>() {
            @Override
            public void onResponse(Call<Citas> call, Response<Citas> response) {

                if(response.isSuccessful()){
                    cita = response.body().getCita();
                    mas = cita.getMascota();
                    vet = cita.getVeterinario();

                    tvFechaCita.setText(cita.parseFecha());
                    tvHoraCita.setText(cita.parseHora());
                    tvTipoCita.setText(cita.getTipoCita());
                    tvNotaCita.setText(cita.getCiNota());

                    //Controllers de item_mascota
                    Picasso.get().load(urlBase+mas.getMasFoto()).into(fotoMascota);
                    tvNombreMascota.setText(mas.getMasNombre());
                    tvSexMas.setText(mas.getMasSexo());
                    tvEdadMas.setText(""+mas.getMasEdad());
                    tvRazaView.setText(mas.getMasRaza());

                    //Controllers de item_veterinario
                    tvNombreVeterinaria.setText(vet.getVetNomVeterinaria());
                    tvNombreVet.setText(vet.getVetNombre());
                    tvTelVet.setText(vet.getVetTelefono());
                    progress.dismiss();

                }else{

                    ObjLetrero.msjErrorCarga(progress);
                }

            }

            @Override
            public void onFailure(Call<Citas> call, Throwable t) {
                ObjLetrero.msjErrorCarga(progress);
            }
        });
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
            Intent i = new Intent(VerCitaActivity.this, ActualizarCitasActivity.class);
            i.putExtra("idCita",idCita);
            startActivityForResult(i,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0 && resultCode == RESULT_OK){
            getInfoCita();
        }
    }


    @Override
    public void onBackPressed() {

        Intent i;
        if(donde==0)
            i = new Intent(VerCitaActivity.this, InicioActivity.class );
        else
            i = new Intent(VerCitaActivity.this, ListadoCitasActivity.class );
        startActivity(i);

        super.onBackPressed();
    }
}
