package com.example.jgchan.datemypet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.mascotasAdapter;
import com.example.jgchan.datemypet.adaptadores.spinnerMascAdapter;
import com.example.jgchan.datemypet.adaptadores.spinnerVeteAdapter;
import com.example.jgchan.datemypet.adaptadores.veterinariosAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.Utils;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.ApiError;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Veterinario;
import com.example.jgchan.datemypet.entidades.Veterinarios;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroCitasActivity extends MenuActivity {

    Calendar myCalendar;
    EditText edtFechaCita,edtHoraCita,edtNotaCita;
    String Hora=null,Fecha=null;
    Button btnAgregarCita;

    FloatingActionButton btnDiaCita, btnHoraCita;

    List<Mascota> mascotas;
    List<Veterinario> veterinarios;
    Call<Mascotas> call;
    Call<Veterinarios> call2;
    Call<Success> callRegistrarCita;

    String respuesta;

    String id_user=null;
    private TokenManager tokenManager;
    private TextView nombreUsuario, correoUsuario;
    private AccessToken datosAlamcenados;
    ProgressDialog progress;

    //SPINNER MASCOTa Y VETERINARIO
    Spinner spinnerListadoMascota, spinnerListadoVeterinario,spnTipoCita;
    String[] tipoCitas = {"Consulta","Cirugía","Análisis","Reproducción","Estética","Radiografías","Vacunación"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_citas);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         myCalendar = Calendar.getInstance();

         edtFechaCita= (EditText) findViewById(R.id.edtFechaCita);
         edtHoraCita= (EditText) findViewById(R.id.edtHoraCita);
        edtNotaCita= (EditText) findViewById(R.id.edtNotaCita);
         btnAgregarCita = (Button) findViewById(R.id.btnRegistrarCita);
         btnDiaCita=(FloatingActionButton)findViewById(R.id.btnDiaCita);
         btnHoraCita=(FloatingActionButton)findViewById(R.id.btnHoraCita);
         spnTipoCita = (Spinner) findViewById(R.id.spnTipoCita);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_spinner_basic, tipoCitas);
        spnTipoCita.setAdapter(adapter);


        btnAgregarCita.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

               registrarCitas();

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

        final TimePickerDialog.OnTimeSetListener hora = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                myCalendar.set(Calendar.MINUTE,minute);
                actualizarEditTex();
            }
        };

        btnDiaCita.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistroCitasActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnHoraCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(RegistroCitasActivity.this, hora,
                        myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),false).show();
            }
        });



        edtFechaCita.setEnabled(false);
        edtFechaCita.setInputType(InputType.TYPE_CLASS_TEXT);
        edtFechaCita.setFocusable(false);

        edtHoraCita.setEnabled(false);
        edtHoraCita.setInputType(InputType.TYPE_CLASS_TEXT);
        edtHoraCita.setFocusable(false);

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

    private void actualizarEditTex() {
        String formatoHora = "h:mm a";
        String formatoHoraGuardar = "H:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoHora, Locale.US);
        SimpleDateFormat sdfGuardar = new SimpleDateFormat(formatoHoraGuardar, Locale.US);
        Hora = sdfGuardar.format(myCalendar.getTime());

        edtHoraCita.setText(sdf.format(myCalendar.getTime()));
    }


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        String formatoFechaGuardar = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfGuardarFecha = new SimpleDateFormat(formatoFechaGuardar, Locale.US);
        Fecha = sdfGuardarFecha.format(myCalendar.getTime());
        edtFechaCita.setText(sdf.format(myCalendar.getTime()));
    }


    public String obtenerIdMascota(){
        View  vv= spinnerListadoMascota.getSelectedView();
        TextView nombre = (TextView)vv.findViewById(R.id.spnListadoIdMascota);

        return nombre.getText().toString();
    }

    public String obtenerIdVeterinario(){
        View  vv= spinnerListadoVeterinario.getSelectedView();
        TextView nombre = (TextView)vv.findViewById(R.id.idVeterinario);

        return nombre.getText().toString();
    }


    public  void  getMascotas(){

        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Por favor espere...");
        progress.setCancelable(false);
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
                    String idsMas[]= new String[mascotas.size()];
                    int cont =0;

                    for (Mascota mas:mascotas) {
                        nombresMas[cont]= mas.getMasNombre();
                        fotosMas[cont]= mas.getMasFoto();
                        idsMas[cont]= ""+mas.getId();
                        cont++;

                    }

                    spinnerListadoMascota = (Spinner) findViewById(R.id.spnIdMascota);
                    spinnerMascAdapter adapter = new spinnerMascAdapter(RegistroCitasActivity.this, R.layout.itemsinnermasc, nombresMas,fotosMas,idsMas);
                    spinnerListadoMascota.setAdapter(adapter);

                    getVeterinarios();

                }else{

                }

            }

            @Override
            public void onFailure(Call<Mascotas> call, Throwable t) {


                progress.dismiss();

                Toast.makeText(RegistroCitasActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });
    }

    private void validacion(final String msj) {

        String Boton = null;

        switch(msj){
            case "mascotas":
                    Boton ="Crear Mascota";
                break;

            case "veterinarios":
                    Boton= "Crear Veterinario";
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
                                    Intent i = new Intent(RegistroCitasActivity.this, CrearMascotactivity.class);
                                    startActivity(i);
                                break;

                            case "veterinarios":
                                Intent i2 = new Intent(RegistroCitasActivity.this, VeterinarioActivity.class);
                                startActivity(i2);
                                break;

                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void getVeterinarios() {


        call2 = service.misveterinarios(
                id_user
        );

        call2.enqueue(new Callback<Veterinarios>() {
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
                            new spinnerVeteAdapter(RegistroCitasActivity.this,
                                    R.layout.itemsinnermasc,
                                    nombreVeterinaria,
                                    nombreVete,
                                    idsVet);
                    spinnerListadoVeterinario.setAdapter(adapter);


                } else {
                    progress.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Veterinarios> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();

                Toast.makeText(RegistroCitasActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });


    }

    private void registrarCitas(){

        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Registrando Cita...");
        progress.setCancelable(false);
        progress.show();

       // @Field("ciFecha") String ciFecha,
       // @Field("ciHora") String ciHora,
       // @Field("ciTipo") String ciTipo,
       // @Field("ciNota") String ciNota);

        callRegistrarCita= service.registrarCita(
                ""+obtenerIdMascota(),
                ""+obtenerIdVeterinario(),
                Fecha,
                Hora,
                ""+spnTipoCita.getSelectedItemId(),
                edtNotaCita.getText().toString()
        );

        callRegistrarCita.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                progress.dismiss();
                // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
                //Toast.makeText(EditarPerfilActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                //Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    respuesta=response.body().getSuccess();
                    msjExito(respuesta);


                }else{
                    progress.dismiss();

                    if(response.code()==401){
                        handleErrors(response.errorBody());
                    }else {
                        Log.e("Error Server",response.message()+ " "+response.code());
                    }
                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                Toast.makeText(RegistroCitasActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();
            }
        });

    }


    private void handleErrors(ResponseBody response){
        progress.dismiss();
        String errores="";

        ApiError apiError = Utils.converErrors(response);


        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()){
            if (error.getKey().equals("idMascota")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("idVeterinario")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("ciFecha")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("ciTipo")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("ciHora")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

        }

        msjErrores(errores);


    }

    public void msjErrores(String Error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡UPPS!")
                .setMessage("La cuenta no se pudo registrar por los siguientes motivos: \n\n"+Error+"")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void msjExito(String respuesta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(RegistroCitasActivity.this,ListadoCitasActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}


