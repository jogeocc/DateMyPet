package com.example.jgchan.datemypet.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jgchan.datemypet.R;
import com.example.jgchan.datemypet.entidades.ManejoArchivos;
import com.example.jgchan.datemypet.entidades.Registro;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by jgchan on 3/03/18.
 */

public class archivosAdapter extends BaseAdapter {
    protected Activity activity;
    protected  File[] items;
    ManejoArchivos ma = new ManejoArchivos();

    public archivosAdapter(Activity activity, File[] items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }


    public void addAll(File[] archivos) {
        for (int i = 0; i < archivos.length; i++) {
            items[i]=archivos[i];
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items[arg0];
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
            v = inf.inflate(R.layout.item_archivo, null);
        }

        File archi = items[position];

        TextView fecha = (TextView)v.findViewById(R.id.archivoFecha);
        fecha.setText(ma.getFecha(archi.lastModified()));

        TextView tvNombreArchivo = (TextView)v.findViewById(R.id.tvNombreArchivo);
        tvNombreArchivo.setText(""+archi.getName());

        TextView tvTamaniooArchivo = (TextView)v.findViewById(R.id.tvTamaniooArchivo);
        tvTamaniooArchivo.setText(ma.getTamanio(archi.length()));

        return v;
    }



}

