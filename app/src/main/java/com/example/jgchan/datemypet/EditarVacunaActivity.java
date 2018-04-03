package com.example.jgchan.datemypet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.spinnerMascAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Vacuna;
import com.example.jgchan.datemypet.entidades.Vacunas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarVacunaActivity extends MenuActivity {

    Calendar myCalendar;
    EditText edtEditarNomVac,edtEditarFechaVac,edtEditarNotaVac;
    Spinner spinnerListadoMascota;
    String Fecha=null;
    Button btnEditarVacuna;
    FloatingActionButton fabEditarFechaVac;

    String respuesta;

    String id_user=null;
    private TokenManager tokenManager;
    private TextView nombreUsuario, correoUsuario;
    private AccessToken datosAlamcenados;
    ProgressDialog progress;

    List<Mascota> mascotas;
    Call<Mascotas> call;
    Call<Vacunas> callInfoVacuna;
    Call<Success> callRegistroVacuna;
    Vacuna vacuna;

    String idVacuna;
    int idMascota;
    String idsMas[];

    Letrero objLetrero = new Letrero(this);

    ActualizarCitasActivity actActCita = new ActualizarCitasActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_vacuna);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idVacuna = extras.getString("idVacuna");
            idMascota = Integer.parseInt(extras.getString("idMascota"));

        }

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myCalendar = Calendar.getInstance();

        edtEditarFechaVac= (EditText) findViewById(R.id.edtEditarFechaVac);
        edtEditarNomVac= (EditText) findViewById(R.id.edtEditarNomVac);
        edtEditarNotaVac= (EditText) findViewById(R.id.edtEditarNotaVac);
        btnEditarVacuna = (Button) findViewById(R.id.btnEditarVacuna);
        fabEditarFechaVac=(FloatingActionButton)findViewById(R.id.fabEditarFechaVac);


        edtEditarFechaVac.setEnabled(false);
        edtEditarFechaVac.setInputType(InputType.TYPE_CLASS_TEXT);
        edtEditarFechaVac.setFocusable(false);

        btnEditarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualizarVacuna();

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        fabEditarFechaVac.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new DatePickerDialog(EditarVacunaActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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


        getMascotas();



    }

    private void actualizarVacuna() {

        progress=objLetrero.msjCargando("Editando vacuna...");
        progress.show();

        callRegistroVacuna= service.editarVacuna(
                ""+idVacuna,
                ""+obtenerIdMascota(),
                edtEditarNomVac.getText().toString(),
                Fecha,
                edtEditarNotaVac.getText().toString()
        );

        callRegistroVacuna.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                if(response.isSuccessful()){
                    respuesta=response.body().getSuccess();
                    objLetrero.msjExitoFinish(respuesta,progress);

                }else{
                    if(response.code()==401){
                        objLetrero.handleErrors(response.errorBody(),"No se pudo registrar la vacuna",progress);
                    }else {
                        objLetrero.msjErrorCarga(progress);
                        Log.e("Error Server",response.message()+ " "+response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {

                objLetrero.msjErrorCarga(progress);
            }
        });



    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        String formatoFechaGuardar = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfGuardarFecha = new SimpleDateFormat(formatoFechaGuardar, Locale.US);
        Fecha = sdfGuardarFecha.format(myCalendar.getTime());
        edtEditarFechaVac.setText(sdf.format(myCalendar.getTime()));
    }


    public  void  getMascotas(){

        progress=objLetrero.msjCargando("Por favor espere...");
        progress.show();

        call= service.mismascotas(
                id_user
        );

        call.enqueue(new Callback<Mascotas>() {
            @Override
            public void onResponse(Call<Mascotas> call, Response<Mascotas> response) {

                if(response.isSuccessful()){
                    mascotas=response.body().getMascotas();

                    if(mascotas.size()==0){
                        validacion("mascotas");
                        return;
                    }


                    String nombresMas[]= new String[mascotas.size()];
                    String fotosMas[]= new String[mascotas.size()];
                    idsMas= new String[mascotas.size()];
                    int cont =0;

                    for (Mascota mas:mascotas) {
                        nombresMas[cont]= mas.getMasNombre();
                        fotosMas[cont]= mas.getMasFoto();
                        idsMas[cont]= ""+mas.getId();
                        cont++;

                    }

                    spinnerListadoMascota = (Spinner) findViewById(R.id.spnIdMascota);
                    spinnerMascAdapter adapter = new spinnerMascAdapter(EditarVacunaActivity.this, R.layout.itemsinnermasc, nombresMas,fotosMas,idsMas);
                    spinnerListadoMascota.setAdapter(adapter);

                    getInfoVacuna();

                }else{
                    objLetrero.msjErrorCarga(progress);
                }

            }

            @Override
            public void onFailure(Call<Mascotas> call, Throwable t) {


                objLetrero.msjErrorCarga(progress);

                Toast.makeText(EditarVacunaActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });
    }

    public void getInfoVacuna() {

        callInfoVacuna= service.verVacuna(
                idVacuna
        );

        callInfoVacuna.enqueue(new Callback<Vacunas>() {
            @Override
            public void onResponse(Call<Vacunas> call, Response<Vacunas> response) {

                if(response.isSuccessful()){

                    vacuna =response.body().getVacuna();

                    edtEditarNomVac.setText(vacuna.getVaNombre());
                    edtEditarNotaVac.setText(vacuna.getVaNota());
                    spinnerListadoMascota.setSelection(actActCita.getSelectIdMascota(idMascota,mascotas,idsMas));
                    setearFecha(vacuna.getVaFecha());

                    progress.dismiss();
                }else{
                    objLetrero.msjErrorCarga(progress);
                }

            }

            @Override
            public void onFailure(Call<Vacunas> call, Throwable t) {


                objLetrero.msjErrorCarga(progress);

                Toast.makeText(EditarVacunaActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });


    }

    private void setearFecha(String vaFecha) {

            String fecha[] = vaFecha.split("-");
            myCalendar.set(Calendar.YEAR,Integer.parseInt(fecha[0]));
            myCalendar.set(Calendar.MONTH,Integer.parseInt(fecha[1])-1);
            myCalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(fecha[2]));
            updateLabel();


    }

    private void validacion(final String msj) {

        String Boton = null;

        switch(msj){
            case "mascotas":
                Boton ="Crear Mascota";
                break;

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡UPPS!")
                .setMessage("No cuenta con "+msj+", por favor registre para continuar")
                .setCancelable(false)
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton(Boton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch(msj){
                            case "mascotas":
                                Intent i = new Intent(EditarVacunaActivity.this, CrearMascotactivity.class);
                                startActivity(i);
                                break;

                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    public String obtenerIdMascota(){
        View  vv= spinnerListadoMascota.getSelectedView();
        TextView nombre = (TextView)vv.findViewById(R.id.spnListadoIdMascota);

        return nombre.getText().toString();
    }


}
