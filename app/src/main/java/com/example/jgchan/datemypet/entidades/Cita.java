package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Cita {

    @Json(name = "id")
    private Integer id;
    @Json(name = "idMascota")
    private Integer idMascota;
    @Json(name = "idVeterinario")
    private Integer idVeterinario;
    @Json(name = "ciFecha")
    private String ciFecha;
    @Json(name = "ciHora")
    private String ciHora;
    @Json(name = "ciTipo")
    private Integer ciTipo;
    @Json(name = "ciNota")
    private String ciNota;
    @Json(name = "created_at")
    private Object createdAt;
    @Json(name = "updated_at")
    private Object updatedAt;
    @Json(name = "mascota")
    private Mascota mascota;
    @Json(name = "veterinario")
    private Veterinario veterinario =null;

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public Integer getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Integer idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public String getCiFecha() {
        return ciFecha;
    }

    public void setCiFecha(String ciFecha) {
        this.ciFecha = ciFecha;
    }

    public Integer getCiTipo() {
        return ciTipo;
    }

    public void setCiTipo(Integer ciTipo) {
        this.ciTipo = ciTipo;
    }

    public String getCiNota() {
        return ciNota;
    }

    public void setCiNota(String ciNota) {
        this.ciNota = ciNota;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getCiHora() {
        return ciHora;
    }

    public void setCiHora(String ciHora) {
        this.ciHora = ciHora;
    }

    public String getTipoCita() {
        String tipoCita="";
        switch (getCiTipo()){
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

    public String parseFecha() {
        String fechaNormal="";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoNormal = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fechaDelServer = formatter.parse(this.getCiFecha());
            fechaNormal = formatoNormal.format(fechaDelServer);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  fechaNormal;
    }


    public String parseHora() {
        String horaNormal="";
        SimpleDateFormat formatoHoraServer = new SimpleDateFormat("H:mm:ss");
        SimpleDateFormat formatoHoraNormal = new SimpleDateFormat("h:mm a");


        try {
            Date horaDeLaCita  = formatoHoraServer.parse(this.getCiHora());
            horaNormal = formatoHoraNormal.format(horaDeLaCita);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  horaNormal;
    }



}