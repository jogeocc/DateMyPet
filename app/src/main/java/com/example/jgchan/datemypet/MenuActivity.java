package com.example.jgchan.datemypet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.citasAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.ParseoToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener {

    public int contadorErrores=0;
    public Letrero objLetrero = new Letrero(this);
    List<Cita> citas ;
    private static final String TAG = "InicioActivity";
    private TextView mensajeVacio;
    private SwipeRefreshLayout lyRefresh;

    Call<Citas> call;
    public apiService service;


    String respuesta;
    String id_user=null;
    public TokenManager tokenManager;
    public TextView nombreUsuario, correoUsuario;
    public AccessToken datosAlamcenados;
    public  ProgressDialog progress;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {

            if(!(this.getLocalClassName().equalsIgnoreCase("InicioActivity"))){
                Intent i = new Intent(this,InicioActivity.class);
                startActivity(i);

            }

        }else if(id == R.id.nav_perfil){

            if(!(this.getLocalClassName().equalsIgnoreCase("VerPerfilActivity"))){
                Intent i = new Intent(this, VerPerfilActivity.class);
                startActivity(i);

            }

        }else if (id == R.id.nav_mascota) {

            if(!(this.getLocalClassName().equalsIgnoreCase("ListaMascotasActivity"))){
                Intent i = new Intent(this, ListaMascotasActivity.class);
                startActivity(i);

            }

        }else if (id == R.id.nav_citas) {

            if(!(this.getLocalClassName().equalsIgnoreCase("ListadoCitasActivity"))){
                Intent i = new Intent(this, ListadoCitasActivity.class);
                startActivity(i);

            }

        }
        else if (id == R.id.nav_veterinarios) {

            if(!(this.getLocalClassName().equalsIgnoreCase("ListaVeterinariosActivity"))){
                Intent i = new Intent(this, ListaVeterinariosActivity.class);
                startActivity(i);

            }

        }
        else if (id == R.id.nav_vacunas) {

            if(!(this.getLocalClassName().equalsIgnoreCase("RegistroVacunasActivity"))){
                Intent i = new Intent(this, RegistroVacunasActivity.class);
                startActivity(i);
            }

        }else if (id == R.id.nav_acerca) {

            if(!(this.getLocalClassName().equalsIgnoreCase("AcercaDeActivity"))){
                Intent i = new Intent(this, AcercaDeActivity.class);
                startActivity(i);
            }

        }
        else if (id == R.id.nav_hack) {

            Intent i = new Intent(this,UnderTailActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Oye!!! Mira estoy usando Date My Pet. Decargalo http://date-my-pet-mx.tk/Download");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_send) {

            String email ="contactodatemypet@gmail.com"; /* Your email address here */
            String subject =  "Contactando a Date My Pet";/* Your subject here */
            String body = "Hola Date My Pet ....";/* Your body here */
            String chooserTitle = "Contactando a Date My Pet";/* Your chooser title here */

            Uri uri = Uri.parse("mailto:" + email)
                    .buildUpon()
                    .appendQueryParameter("subject", subject)
                    .appendQueryParameter("body", body)
                    .build();

           // Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
           // startActivity(Intent.createChooser(emailIntent, chooserTitle));


            String mailto = "mailto: contactodatemypet@ate-my-pet-mx.tk"+
                    "?cc=" + "support@date-my-pet-mx.tk" +
                    "&subject=" + Uri.encode(subject) +
                    "&body=" + Uri.encode(body);

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(mailto));

            try {
                startActivity(emailIntent);
            } catch (ActivityNotFoundException e) {
                //TODO: Handle case where no email app is available
            }


        } else if (id == R.id.nav_salir) {
            tokenManager.deleteToken();
            Intent i = new Intent(this, IngresarActivity.class);
            startActivity(i);
            finish();
        }


        if (progress!=null){
            if (progress.isShowing()){
                progress.dismiss();
            }
        }

        if (call!=null){
            if(call.isExecuted()){
                call.cancel();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
