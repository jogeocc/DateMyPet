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
