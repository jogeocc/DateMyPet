package com.example.jgchan.datemypet.entidades;
import java.util.List;
import com.squareup.moshi.Json;

public class Vacunas {

@Json(name = "vacunas")
private List<Vacuna> vacunas = null;
@Json(name = "vacuna")
private  Vacuna vacuna= null;

public List<Vacuna> getVacunas() {
return vacunas;
}

public void setVacunas(List<Vacuna> vacunas) {
this.vacunas = vacunas;
}

public Vacuna getVacuna() {
        return vacuna;
    }

public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
    }
}