package com.example.jgchan.datemypet.entidades;

import com.squareup.moshi.Json;

/**
 * Created by jgchan on 26/02/18.
 */

public class AccessToken {

    @Json(name ="token_type")

    String tokenType;

    @Json(name = "expires_in")
    int expiresInt;

    @Json(name ="access_token")
    String accessToken;

    @Json(name ="id_user")
    String id_user;

    @Json(name ="name_user")
    String name_user;

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    @Json(name="refresh_token")
    String refreshToken;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresInt() {
        return expiresInt;
    }

    public void setExpiresInt(int expiresInt) {
        this.expiresInt = expiresInt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getId_user(String id) {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }



    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
