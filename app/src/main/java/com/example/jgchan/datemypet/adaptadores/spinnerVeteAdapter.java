package com.example.jgchan.datemypet.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jgchan.datemypet.R;
import com.squareup.picasso.Picasso;

/**
 * Created by jgchan on 26/03/18.
 */

public class spinnerVeteAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private String[] contentArray;
    private String[] idsArray;
    private String[] nombreVeterinario;


    public spinnerVeteAdapter(
            Context context,
            int resource,
            String[] objects,
            String[] nombreVeterinario,
            String[] idsArray) {

        super(context,  R.layout.item_veterinario, R.id.tvNombreVet, objects);
        this.ctx = context;
        this.contentArray = objects;
        this.idsArray = idsArray;
        this.nombreVeterinario=nombreVeterinario;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_spinner_veterinario, parent, false);

        ImageView  imgVet= (ImageView) v.findViewById(R.id.imgListVetLogo);
        TextView  tvNombreVeterinaria= (TextView) v.findViewById(R.id.tvNombreVet);
        TextView tvNombreVet= (TextView) v.findViewById(R.id.tvNombreVeterinario);
        TextView idVeterinario= (TextView) v.findViewById(R.id.idVeterinario);

        imgVet.setImageResource(R.drawable.iconoveterinario);
        tvNombreVeterinaria.setText(contentArray[position]);
        tvNombreVet.setText(nombreVeterinario[position]);
        idVeterinario.setText(""+idsArray[position]);



        return v;
    }
}

