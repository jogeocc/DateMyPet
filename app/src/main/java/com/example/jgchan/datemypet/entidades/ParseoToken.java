//com.example.jgchan.datemypet.entidades.ParseoToken.java-----------------------------------

package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;


public class ParseoToken {

    @Json(name = "success")
    private Success success;

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

}