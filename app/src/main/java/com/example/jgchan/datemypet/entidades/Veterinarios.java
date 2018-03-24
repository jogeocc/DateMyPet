package com.example.jgchan.datemypet.entidades;

import java.util.List;
import com.squareup.moshi.Json;

public class Veterinarios {

@Json(name = "veterinarios")
private List<Veterinario> veterinarios = null;

public List<Veterinario> getVeterinarios() {
return veterinarios;
}

public void setVeterinarios(List<Veterinario> veterinarios) {
this.veterinarios = veterinarios;
}

}