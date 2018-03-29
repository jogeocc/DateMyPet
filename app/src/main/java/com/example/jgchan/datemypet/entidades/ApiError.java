package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

import java.util.List;
import java.util.Map;

/**
 * Created by jgchan on 26/02/18.
 */

public class ApiError {

    @Json(name = "remember_token")
    private String rememberToken;
    @Json(name = "mensaje")
    private String mensaje;

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }



    String message;
         Map<String, List<String>> errors;

         public String getMessage() {
             return message;
         }

         public Map<String, List<String>> getErrors() {
             return errors;
         }


}
