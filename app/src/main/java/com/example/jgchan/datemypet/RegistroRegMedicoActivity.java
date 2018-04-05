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

import com.example.jgchan.datemypet.adaptadores.spinnerVeteAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Veterinario;
import com.example.jgchan.datemypet.entidades.Veterinarios;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroRegMedicoActivity extends MenuActivity {

    Calendar myCalendar;
    EditText edtNomPerca,edtFechaPerca,edtNotaPerca;
    Spinner spinnerListadoVeterinario;
    String Fecha=null;
    Button btnAgregarRegistro;
    FloatingActionButton fabCrearFechaReg;

    List<Veterinario> veterinarios;
    Call<Veterinarios> call;
    Call<Success> callRegistroReg;

    String idMascota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_reg_medico);

        objLetrero = new Letrero(this);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myCalendar = Calendar.getInstance();

        edtFechaPerca= (EditText) findViewById(R.id.edtRegFechPercan);
        edtNomPerca= (EditText) findViewById(R.id.edtRegPercance);
        edtNotaPerca= (EditText) findViewById(R.id.edtNotaPercance);
        btnAgregarRegistro = (Button) findViewById(R.id.btnAgregarRegistro);
        fabCrearFechaReg=(FloatingActionButton)findViewById(R.id.fabCrearFechaPerc);


        edtFechaPerca.setEnabled(false);
        edtFechaPerca.setInputType(InputType.TYPE_CLASS_TEXT);
        edtFechaPerca.setFocusable(false);

        btnAgregarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registroRegistro();

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        fabCrearFechaReg.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistroRegMedicoActivity.this, date,
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

        Bundle extras = getIntent().getExtras();
        idMascota=extras.getString("idMascota");

        getVeterinarios();



    }

    private void registroRegistro() {

        progress=objLetrero.msjCargando("Agregando registro");
        progress.show();

        callRegistroReg= service.registrarRegMed(
                idMascota,
                ""+obtenerIdVeterinario(),
                Fecha,
                edtNomPerca.getText().toString(),
                edtNotaPerca.getText().toString()
        );

        callRegistroReg.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                if(response.isSuccessful()){
                    respuesta=response.body().getSuccess();
                    objLetrero.msjExito(respuesta,progress);

                }else{
                    if(response.code()==401){
                        objLetrero.handleErrors(response.errorBody(),"No se pudo agregar el registro ",progress);
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

    private void getVeterinarios() {

        if(contadorErrores==0){
            progress=objLetrero.msjCargando("Buscando datos...");
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

                    if (veterinarios.size() == 0) {
                        validacion("veterinarios");
                        return;
                    }

                    String nombreVeterinaria[] = new String[veterinarios.size()];
                    String nombreVete[] = new String[veterinarios.size()];
                    String idsVet[] = new String[veterinarios.size()];

                    int cont = 0;

                    for (Veterinario vete : veterinarios) {
                        nombreVeterinaria[cont] = vete.getVetNomVeterinaria();
                        nombreVete[cont] = vete.getVetNombre();
                        idsVet[cont] = "" + vete.getId();
                        cont++;

                    }


                    spinnerListadoVeterinario = (Spinner) findViewById(R.id.spnIdVeterinario);
                    spinnerVeteAdapter adapter =
                            new spinnerVeteAdapter(RegistroRegMedicoActivity.this,
                                    R.layout.itemsinnermasc,
                                    nombreVeterinaria,
                                    nombreVete,
                                    idsVet);
                    spinnerListadoVeterinario.setAdapter(adapter);


                } else {
                    contadorErrores=0;
                   objLetrero.msjErrorCarga(progress);
                }

            }

            @Override
            public void onFailure(Call<Veterinarios> call, Throwable t) {
                contadorErrores++;
                if (contadorErrores==2){
                    objLetrero.msjErrorCarga(progress);
                    contadorErrores=0;
                }else{
                    getVeterinarios();
                }

            }

        });
    }

    private void validacion(final String msj) {

        String Boton = null;

        switch(msj){
            case "veterinarios":
                Boton ="Crear Veterinario";
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
                            case "veterinarios":
                                Intent i = new Intent(RegistroRegMedicoActivity.this, Veterinario.class);
                                startActivity(i);
                                break;

                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        String formatoFechaGuardar = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfGuardarFecha = new SimpleDateFormat(formatoFechaGuardar, Locale.US);
        Fecha = sdfGuardarFecha.format(myCalendar.getTime());
        edtFechaPerca.setText(sdf.format(myCalendar.getTime()));
    }

    public String obtenerIdVeterinario(){
        View  vv= spinnerListadoVeterinario.getSelectedView();
        TextView nombre = (TextView)vv.findViewById(R.id.idVeterinario);

        return nombre.getText().toString();
    }

}
