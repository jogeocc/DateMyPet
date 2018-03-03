package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

public class Mascota {

@Json(name = "id")
private Integer id;
@Json(name = "idUsuario")
private Integer idUsuario;
@Json(name = "masNombre")
private String masNombre;
@Json(name = "masRaza")
private String masRaza;
@Json(name = "masTipo")
private String masTipo;
@Json(name = "masSexo")
private String masSexo;
@Json(name = "masEdad")
private Integer masEdad;
@Json(name = "masSenaPart")
private String masSenaPart;
@Json(name = "masFoto")
private String masFoto;
@Json(name = "masHobbie")
private String masHobbie;
@Json(name = "masCompPerf")
private Integer masCompPerf;
@Json(name = "masActivo")
private Integer masActivo;
@Json(name = "created_at")
private String createdAt;
@Json(name = "updated_at")
private String updatedAt;

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

public String getMasNombre() {
return masNombre;
}

public void setMasNombre(String masNombre) {
this.masNombre = masNombre;
}

public String getMasRaza() {
return masRaza;
}

public void setMasRaza(String masRaza) {
this.masRaza = masRaza;
}

public String getMasTipo() {
return masTipo;
}

public void setMasTipo(String masTipo) {
this.masTipo = masTipo;
}

public String getMasSexo() {
return masSexo;
}

public void setMasSexo(String masSexo) {
this.masSexo = masSexo;
}

public Integer getMasEdad() {
return masEdad;
}

public void setMasEdad(Integer masEdad) {
this.masEdad = masEdad;
}

public String getMasSenaPart() {
return masSenaPart;
}

public void setMasSenaPart(String masSenaPart) {
this.masSenaPart = masSenaPart;
}

public String getMasFoto() {
return masFoto;
}

public void setMasFoto(String masFoto) {
this.masFoto = masFoto;
}

public String getMasHobbie() {
return masHobbie;
}

public void setMasHobbie(String masHobbie) {
this.masHobbie = masHobbie;
}

public Integer getMasCompPerf() {
return masCompPerf;
}

public void setMasCompPerf(Integer masCompPerf) {
this.masCompPerf = masCompPerf;
}

public Integer getMasActivo() {
return masActivo;
}

public void setMasActivo(Integer masActivo) {
this.masActivo = masActivo;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

}