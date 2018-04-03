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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.spinnerMascAdapter;
import com.example.jgchan.datemypet.adaptadores.spinnerVeteAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.Utils;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.ApiError;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Citas;
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
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizarCitasActivity extends MenuActivity {

    Calendar myCalendar;
    EditText edtEditFechaCita,edtEditHoraCita,edtEditNotaCita;
    String Hora=null,Fecha=null;
    Button btnActualizarCita;

    FloatingActionButton btnEditDiaCita, btnEditHoraCita;

    List<Mascota> mascotas;
    List<Veterinario> veterinarios;
    Cita cita;
    Call<Mascotas> call;
    Call<Veterinarios> call2;
    Call<Success> callEditarCita;
    Call<Citas> callDatosCita;

    String respuesta;

    String id_user=null;
    private TokenManager tokenManager;
    private TextView nombreUsuario, correoUsuario;
    private AccessToken datosAlamcenados;
    ProgressDialog progress;


    String idCita;

    //SPINNER MASCOTa Y VETERINARIO
    Spinner spinnerEditListadoMascota, spinnerEditListadoVeterinario,spnEditTipoCita;
    String[] tipoCitas = {"Consulta","Cirugía","Análisis","Reproducción","Estética","Radiografías","Vacunación"};


    //ARREGLOS AUXILIARES PARA RELLENAS LOS SPINNERS
    String nombresMas[];
    String fotosMas[];
    String idsMas[];

    String nombreVeterinaria[];
    String nombreVete[];
    String idsVet[];

    //Objeto Letrero
    Letrero objLetrero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_citas);

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

        edtEditFechaCita= (EditText) findViewById(R.id.edtEditFechaCita);
        edtEditHoraCita= (EditText) findViewById(R.id.edtEditHoraCita);
        edtEditNotaCita= (EditText) findViewById(R.id.edtEditNotaCita);
        btnActualizarCita = (Button) findViewById(R.id.btnEditarCita);
        btnEditDiaCita=(FloatingActionButton)findViewById(R.id.btnEditDiaCita);
        btnEditHoraCita=(FloatingActionButton)findViewById(R.id.btnEditHoraCita);
        spnEditTipoCita = (Spinner) findViewById(R.id.spnEditTipoCita);
        spinnerEditListadoMascota = (Spinner) findViewById(R.id.spnEditIdMascota);
        spinnerEditListadoVeterinario = (Spinner) findViewById(R.id.spnEditIdVeterinario);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_spinner_basic, tipoCitas);
        spnEditTipoCita.setAdapter(adapter);


        btnActualizarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            actualizarCita();

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

        btnEditDiaCita.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ActualizarCitasActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnEditHoraCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ActualizarCitasActivity.this, hora,
                        myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),false).show();
            }
        });



        edtEditFechaCita.setEnabled(false);
        edtEditFechaCita.setInputType(InputType.TYPE_CLASS_TEXT);
        edtEditFechaCita.setFocusable(false);

        edtEditHoraCita.setEnabled(false);
        edtEditHoraCita.setInputType(InputType.TYPE_CLASS_TEXT);
        edtEditHoraCita.setFocusable(false);

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

        //REcuperar idCita
        Bundle extras = getIntent().getExtras();
        idCita=extras.getString("idCita");

        getMascotas();

    }

    private void actualizarCita() {


        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Actualizando Cita...");
        progress.setCancelable(false);
        progress.show();

        // @Field("ciFecha") String ciFecha,
        // @Field("ciHora") String ciHora,
        // @Field("ciTipo") String ciTipo,
        // @Field("ciNota") String ciNota);

        callEditarCita= service.actualizarCita(
                idCita,
                ""+obtenerIdMascota(),
                ""+obtenerIdVeterinario(),
                Fecha,
                Hora,
                ""+spnEditTipoCita.getSelectedItemId(),
                edtEditNotaCita.getText().toString()
        );

        callEditarCita.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                progress.dismiss();
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
                msjErrorCarga();
               // Toast.makeText(ActualizarCitasActivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();
            }
        });



    }

    private void getInfoCita() {

        callDatosCita = service.visualizarEditarCita(
                idCita
        );

        callDatosCita.enqueue(new Callback<Citas>() {
            @Override
            public void onResponse(Call<Citas> call, Response<Citas> response) {

                if(response.isSuccessful()){
                   cita = response.body().getCita();


                    spinnerEditListadoMascota.setSelection(getSelectIdMascota(cita.getIdMascota(),mascotas,idsMas));
                    spinnerEditListadoVeterinario.setSelection(getSelectIdVeterinario(cita.getIdVeterinario()));
                    setearFecha(cita.getCiFecha(),myCalendar);
                    seteaHora(cita.getCiHora());
                    edtEditNotaCita.setText(cita.getCiNota());
                    spnEditTipoCita.setSelection(cita.getCiTipo());


                    progress.dismiss();
                }else{

                }

            }

            @Override
            public void onFailure(Call<Citas> call, Throwable t) {

                objLetrero.msjErrorCarga(progress);
            }
        });
    }

    private int getSelectIdVeterinario(Integer idVeterinario) {
        int posicion=0;

        for (posicion=0;posicion<veterinarios.size(); posicion++){

            if(Integer.parseInt(idsVet[posicion])==idVeterinario) return posicion;
        }

        return posicion;
    }

    public int getSelectIdMascota(Integer idMascota, List<Mascota> mascotas, String[] idsMas) {
        int posicion=0;

        for (posicion=0;posicion<mascotas.size(); posicion++){

            if(Integer.parseInt(idsMas[posicion])==idMascota) return posicion;
        }

        return posicion;
    }

    private void seteaHora(String ciHora) {
        String hora[]=ciHora.split(":");
        myCalendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hora[0]));
        myCalendar.set(Calendar.MINUTE,Integer.parseInt(hora[1]));
        actualizarEditTex();
    }

    public void setearFecha(String ciFecha, Calendar myCalendar) {
        String fecha[] = ciFecha.split("-");
        myCalendar.set(Calendar.YEAR,Integer.parseInt(fecha[0]));
        myCalendar.set(Calendar.MONTH,Integer.parseInt(fecha[1])-1);
        myCalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(fecha[2]));
        updateLabel();


    }

    private void actualizarEditTex() {
        String formatoHora = "h:mm a";
        String formatoHoraGuardar = "H:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoHora, Locale.US);
        SimpleDateFormat sdfGuardar = new SimpleDateFormat(formatoHoraGuardar, Locale.US);
        Hora = sdfGuardar.format(myCalendar.getTime());

        edtEditHoraCita.setText(sdf.format(myCalendar.getTime()));
    }


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        String formatoFechaGuardar = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfGuardarFecha = new SimpleDateFormat(formatoFechaGuardar, Locale.US);
        Fecha = sdfGuardarFecha.format(myCalendar.getTime());
        edtEditFechaCita.setText(sdf.format(myCalendar.getTime()));
    }


    public String obtenerIdMascota(){
        View  vv= spinnerEditListadoMascota.getSelectedView();
        TextView nombre = (TextView)vv.findViewById(R.id.spnListadoIdMascota);

        return nombre.getText().toString();
    }

    public String obtenerIdVeterinario(){
        View  vv= spinnerEditListadoVeterinario.getSelectedView();
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


                    nombresMas= new String[mascotas.size()];
                    fotosMas= new String[mascotas.size()];
                    idsMas= new String[mascotas.size()];


                    int cont =0;

                    for (Mascota mas:mascotas) {
                        nombresMas[cont]= mas.getMasNombre();
                        fotosMas[cont]= mas.getMasFoto();
                        idsMas[cont]= ""+mas.getId();
                        cont++;

                    }

                    spinnerMascAdapter adapter = new spinnerMascAdapter(ActualizarCitasActivity.this, R.layout.itemsinnermasc, nombresMas,fotosMas,idsMas);
                    spinnerEditListadoMascota.setAdapter(adapter);

                    getVeterinarios();

                }else{

                }

            }

            @Override
            public void onFailure(Call<Mascotas> call, Throwable t) {


                progress.dismiss();

                //Toast.makeText(ActualizarCitasActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
                msjErrorCarga();
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
                                Intent i = new Intent(ActualizarCitasActivity.this, CrearMascotactivity.class);
                                startActivity(i);
                                break;

                            case "veterinarios":
                                Intent i2 = new Intent(ActualizarCitasActivity.this, VeterinarioActivity.class);
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

                    nombreVeterinaria = new String[veterinarios.size()];
                    nombreVete = new String[veterinarios.size()];
                    idsVet = new String[veterinarios.size()];

                    int cont = 0;

                    for (Veterinario vete : veterinarios) {
                        nombreVeterinaria[cont] = vete.getVetNomVeterinaria();
                        nombreVete[cont] = vete.getVetNombre();
                        idsVet[cont] = "" + vete.getId();
                        cont++;

                    }



                    spinnerVeteAdapter adapter =
                            new spinnerVeteAdapter(ActualizarCitasActivity.this,
                                    R.layout.itemsinnermasc,
                                    nombreVeterinaria,
                                    nombreVete,
                                    idsVet);
                    spinnerEditListadoVeterinario.setAdapter(adapter);

                    getInfoCita();

                } else {
                    progress.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Veterinarios> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();

                //Toast.makeText(ActualizarCitasActivity.this, "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
                msjErrorCarga();
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
                .setMessage("La cita no se pudo actualizar por los siguientes motivos: \n\n"+Error+"")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void msjErrorCarga() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡UPPS!")
                .setMessage("Ha Ocurrido un error, si el problema persiste contacte al administrador.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
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
                        Intent i = getIntent();
                        setResult(RESULT_OK,i);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
