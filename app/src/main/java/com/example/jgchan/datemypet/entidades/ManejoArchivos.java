package com.example.jgchan.datemypet.entidades;

/**
 * Created by jgchan on 2/04/18.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import com.example.jgchan.datemypet.conexion.apiService;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManejoArchivos implements FilenameFilter {

    String name;
    Activity activity;
    Call<ResponseBody> descargar;
    boolean writtenToDisk=false;
    ProgressDialog progress;
    Letrero objLetrero;
    String archivo="";
    int contadorErrores=0;

    public ManejoArchivos(){}

    public ManejoArchivos(String name, Activity activity){
        this.name=name;
        this.activity=activity;
        objLetrero= new Letrero(activity);
    }

    public ManejoArchivos(String name, Activity activity, ProgressDialog progress){
        this.name=name;
        this.activity=activity;
        this.progress=progress;
        objLetrero= new Letrero(activity);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().contains(this.name.toLowerCase());
    }

    public File[] verContenido(String carpeta) {


        String path = activity.getExternalFilesDir(null) + File.separator+"historiales_medicos"+File.separator+carpeta;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File[] files = directory.listFiles(this);
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

        return files;

    }

    public void compartirArchivo(String carpeta, String nombre_archivo){
        final String base_url=activity.getExternalFilesDir(null) + File.separator+"historiales_medicos"+File.separator+carpeta+File.separator;
        File file = new File(base_url+nombre_archivo);

        if (file.exists()) {
            Uri path =   Uri.fromFile(new File(file.getPath()));

            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent .setType("vnd.android.cursor.dir/email");
            emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Compartiendo historial...");
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Compartir Historial");
            try {
                activity.startActivity(Intent.createChooser(emailIntent , "Compartir Historial"));
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
    }

    public void verArchivo(String carpeta, String nombre_archivo){
        final String base_url=activity.getExternalFilesDir(null) + File.separator+"historiales_medicos"+File.separator+carpeta+File.separator;
        File file = new File(base_url+nombre_archivo);

        if (file.exists()) {
            Uri uri =   Uri.fromFile(new File(file.getPath()));

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Log.e("Open File", ""+Uri.parse(String.valueOf(file.getPath())));
            intent.setDataAndType(uri,"application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent2 = Intent.createChooser(intent, "Abrir historial con: ");
            try {
                activity.startActivity(intent2);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
    }


    public boolean descargarPdf(final String idMascota, final String nombreMascota, final apiService service){
        Log.d("Files", "Entro a descargar");


        descargar= service.descargarPdf(
                idMascota
        );

        descargar.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    String carpeta=idMascota+"-"+nombreMascota;

                    Log.d("Files", "Si regreso correctamente");


                    writtenToDisk = writeResponseBodyToDisk(response.body(),carpeta,archivo);

                    if(writtenToDisk) objLetrero.msjExitoDescarga("Descarga Completa \nArchivo: "+archivo,carpeta,archivo,ManejoArchivos.this,progress);
                    else objLetrero.msjErrorDescarga(progress);
                    Log.d("PDFDOWNLOAD", "Se descargo coorectamente? " + writtenToDisk);

                }else{
                    contadorErrores++;
                    if (contadorErrores==3){
                        objLetrero.msjErrorDescarga(progress);
                        contadorErrores=0;
                    }else
                        descargarPdf(idMascota,nombreMascota,service);


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                objLetrero.msjErrorDescarga(progress);
                 Log.d("Files", "No error regreso correctamente "+t.getMessage());
            }
        });

        return writtenToDisk;

    }


    private boolean writeResponseBodyToDisk(ResponseBody body,String carpeta,String nombreArchivo) {
        try {
            // todo change the file location/name according to your needs

            archivo="HM-"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".pdf";

            Log.d("PDFDOWNLOAD", "Esta descargando.....");

            File storageDir = new File(
                    activity.getExternalFilesDir(null)
                            + File.separator+"historiales_medicos"
                            +File.separator
                            +carpeta);

            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            File futureStudioIconFile = new File(
                    storageDir.getPath() +
                            File.separator +
                            archivo
                            );

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("PDFDOWNLOAD", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public String getFecha(long timeFecha){

        String fechaNormal="";
        Date dateFecha = new Date(timeFecha);
        SimpleDateFormat formatoNormal = new SimpleDateFormat("dd/MM/yyyy h:mm a");
        fechaNormal = formatoNormal.format(dateFecha);
        return  fechaNormal;

    }


    public String getTamanio(long tamanioBytes){
        String tamanio="";
        float tamKbytes=0;
        float tamMbytes=0;

        if(tamanioBytes<1000){
            tamanio= formatoPeso(tamanioBytes) + "B";
        }
        else if(tamanioBytes>=1000){
            tamKbytes= ((float) tamanioBytes / (float) 1024);
            tamanio= formatoPeso(tamKbytes) + " KB";

        }else if(tamanioBytes>=1000000){
            tamMbytes= (tamKbytes / (float) 1024);
            tamanio= formatoPeso(tamMbytes) + " MB";

        }else if(tamanioBytes>=1000000000 ){
            tamanio= formatoPeso(tamMbytes / (float) 1024) + " GB";
        }

        return tamanio;
    }


    public String formatoPeso(float peso){
        DecimalFormat myFormatter = new DecimalFormat("###,###.#");
        String output = myFormatter.format(peso);

        return output;
    }

}
