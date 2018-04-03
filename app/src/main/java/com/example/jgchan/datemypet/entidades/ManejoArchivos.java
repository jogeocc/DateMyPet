package com.example.jgchan.datemypet.entidades;

/**
 * Created by jgchan on 2/04/18.
 */


import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import com.example.jgchan.datemypet.conexion.apiService;
import java.io.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManejoArchivos implements FilenameFilter {

    String name;
    Activity activity;
    Call<ResponseBody> descargar;

    public ManejoArchivos(String name, Activity activity){
        this.name=name;
        this.activity=activity;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().contains(this.name.toLowerCase());
    }

    public File[] verContenido() {


        String path = activity.getExternalFilesDir(null) + File.separator+"historiales_medicos";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);

        File[] files = directory.listFiles(this);
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

        return files;

    }

    public void verArchivo(String nombre_archivo){
        final String base_url=activity.getExternalFilesDir(null) + File.separator+"historiales_medicos"+File.separator;
        File file = new File(base_url+nombre_archivo);

        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(activity,
                    "com.example.jgchan.datemypet.provider",
                    file), "application/pdf");
            activity.startActivity(intent);
        }
    }


    public boolean descargarPdf(String idMascota, apiService service){
        final boolean[] writtenToDisk = new boolean[1];

        descargar= service.descargarPdf(
                idMascota
        );

        descargar.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){

                    writtenToDisk[0] = writeResponseBodyToDisk(response.body());

                    Log.d("PDFDOWNLOAD", "Se descargo coorectamente? " + writtenToDisk[0]);

                }else{
                    Toast.makeText(activity, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                 writtenToDisk[0] =false;
            }
        });

        return writtenToDisk[0];

    }


    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs

            File storageDir = new File(activity.getExternalFilesDir(null) + File.separator+"historiales_medicos");

            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            File futureStudioIconFile = new File(storageDir.getPath() + File.separator + "historial.pdf");

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
}
