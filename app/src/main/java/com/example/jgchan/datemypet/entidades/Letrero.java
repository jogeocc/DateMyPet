package com.example.jgchan.datemypet.entidades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.jgchan.datemypet.InfoMascotaActivity;
import com.example.jgchan.datemypet.ListaMascotasActivity;
import com.example.jgchan.datemypet.conexion.Utils;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jgchan on 31/03/18.
 */

public class Letrero {

    Activity activity;

    public Letrero(Activity activity) {
        this.activity = activity;
    }

    public void msjErrorCarga(ProgressDialog progress) {
        progress.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡UPPS!")
                .setMessage("Ha Ocurrido un error, si el problema persiste contacte al administrador.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        return;
    }

    public void msjErrorCargaSinNada(ProgressDialog progress) {
        progress.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡UPPS!")
                .setMessage("Ha Ocurrido un error, si el problema persiste contacte al administrador.")
                .setCancelable(false)
                .setPositiveButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();

        return;
    }


    public void msjErrorDescarga(ProgressDialog progress) {
        progress.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡UPPS!")
                .setMessage("No se pudo descargar el archivo, si el problema persiste contacte al administrador.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        return;
    }



    public ProgressDialog msjCargando(String msj){
        ProgressDialog progress = new ProgressDialog(activity);
        progress.setTitle("Cargando");
        progress.setMessage(msj);
        progress.setCancelable(false);
        return progress;
    }

    public void handleErrors(ResponseBody response, String msj ,ProgressDialog progress){

        progress.dismiss();
        String errores="";

        ApiError apiError = Utils.converErrors(response);

        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()){
            if (error.getKey().equals("username")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("nombre")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("correo")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("direccion")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("password")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("celular")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("idMascota")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("vaNombre")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("vaFecha")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("vaNota")){
                errores+="- "+error.getValue().get(0)+"\n";
            }


            if (error.getKey().equals("idVeterinario")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("ciFecha")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("ciTipo")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("ciHora")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("regMedFecha")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("regMedPercanse")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("regMedDescp")){
                errores+="- "+error.getValue().get(0)+"\n";
            }


        }

        msjErrores(msj,errores);

    }


    public void msjErrores(String msj,String Error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡UPPS!")
                .setMessage(msj+" debido a los siguientes motivos:\n\n"+Error+"")
                .setCancelable(false)
                .setPositiveButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public AlertDialog.Builder  msjConfirmacion(String msj) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡Advertencia!")
                .setMessage("¿"+msj+"?")
                .setCancelable(false)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

       return builder;
    }

    public void msjExito(String respuesta, ProgressDialog progress) {
        progress.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                           activity.onBackPressed();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void msjExitoDescarga(String respuesta, final String Carpeta, final String nombreArchivo, final ManejoArchivos ma, ProgressDialog progress) {
        progress.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setNeutralButton("OK",null)
                .setPositiveButton("Ver archivo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ma.verArchivo(Carpeta,nombreArchivo);
                    }
                })
                .setNegativeButton("Compartir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ma.compartirArchivo(Carpeta,nombreArchivo);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void msjExitoSinNada(String respuesta, ProgressDialog progress) {
        progress.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void msjExitoFinish(String respuesta, ProgressDialog progress) {
        progress.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent i = activity.getIntent();
                        activity.setResult(RESULT_OK,i);
                        activity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void msjExitoSinRet (String respuesta, ProgressDialog progress) {
        progress.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void msjActivacionUsua(String nombreUsuario) {


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(nombreUsuario + " tu cuenta se activo con éxito")
                .setCancelable(false)
                .setPositiveButton("OK",null);
        AlertDialog alert = builder.create();
        alert.show();

    }
}
