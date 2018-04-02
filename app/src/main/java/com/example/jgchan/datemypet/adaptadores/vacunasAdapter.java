package com.example.jgchan.datemypet.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jgchan.datemypet.R;
import com.example.jgchan.datemypet.entidades.Cita;
import com.example.jgchan.datemypet.entidades.Vacuna;

import java.util.List;

/**
 * Created by jgchan on 3/03/18.
 */

public class vacunasAdapter extends BaseAdapter {
    protected Activity activity;
    protected List<Vacuna> items;

    public vacunasAdapter(Activity activity, List<Vacuna> items) {
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

    public void addAll(List<Vacuna> category) {
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
            v = inf.inflate(R.layout.item_vacuna, null);
        }

        Vacuna vac = items.get(position);


        TextView fecha = (TextView) v.findViewById(R.id.tvLisFechaAplicVac);
        fecha.setText(vac.parseFecha());

        TextView nombre = (TextView) v.findViewById(R.id.tvLisNomVac);
        nombre.setText(vac.getVaNombre());


        TextView id = (TextView) v.findViewById(R.id.idVacuna);
        id.setText(""+vac.getId());

        TextView nota = (TextView) v.findViewById(R.id.tvNotaVac);
        nota.setText(""+vac.getVaNota());

        return v;
    }



}

