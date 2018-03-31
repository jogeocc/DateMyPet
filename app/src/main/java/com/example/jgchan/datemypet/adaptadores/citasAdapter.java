package com.example.jgchan.datemypet.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.R;
import com.example.jgchan.datemypet.entidades.Cita;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jgchan on 3/03/18.
 */

public class citasAdapter extends BaseAdapter {
    protected Activity activity;
    protected List<Cita> items;

    public citasAdapter (Activity activity, List<Cita> items) {
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

    public void addAll(List<Cita> category) {
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
            v = inf.inflate(R.layout.item_citas, null);
        }

        Cita dir = items.get(position);


        TextView title = (TextView) v.findViewById(R.id.tvTituloCita);
        title.setText(dir.parseFecha()+" "+dir.parseHora());

        TextView description = (TextView) v.findViewById(R.id.tvContenido);
        description.setText(dir.getTipoCita());

        TextView nombreMascota = (TextView) v.findViewById(R.id.tvNombreMascota);
        nombreMascota.setText("Mascota: "+dir.getMascota().getMasNombre());

        TextView id = (TextView) v.findViewById(R.id.idCita);
        id.setText(""+dir.getId());



        return v;
    }



}

