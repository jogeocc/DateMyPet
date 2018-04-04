package com.example.jgchan.datemypet;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.citasAdapter;
import com.example.jgchan.datemypet.adaptadores.registrosAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.Letrero;
import com.example.jgchan.datemypet.entidades.Registro;
import com.example.jgchan.datemypet.entidades.Registros;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class RegistrosFragment extends Fragment {

    ListView lista_registros;
    private static final String TAG = "RegistroActivity";
    String idMascota;

    Call<Registros> call;
    apiService service;
    private TextView mensajeVacio;
    private SwipeRefreshLayout lyRefresh;
    public ProgressDialog progress;
    Letrero objLetrero;

    List<Registro> registros;

    FloatingActionButton fab;

    @SuppressLint("ValidFragment")
    public RegistrosFragment(Letrero objLetrero, FloatingActionButton fab, String idMascota) {
        this.objLetrero=objLetrero;
        this.fab=fab;
        this.idMascota = idMascota;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_registros, container, false);



        lyRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);


        mensajeVacio=(TextView)rootView.findViewById(R.id.tvRegistroVacio); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

        mensajeVacio.setVisibility(rootView.VISIBLE); //LO HACEMOS VISIBLE

        lista_registros = (ListView)rootView.findViewById(R.id.lista_registros); //DAMOS DE ALTA EL CONTROLADOR LISTVIEW

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION
                //HABILITAMOS EL ONCLICK PARA CADA ITEM DE LA LISTVIEW
        lista_registros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

            }
        });

        lista_registros.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0)
                    // Puedes ocultarlo simplemente
                    //fab.hide();
                    // o añadir la animación deseada
                    fab.animate().translationY(fab.getHeight() +
                            getResources().getDimension(R.dimen.fab_margin))
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(100); // Cambiar al tiempo deseado
                else if (firstVisibleItem == 0)
                    //fab.show();
                    fab.animate().translationY(0)
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(100); // Cambiar al tiempo deseado
            }

        });

        registros(false);


        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        registros(true); //Llamando al metodo que busca todas las citas
                        lyRefresh.setRefreshing(false); //Terminando el refresh
                    }
                }
        );

        return rootView;
    }


    private  void  registros(boolean estaRefrescando){

        if(!estaRefrescando) {
            progress = objLetrero.msjCargando("Buscando historial médico, por favor espere...");
            progress.show();
        }


        call= service.susRegistrosMedicos(
                idMascota
        );

        call.enqueue(new Callback<Registros>() {
            @Override
            public void onResponse(Call<Registros> call, Response<Registros> response) {

                Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    registros=response.body().getRegistros();
                    registrosAdapter adapter = new registrosAdapter(getActivity(), registros);
                    if(registros.size()<=0){
                        mensajeVacio.setVisibility(View.VISIBLE);
                    }else{
                        mensajeVacio.setVisibility(View.INVISIBLE);
                    }

                    lista_registros.setAdapter(adapter);

                }else{
                    progress.dismiss();

                }

            }

            @Override
            public void onFailure(Call<Registros> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());

                objLetrero.msjErrorCarga(progress);
                //msjErrores("Error en la conexión");
            }
        });
    }


}
