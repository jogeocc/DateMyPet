package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vacuna {

@Json(name = "id")
private Integer id;
@Json(name = "idMascota")
private Integer idMascota;
@Json(name = "vaNombre")
private String vaNombre;
@Json(name = "vaFecha")
private String vaFecha;
@Json(name = "vaNota")
private String vaNota;
@Json(name = "created_at")
private Object createdAt;
@Json(name = "updated_at")
private Object updatedAt;

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

public String getVaNombre() {
return vaNombre;
}

public void setVaNombre(String vaNombre) {
this.vaNombre = vaNombre;
}

public String getVaFecha() {
return vaFecha;
}

public void setVaFecha(String vaFecha) {
this.vaFecha = vaFecha;
}

public String getVaNota() {
return vaNota;
}

public void setVaNota(String vaNota) {
this.vaNota = vaNota;
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

    public String parseFecha() {
        String fechaNormal="";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoNormal = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fechaDelServer = formatter.parse(this.getVaFecha());
            fechaNormal = formatoNormal.format(fechaDelServer);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  fechaNormal;
    }
}