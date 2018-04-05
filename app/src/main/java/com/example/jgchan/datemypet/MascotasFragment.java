package com.example.jgchan.datemypet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;

import com.example.jgchan.datemypet.adaptadores.mascotasAdapter;
import com.example.jgchan.datemypet.adaptadores.perfilMascotasAdapter;
import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Mascotas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class MascotasFragment extends Fragment {

    ListView lista_mascota;
    List<Mascota> mascotas ;
    private static final String TAG = "ListasMascotasctivity";
    private TextView mensajeVacio,nombreUsuario, correoUsuario;
    private SwipeRefreshLayout lyRefresh;
    private AccessToken datosAlamcenados;

    Call<Mascotas> call;
    apiService service;
    String id_user=null;
    private TokenManager tokenManager;
    ProgressDialog progress;

    FloatingActionButton fab;

    @SuppressLint("ValidFragment")
    public MascotasFragment(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mascotas, container, false);

        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados= tokenManager.getToken();

        //ANEXANDO EL REFRRESH

        lyRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.refreshListaMascota);
        lyRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        mensajeVacio=(TextView)rootView.findViewById(R.id.tvMascotaVacio); //TEXTVIEW PARA DESPLEGAR QUE HAY DATOS

        mensajeVacio.setVisibility(View.VISIBLE); //LO HACEMOS VISIBLE

        lista_mascota = (ListView)rootView.findViewById(R.id.lista_mascotas); //DAMOS DE ALTA EL CONTROLADOR LISTVIEW

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user=tokenManager.getToken().getId_user();


        //HABILITAMOS EL ONCLICK PARA CADA ITEM DE LA LISTVIEW
        lista_mascota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                TextView idMascota =(TextView)view.findViewById(R.id.listIdMasc);
                TextView nombreMascota =(TextView)view.findViewById(R.id.listNomMas);

                String nombre = nombreMascota.getText().toString();

                Intent i = new Intent(getContext(), InfoMascotaActivity.class);
                i.putExtra("idMascota",idMascota.getText());
                i.putExtra("nombre",nombre);
                i.putExtra("donde",0);
                startActivity(i);
                getActivity().finish();

            }
        });


        lista_mascota.setOnScrollListener(new AbsListView.OnScrollListener() {


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

        lista_mascota.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final TextView idMascota =(TextView)view.findViewById(R.id.listIdMasc);
                final TextView nombreMascota =(TextView)view.findViewById(R.id.listNomMas);
                TextView letrero =(TextView)view.findViewById(R.id.tvLetreroItemNomMas);



                PopupMenu popup = new PopupMenu(getActivity(), letrero);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_popup_vac_y_his, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i;

                        switch (item.getItemId()) {
                            case R.id.popup_vacunas:
                                i = new Intent(getContext(), VerVacunasActivity.class);
                                i.putExtra("idMascota",idMascota.getText().toString());
                                i.putExtra("donde",1);
                                startActivity(i);
                                break;

                            case R.id.popup_historial:

                                i = new Intent(getContext(), HistorialMedicoActivity.class);
                                i.putExtra("idMascota",idMascota.getText().toString());
                                i.putExtra("nombreMascota",nombreMascota.getText().toString());
                                i.putExtra("donde",1);
                                startActivity(i);

                                break;

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                return true;
            }
        });

        getMascotas(false);


        lyRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        getMascotas(true); //Llamando al metodo que busca todas las citas

                    }
                }
        );



        return rootView;
    }

    public  void  getMascotas(final boolean estaRefrescando){

        if(!estaRefrescando) {
            progress = new ProgressDialog(getContext());
            progress.setTitle("Cargando");
            progress.setMessage("Buscando mascotas, por favor espere...");
            progress.setCancelable(false);
            progress.show();
        }


        call= service.mismascotas(
                id_user
        );

        call.enqueue(new Callback<Mascotas>() {
            @Override
            public void onResponse(Call<Mascotas> call, Response<Mascotas> response) {


                Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    mascotas=response.body().getMascotas();
                    mascotasAdapter adapter = new mascotasAdapter(getActivity(), mascotas);
                    if(mascotas.size()<=0){
                       mensajeVacio.setVisibility(View.VISIBLE);
                    }else{
                       mensajeVacio.setVisibility(View.INVISIBLE);
                    }

                    lista_mascota.setAdapter(adapter);

                    if(estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh

                }else{
                    progress.dismiss();
                    if(estaRefrescando) lyRefresh.setRefreshing(false); //Terminando el refresh
                    if (response.code() == 421) {
                        //mensaje();
                        //Toast.makeText(IngresarActivity.this, "Credenciales no correspondientes", Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 420) {
                        //handleErrors(response.errorBody());
                        //Toast.makeText(IngresarActivity.this, "Credenciales no correspondientes", Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 401) {
                        // ApiError apiError = Utils.converErrors(response.errorBody());
                        //handleErrors(response.errorBody());
                        //
                    }
                }

            }

            @Override
            public void onFailure(Call<Mascotas> call, Throwable t) {
                Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();

                Toast.makeText(getContext(), "Ocurrió un error intentelo mas tarde.", Toast.LENGTH_LONG).show();
                //msjErrores("Error en la conexión");
            }
        });
    }

}
