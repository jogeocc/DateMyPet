package com.example.jgchan.datemypet.entidades;

import java.util.List;
import com.squareup.moshi.Json;

public class Veterinarios {

@Json(name = "veterinarios")
private List<Veterinario> veterinarios = null;

@Json(name = "veterinario")
private Veterinario veterinario =null;

public List<Veterinario> getVeterinarios() {
return veterinarios;
}

public Veterinario getVeterinario() {
        return veterinario;
    }

public void setVeterinarios(List<Veterinario> veterinarios) {
this.veterinarios = veterinarios;
}

}