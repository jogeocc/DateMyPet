package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

public class Usuarios {

@Json(name = "usuario")
private Usuario usuario;
prtivate List<Usuario> usuarios;

public Usuario getUsuario() {
return usuario;
}

public void setUsuario(Usuario usuario) {
this.usuario = usuario;
}

public List<Usuario> getUsuarios() {
        return usuario;
    }

public void setUsuario(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}