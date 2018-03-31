package com.example.jgchan.datemypet.entidades;

import java.util.List;
import com.squareup.moshi.Json;

public class Citas {

    @Json(name = "citas")
    private List<Cita> citas = null;

    public List<Cita> getCitas() {
        return citas;
    }

    @Json(name = "cita")
    private Cita cita;

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

}