
package com.example.jgchan.datemypet.entidades;

import java.util.List;
import com.squareup.moshi.Json;

public class Mascotas {

@Json(name = "mascotas")

private List<Mascota> mascotas = null;

public List<Mascota> getMascotas() {
return mascotas;
}

public void setMascotas(List<Mascota> mascotas) {
this.mascotas = mascotas;
}

}