package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.ManejoArchivos;

public class navegacionActivity extends AppCompatActivity {

    WebView web;
    Letrero objLetrero = new Letrero(this);
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    public apiService service;
    boolean bandera=false;
    SwipeRefreshLayout lyRefresh;
    String idMascota, nombreMascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        web=(WebView)findViewById(R.id.wvNavegador);

        Bundle extras = getIntent().getExtras();
         idMascota=extras.getString("idMascota");
         nombreMascota=extras.getString("nombreMascota");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        lyRefresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = objLetrero.msjCargando("Descargando....");
                progressDialog.show();

                ManejoArchivos ma = new ManejoArchivos("",navegacionActivity.this,progressDialog);
                bandera = ma.descargarPdf(idMascota,nombreMascota,service);


                Log.d("Regreso bandera", ""+bandera);
            }
        });

        cargarRegistro();

        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        cargarRegistro();
                    }
                }
        );

    }

    private void cargarRegistro() {
        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...

                if(progressDialog==null){
                    progressDialog= objLetrero.msjCargando("Generando Historial...");
                    progressDialog.show();
                }

                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100){
                    lyRefresh.setRefreshing(false); //Terminando el refresh
                    progressDialog.dismiss();
                }

            }
        });
        web.setWebViewClient(new HelloWebViewClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("http://date-my-pet-mx.tk/visualizar/"+idMascota+"/historial");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
