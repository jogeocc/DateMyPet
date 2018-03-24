package com.example.jgchan.datemypet.entidades;
import com.squareup.moshi.Json;

public class Veterinario {

@Json(name = "id")
private Integer id;
@Json(name = "idUsuario")
private Integer idUsuario;
@Json(name = "vetNombre")
private String vetNombre;
@Json(name = "vetDireccion")
private String vetDireccion;
@Json(name = "vetTelefono")
private String vetTelefono;
@Json(name = "vetNomVeterinaria")
private String vetNomVeterinaria;
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

public Integer getIdUsuario() {
return idUsuario;
}

public void setIdUsuario(Integer idUsuario) {
this.idUsuario = idUsuario;
}

public String getVetNombre() {
return vetNombre;
}

public void setVetNombre(String vetNombre) {
this.vetNombre = vetNombre;
}

public String getVetDireccion() {
return vetDireccion;
}

public void setVetDireccion(String vetDireccion) {
this.vetDireccion = vetDireccion;
}

public String getVetTelefono() {
return vetTelefono;
}

public void setVetTelefono(String vetTelefono) {
this.vetTelefono = vetTelefono;
}

public String getVetNomVeterinaria() {
return vetNomVeterinaria;
}

public void setVetNomVeterinaria(String vetNomVeterinaria) {
this.vetNomVeterinaria = vetNomVeterinaria;
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
