package com.example.jgchan.datemypet.entidades;

import java.util.List;
import com.squareup.moshi.Json;

public class Registros {

@Json(name = "registros")
private List<Registro> registros = null;

@Json(name = "registro")
private Registro registro = null;

public List<Registro> getRegistros() {
return registros;
}

public void setRegistros(List<Registro> registros) {
this.registros = registros;
}

    public Registro getRegistro() {
        return registro;
    }
}