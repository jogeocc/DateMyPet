package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

public class Registro {

@Json(name = "id")
private Integer id;
@Json(name = "idHistorial")
private Integer idHistorial;
@Json(name = "idVeterinario")
private Integer idVeterinario;
@Json(name = "regMedFecha")
private String regMedFecha;
@Json(name = "regMedPercanse")
private String regMedPercanse;
@Json(name = "regMedDescp")
private String regMedDescp;
@Json(name = "created_at")
private Object createdAt;
@Json(name = "updated_at")
private Object updatedAt;

@Json(name = "veterinario")
private Veterinario veterinario;

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getIdHistorial() {
return idHistorial;
}

public void setIdHistorial(Integer idHistorial) {
this.idHistorial = idHistorial;
}

public Integer getIdVeterinario() {
return idVeterinario;
}

public void setIdVeterinario(Integer idVeterinario) {
this.idVeterinario = idVeterinario;
}

public String getRegMedFecha() {
return regMedFecha;
}

public void setRegMedFecha(String regMedFecha) {
this.regMedFecha = regMedFecha;
}

public String getRegMedPercanse() {
return regMedPercanse;
}

public void setRegMedPercanse(String regMedPercanse) {
this.regMedPercanse = regMedPercanse;
}

public String getRegMedDescp() {
return regMedDescp;
}

public void setRegMedDescp(String regMedDescp) {
this.regMedDescp = regMedDescp;
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

}