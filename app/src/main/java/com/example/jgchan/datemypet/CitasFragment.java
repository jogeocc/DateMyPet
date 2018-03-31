package com.example.jgchan.datemypet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.citasAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Citas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class CitasFragment extends Fragment {

    ListView lista_citas;
    List<Cita> citas ;
    private static final String TAG = "InicioActivity";
    private TextView mensajeVacio,nombreUsuario, correoUsuario;
    private SwipeRefreshLayout lyRefresh;
    private AccessToken datosAlamcenados;

    Call<Citas> call;
    apiService service;
    String id_user=null;
    private TokenManager tokenManager;
    public ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_citas, container, false);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance( this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();



        lyRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);


        mensajeVacio = rootView.findViewById(R.id.tvCitasVacio);

        mensajeVacio=(TextView)rootView.findViewById(R.id.tvCitasVacio); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

        mensajeVacio.setVisibility(rootView.VISIBLE); //LO HACEMOS VISIBLE

        lista_citas = (ListView)rootView.findViewById(R.id.lista_citas); //DAMOS DE ALTA EL CONTROLADOR LISTVIEW

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        //HABILITAMOS EL ONCLICK PARA CADA ITEM DE LA LISTVIEW
        lista_citas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                TextView tit =(TextView)view.findViewById(R.id.idCita);

                Intent i = new Intent(getContext(), VerCitaActivity.class);
                i.putExtra("idCita",tit.getText().toString());
                i.putExtra("donde",0);
                startActivity(i);
                getActivity().finish();

            }
        });

        citas(false);


        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        citas(true); //Llamando al metodo que busca todas las citas
                        lyRefresh.setRefreshing(false); //Terminando el refresh
                    }
                }
        );

        return rootView;
    }


    private  void  citas(boolean estaRefrescando){

        if(!estaRefrescando) {
            progress = new ProgressDialog(getContext());
            progress.setTitle("Cargando");
            progress.setMessage("Buscando citas, por favor espere...");
            progress.setCancelable(false);
            progress.show();
        }


        call= service.miscitas(
                id_user
        );

        call.enqueue(new Callback<Citas>() {
            @Override
            public void onResponse(Call<Citas> call, Response<Citas> response) {

                Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    citas=response.body().getCitas();
                    citasAdapter adapter = new citasAdapter(getActivity(), citas);
                    if(citas.size()<=0){
                        mensajeVacio.setVisibility(View.VISIBLE);
                    }else{
                        mensajeVacio.setVisibility(View.INVISIBLE);
                    }

                    lista_citas.setAdapter(adapter);

                }else{
                    progress.dismiss();

                }

            }

            @Override
            public void onFailure(Call<Citas> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                Toast.makeText(getContext(), "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });
    }


}
