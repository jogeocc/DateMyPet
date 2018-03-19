
package com.example.jgchan.datemypet.adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jgchan.datemypet.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
        imgFotoMascotaDiag.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)imgFotoMascotaDiag.getDrawable()).getBitmap();

               Uri foto= Uri.parse(saveImageFile(bitmap));

                galleryAddPic(foto);
                showToastMessage("Foto Guardada");
                return true;
            }
        });
        Picasso.get().load(url).into(imgFotoMascotaDiag);


    }


    void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    public String saveImageFile(Bitmap bitmap) {
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath()+File.separator+Environment.DIRECTORY_PICTURES+File.separator+"DateMyPet", "Perfil_Mascotas");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + "descarga_"+System.currentTimeMillis() + ".jpg");
        return uriSting;
    }


    private void galleryAddPic(Uri direccionFoto) {
        //Toast.makeText(this, ""+direccionFoto, Toast.LENGTH_SHORT).show();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(direccionFoto);
        this.sendBroadcast(mediaScanIntent);
    }

}