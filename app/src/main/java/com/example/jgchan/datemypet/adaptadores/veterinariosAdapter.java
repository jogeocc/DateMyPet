package com.example.jgchan.datemypet.adaptadores;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.CrearMascotactivity;
import com.example.jgchan.datemypet.R;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Veterinario;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by jgchan on 3/03/18.
 */

public class veterinariosAdapter extends BaseAdapter {
    protected Activity activity;
    protected List<Veterinario> items;


    public veterinariosAdapter(Activity activity, List<Veterinario> items) {
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

    public void addAll(List<Veterinario> veterinarios) {
        for (int i = 0; i < veterinarios.size(); i++) {
            items.add(veterinarios.get(i));
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
            v = inf.inflate(R.layout.item_veterinario, null);

        }


        TextView  tvNombreVeterinaria= (TextView) v.findViewById(R.id.tvNombreVet);
        TextView tvNombreVet= (TextView) v.findViewById(R.id.tvNombreVeterinario);
        final TextView tvTelVet= (TextView) v.findViewById(R.id.tvTelVet);
        TextView idVeterinario= (TextView) v.findViewById(R.id.idVeterinario);
        final LinearLayout itemVeterinario = (LinearLayout) v.findViewById(R.id.ItemVeterinario);



        final Veterinario vet = items.get(position);

        tvNombreVeterinaria.setText(vet.getVetNomVeterinaria());
        tvNombreVet.setText(vet.getVetNombre());
        tvTelVet.setText(vet.getVetTelefono());
        idVeterinario.setText(""+vet.getId());


        return v;
    }




}

