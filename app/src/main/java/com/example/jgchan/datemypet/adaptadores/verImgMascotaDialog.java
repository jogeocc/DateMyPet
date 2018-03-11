
package com.example.jgchan.datemypet.adaptadores;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jgchan.datemypet.R;
import com.squareup.picasso.Picasso;

public class verImgMascotaDialog extends Activity {

    ImageView imgFotoMascotaDiag;
    String mascota,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogfragment);
        Bundle extras = getIntent().getExtras();
        mascota=extras.getString("nombreMascota");
        url=extras.getString("url");
        this.setTitle(mascota);

        imgFotoMascotaDiag = (ImageView) findViewById(R.id.imgFotoMascotaDiag);

        Picasso.get().load(url).into(imgFotoMascotaDiag);


    }


    void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

}