package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

public class Usuario {

@Json(name = "id")
private Integer id;
@Json(name = "username")
private String username;
@Json(name = "nombre")
private String nombre;
@Json(name = "correo")
private String correo;
@Json(name = "direccion")
private String direccion;
@Json(name = "telefono")
private String telefono;
@Json(name = "celular")
private String celular;
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

public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}

public String getNombre() {
return nombre;
}

public void setNombre(String nombre) {
this.nombre = nombre;
}

public String getCorreo() {
return correo;
}

public void setCorreo(String correo) {
this.correo = correo;
}

public String getDireccion() {
return direccion;
}

public void setDireccion(String direccion) {
this.direccion = direccion;
}

public Object getTelefono() {
return telefono;
}

public void setTelefono(Object telefono) {
this.telefono = telefono;
}

public String getCelular() {
return celular;
}

public void setCelular(String celular) {
this.celular = celular;
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