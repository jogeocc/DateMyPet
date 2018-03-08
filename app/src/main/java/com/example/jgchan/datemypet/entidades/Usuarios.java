package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

import java.util.List;

public class Usuarios {

@Json(name = "usuario")
private Usuario usuario;
private List<Usuario> usuarios;

public Usuario getUsuario() {
return usuario;
}

public void setUsuario(Usuario usuario) {
this.usuario = usuario;
}

public void setUsuarios(List<Usuario> usuarios){
    this.usuarios = usuarios;
}

}