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
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatoHoraServer = new SimpleDateFormat("H:mm:ss");

    SimpleDateFormat formatoHoraNormal = new SimpleDateFormat("h:mm a");

    SimpleDateFormat formatoNormal = new SimpleDateFormat("dd/MM/yyyy");

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


       try {

            Date fechaDelServer = formatter.parse(dir.getCiFecha());
            Date horaDeLaCita  = formatoHoraServer.parse(dir.getCiHora());
            String fechaNormal= formatoNormal.format(fechaDelServer);
            String horaNormal = formatoHoraNormal.format(horaDeLaCita);


           TextView title = (TextView) v.findViewById(R.id.tvTituloCita);
           title.setText(fechaNormal+ " "+horaNormal);

            //Toast.makeText(activity, "la fecha tiene formato "+fechaNormal, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();

            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
        }



        TextView description = (TextView) v.findViewById(R.id.tvContenido);
        description.setText(getTipoCita(dir.getCiTipo()));

        TextView nombreMascota = (TextView) v.findViewById(R.id.tvNombreMascota);
        nombreMascota.setText("Mascota: "+dir.getMascota().getMasNombre());

        TextView id = (TextView) v.findViewById(R.id.idCita);
        id.setText(""+dir.getId());



        return v;
    }

    public String getTipoCita(int tipo) {
        String tipoCita=null;
       // "Consulta","Cirugía","Análisis","Reproducción","Estética","Radiografías","Vacunación"

        switch (tipo){
            case 0 :
                tipoCita="Consulta";
                break;
            case 1 :
                 tipoCita="Cirugía";
                break;
            case 2 :
                 tipoCita="Análisis";
                break;
            case 3 :
                tipoCita="Reproducción";
                break;
            case 4 :
                tipoCita="Estética";
                break;
            case 5 :
                tipoCita="Radiografías";
                break;
            case 6 :
                tipoCita="Vacunación";
                break;
        }



        return tipoCita;
    }


}

