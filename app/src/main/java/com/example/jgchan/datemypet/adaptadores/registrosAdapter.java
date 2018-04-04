package com.example.jgchan.datemypet.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jgchan.datemypet.R;
import com.example.jgchan.datemypet.entidades.Registro;
import com.example.jgchan.datemypet.entidades.Vacuna;

import java.util.List;

/**
 * Created by jgchan on 3/03/18.
 */

public class registrosAdapter extends BaseAdapter {
    protected Activity activity;
    protected  List<Registro> items;

    public registrosAdapter(Activity activity, List<Registro> items) {
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

    public void addAll(List<Registro> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
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
            v = inf.inflate(R.layout.item_registro, null);
        }

        Registro reg = items.get(position);

        TextView idRegistro = (TextView)v.findViewById(R.id.idRegistro);
        idRegistro.setText(""+reg.getId());

        TextView fecha = (TextView)v.findViewById(R.id.regMedFecha);
        fecha.setText(""+reg.getRegMedFecha());

        TextView percanse = (TextView)v.findViewById(R.id.tvLisRegMedPercanse);
        percanse.setText(""+reg.getRegMedPercanse());

        TextView vete = (TextView)v.findViewById(R.id.tvNombreVet);
        vete.setText(""+reg.getVeterinario().getVetNombre());

        TextView descrip = (TextView)v.findViewById(R.id.tvLisRegMedDescp);
        descrip.setText(""+reg.getId());

        return v;
    }



}

