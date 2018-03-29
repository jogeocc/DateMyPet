package com.example.jgchan.datemypet.adaptadores;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.R;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jgchan on 26/03/18.
 */

public class spinnerMascAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private String[] contentArray;
    private String[] imageArray;
    private String[] idsArray;

    public spinnerMascAdapter(Context context, int resource, String[] objects,
                          String[] imageArray,String[] idsArray) {
        super(context,  R.layout.itemsinnermasc, R.id.spinnerTextView, objects);
        this.ctx = context;
        this.contentArray = objects;
        this.imageArray = imageArray;
        this.idsArray = idsArray;
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
        View row = inflater.inflate(R.layout.itemsinnermasc, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
        textView.setText(contentArray[position]);

        TextView idmascotas = (TextView) row.findViewById(R.id.spnListadoIdMascota);
        idmascotas.setText(idsArray[position]);

        ImageView imageView = (ImageView)row.findViewById(R.id.spinnerImages);
        final String url = "http://date-my-pet-mx.tk/"+imageArray[position];
        Picasso.get().load(url).into(imageView);

        return row;
    }
}

