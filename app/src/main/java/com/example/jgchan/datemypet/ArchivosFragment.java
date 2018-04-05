package com.example.jgchan.datemypet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jgchan.datemypet.adaptadores.archivosAdapter;
import com.example.jgchan.datemypet.adaptadores.citasAdapter;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.ManejoArchivos;

import java.io.File;

@SuppressLint("ValidFragment")
public class ArchivosFragment extends Fragment {

    ManejoArchivos ma;
    SwipeRefreshLayout lyRefresh;
    ListView lista_archivos;
    TextView mensajeVacio;
    Letrero objLetrero;
    FloatingActionButton fab;
    String nombreMascota, idMascota;
    String carpeta;

    @SuppressLint("ValidFragment")
    public ArchivosFragment(Letrero objLetrero, FloatingActionButton fab, ManejoArchivos ma, String idMascota, String nombreMascota) {
        this.ma=ma;
        this.fab=fab;
        this.objLetrero=objLetrero;
        this.idMascota=idMascota;
        this.nombreMascota=nombreMascota;
        this.carpeta=idMascota+"-"+nombreMascota;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_archivos, container, false);

        lyRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);


        mensajeVacio=(TextView)rootView.findViewById(R.id.tvArchivosVacio); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

        mensajeVacio.setVisibility(rootView.VISIBLE); //LO HACEMOS VISIBLE

        lista_archivos = (ListView)rootView.findViewById(R.id.lista_archivos); //DAMOS DE ALTA EL CONTROLADOR LISTVIEW


        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        cargarArchivos(); //Llamando al metodo que busca todas las citas
                        lyRefresh.setRefreshing(false); //Terminando el refresh
                    }
                }
        );

        lista_archivos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final TextView nombre =(TextView)view.findViewById(R.id.tvNombreArchivo);
                final String nombreArchivo = nombre.getText().toString();


                PopupMenu popup = new PopupMenu(getActivity(), nombre);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu_archivos, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i;

                        switch (item.getItemId()) {
                            case R.id.popup_compartir:
                                    ma.compartirArchivo(idMascota+"-"+nombreMascota,nombreArchivo);
                                break;

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                return true;
            }
        });

        lista_archivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                TextView tit =(TextView)view.findViewById(R.id.tvNombreArchivo);
                String nombre = tit.getText().toString();
                ma.verArchivo(idMascota+"-"+nombreMascota,nombre);

            }
        });

        lista_archivos.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0)
                    fab.animate().translationY(fab.getHeight() +
                            getResources().getDimension(R.dimen.fab_margin))
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(100); // Cambiar al tiempo deseado
                else if (firstVisibleItem == 0)
                    fab.animate().translationY(0)
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(100); // Cambiar al tiempo deseado
            }

        });

        cargarArchivos();

        return rootView;

    }

    public void cargarArchivos() {

        File archivos[]=ma.verContenido(carpeta);
        archivosAdapter adapter = new archivosAdapter(getActivity(),archivos );

        if(archivos.length==0){
            mensajeVacio.setVisibility(View.VISIBLE);
        }else{
            mensajeVacio.setVisibility(View.INVISIBLE);
        }

        lista_archivos.setAdapter(adapter);

    }


}
