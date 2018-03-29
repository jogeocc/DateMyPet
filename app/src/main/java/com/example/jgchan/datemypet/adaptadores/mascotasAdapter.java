package com.example.jgchan.datemypet.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.R;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jgchan on 3/03/18.
 */

public class mascotasAdapter extends BaseAdapter {
    protected Activity activity;
    protected List<Mascota> items;


    public mascotasAdapter(Activity activity, List<Mascota> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(List<Mascota> mascota) {
        for (int i = 0; i < mascota.size(); i++) {
            items.add(mascota.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;


        if (convertView == null) {

           LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_mascota, null);

        }

        ImageView fotoMascota = (ImageView) v.findViewById(R.id.lisImgMascota);
        final TextView  tvNombreMascota= (TextView) v.findViewById(R.id.listNomMas);
        TextView tvSexMas= (TextView) v.findViewById(R.id.listSexMas);
        TextView tvEdadMas= (TextView) v.findViewById(R.id.listEdadMas);
        TextView tvRazaView= (TextView) v.findViewById(R.id.listRazaMas);
        TextView idMascota= (TextView) v.findViewById(R.id.listIdMasc);


        final Mascota mas = items.get(position);

        tvNombreMascota.setText("Nombre: "+mas.getMasNombre());
        tvSexMas.setText("Sexo: "+mas.getMasSexo());
        tvEdadMas.setText("Edad: "+mas.getMasEdad());
        tvRazaView.setText("Raza: "+mas.getMasRaza());
        idMascota.setText(""+mas.getId());


        final String url = "http://date-my-pet-mx.tk/"+mas.getMasFoto();
        Picasso.get().load(url).into(fotoMascota);

        fotoMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, verImgMascotaDialog.class);
                i.putExtra("nombreMascota",mas.getMasNombre());
                i.putExtra("url",url);
                activity.startActivity(i);
            }
        });

        return v;
    }
}

