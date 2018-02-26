package com.example.jgchan.datemypet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private EditText txtNombre, txtDireccion, txtTelefono, txtCelular, txtCorreo;
    String nombre_usuario, contrasenia, nombre,direccion, telefono,celular, correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        txtNombre=(EditText)findViewById(R.id.txtNombre);
        txtDireccion=(EditText)findViewById(R.id.txtDireccion);
        txtTelefono=(EditText)findViewById(R.id.txtTelefono);
        txtCelular=(EditText)findViewById(R.id.txtCelular);
        txtCorreo=(EditText)findViewById(R.id.txtCorreo);

        Bundle extras = getIntent().getExtras();
        nombre_usuario=extras.getString("nombre_usuario");
        contrasenia=extras.getString("contrasenia");



        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(RegistroUsuarioActivity.this, "Hola", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });*/

      //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
      //          this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
      //  drawer.addDrawerListener(toggle);
      //  toggle.syncState();

      //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registro_usuario, menu);
        return true;
    }*/


    public  void  registrar_click(View v){

            if(validar()){

            }
    }

    private boolean validar() {

        nombre=txtNombre.getText().toString();
        direccion=txtDireccion.getText().toString();;
        telefono=txtTelefono.getText().toString();;
        celular=txtCelular.getText().toString();;
        correo=txtCorreo.getText().toString();;

        return  true;
    }


}
