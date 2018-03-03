package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

public class Cita {

    @Json(name = "id")
    private Integer id;
    @Json(name = "idMascota")
    private Integer idMascota;
    @Json(name = "idVeterinario")
    private Integer idVeterinario;
    @Json(name = "ciFecha")
    private String ciFecha;
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

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
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

}