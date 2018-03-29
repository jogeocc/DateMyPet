package com.example.jgchan.datemypet;

import android.content.SharedPreferences;

import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.ParseoToken;

/**
 * Created by jgchan on 26/02/18.
 */

public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    static synchronized TokenManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(ParseoToken token){
        editor.putString("ACCESS_TOKEN", token.getSuccess().getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN", token.getSuccess().getAccessToken()).commit();
        editor.putString("ID", String.valueOf(token.getSuccess().getId())).commit();
        editor.putString("USER", token.getSuccess().getUsername()).commit();
        editor.putString("NAME", String.valueOf(token.getSuccess().getName())).commit();
        editor.putString("EMAIL", token.getSuccess().getEmail()).commit();
        editor.putString("REMEMBER_TOKEN", token.getSuccess().getRemember_token()).commit();
    }


    public void auxSaveFoto(String ruta){
        editor.putString("AUXFOTO", ruta).commit();
    }

    public String getSaveFoto(){
       return prefs.getString("AUXFOTO", null);
    }

    public void eliminarSaveFoto(){
        editor.remove("AUXFOTO").commit();
    }

    public void guardarActualizacion(String usuario, String nombre, String email){
        editor.putString("USER", usuario).commit();
        editor.putString("NAME", nombre).commit();
        editor.putString("EMAIL", email).commit();
    }

    public void deleteToken(){
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();
    }

    public void deleteRemember(){
        editor.remove("REMEMBER_TOKEN").commit();
    }

    public void eliminoCuenta(){
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();
        editor.remove("ID").commit();
        editor.remove("USER").commit();
        editor.remove("NAME").commit();
        editor.remove("EMAIL").commit();
    }

    public AccessToken getToken(){
        AccessToken token = new AccessToken();
        token.setAccessToken(prefs.getString("ACCESS_TOKEN", null));
        token.setRefreshToken(prefs.getString("REFRESH_TOKEN", null));
        token.setId_user(prefs.getString("ID", null));
        token.setName_user(prefs.getString("USER", null));
        token.setName(prefs.getString("NAME", null));
        token.setRemember_token(prefs.getString("REMEMBER_TOKEN", null));
        token.setEmail(prefs.getString("EMAIL", null));
        return token;
    }



}
