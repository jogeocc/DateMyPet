package com.example.jgchan.datemypet.conexion;

import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.Mascota;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.ParseoToken;
import com.example.jgchan.datemypet.entidades.Registros;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Usuarios;
import com.example.jgchan.datemypet.entidades.Vacunas;
import com.example.jgchan.datemypet.entidades.Veterinario;
import com.example.jgchan.datemypet.entidades.Veterinarios;
import com.facebook.stetho.inspector.network.ResponseBodyData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by jgchan on 26/02/18.
 */

public interface apiService {


    @POST("registrar") //http://dominion.tk/api/
    @FormUrlEncoded
    /*'username',
      'nombre',
      'correo',
      "direccion",
       "telefono",
       "celular",
       'password'*/
    Call<ParseoToken> registrar(@Field("username") String username ,
                                @Field("nombre") String nombre,
                                @Field("correo") String correo,
                                @Field("direccion") String direccion,
                                @Field("telefono") String telefono,
                                @Field("celular") String celular,
                                @Field("password") String password);


    @POST("ingresar")
    @FormUrlEncoded
    Call<ParseoToken> ingresar(@Field("username") String username, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<ParseoToken> login(@Field("username") String username, @Field("password") String password);

    @POST("logintemp")
    @FormUrlEncoded
    Call<ParseoToken> ingresarfake(@Field("username") String username, @Field("password") String password);

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);

    @GET("citas/{idUsuario}/listado")
    Call<Citas> miscitas(@Path("idUsuario") String idUsuario);

    @GET("citas/{idUsuario}/miscitas")
    Call<Citas> miscitascompletas(@Path("idUsuario") String idUsuario);

    @GET("usuario/{idUsuario}/ver")
    Call<Usuarios> usuario(@Path("idUsuario") String idUsuario);

    @GET("usuario/{idUsuario}/editar")
    Call<Usuarios> traerdatosUsuario(@Path("idUsuario") String idUsuario);

    @GET("usuario/{idUsuario}/eliminarcuenta")
    Call<Success> eliminarCuenta(@Path("idUsuario") String idUsuario);

    @PUT("usuario/{idUsuario}/actualizar") //http://dominion.tk/api/
    @FormUrlEncoded
    Call<Success> actualizar(   @Path("idUsuario") String idUsuario,
                                @Field("username") String username ,
                                @Field("nombre") String nombre,
                                @Field("correo") String correo,
                                @Field("direccion") String direccion,
                                @Field("telefono") String telefono,
                                @Field("celular") String celular);

    @GET("mismascotas/{idUsuario}/listado")
    Call<Mascotas> mismascotas(@Path("idUsuario") String idUsuario);

    @GET("mascota/{idMascota}/visualizar")
    Call<Mascotas> infoMascota(@Path("idMascota") String idMascota);

    @GET("mascota/{idMascota}/compartir")
    Call<Success> compartirPerfil(@Path("idMascota") String idMascota);

    @GET("mascota/{idMascota}/eliminar")
    Call<Success> eliminarMascota(@Path("idMascota") String idMascota);

    @POST("mascota/crear/nueva")
    @Multipart
    Call<Success> registrarMascota(
                                    @Part("idUsuario") RequestBody idUsuario,
                                    @Part MultipartBody.Part fotoRegistro,
                                    @Part("masNombre") RequestBody masNombre,
                                    @Part("masRaza")RequestBody masRaza,
                                    @Part("masTipo")RequestBody masTipo,
                                    @Part("masSexo")RequestBody masSexo,
                                    @Part("masEdad")RequestBody masEdad,
                                    @Part("masSenaPart")RequestBody masSenaPart,
                                    @Part("masHobbie")RequestBody masHobbie);

    @POST("mascota/actualizar")
    @Multipart
    Call<Success> editarMascota(
            @Part("idMascota") RequestBody idMascota,
            @Part MultipartBody.Part fotoRegistro,
            @Part("masNombre") RequestBody masNombre,
            @Part("masRaza")RequestBody masRaza,
            @Part("masTipo")RequestBody masTipo,
            @Part("masSexo")RequestBody masSexo,
            @Part("masEdad")RequestBody masEdad,
            @Part("masSenaPart")RequestBody masSenaPart,
            @Part("masHobbie")RequestBody masHobbie);

    @GET("veterinarios/{idUsuario}/listado")
    Call<Veterinarios> misveterinarios(@Path("idUsuario") String idUsuario);

    @GET("usuario/{idUsuario}/tiene-mascotas")
    Call<Success> tieneMascotas(@Path("idUsuario") String idUsuario);

    @POST("veterinarios/crear/nueva")
    @FormUrlEncoded
    Call<Success> registrarVeterinario(
            @Field("idUsuario")  String idUsuario,
            @Field("vetNomVeterinaria")  String nomVeterinaria,
            @Field("vetNombre") String nomVeterinario,
            @Field("vetDireccion") String direccionVet,
            @Field("vetTelefono")  String telefono);

    @GET("veterinarios/{idVeterinario}/visualizar")
    Call<Veterinarios> infoVeterinario(@Path("idVeterinario") String idVeterinario);

    @GET("veterinarios/{idVeterinario}/eliminar")
    Call<Success> eliminarVeterinario(@Path("idVeterinario") String idVeterinario);

    @PUT("veterinarios/{idVeterinario}/actualizar")
    @FormUrlEncoded
    Call<Success> actualizarVeterinario(
            @Path("idVeterinario") String idVeterinario,
            @Field("vetNomVeterinaria")  String nomVeterinaria,
            @Field("vetNombre") String nomVeterinario,
            @Field("vetDireccion") String direccionVet,
            @Field("vetTelefono")  String telefono);


    @GET("usuario/{idUsuario}/tiene-veterinarios")
    Call<Success> tieneVeterinario(@Path("idUsuario") String idUsuario);


    @POST("citas/crear/nueva")
    @FormUrlEncoded
    Call<Success> registrarCita(
            @Field("idMascota") String idMascota,
            @Field("idVeterinario") String idVeterinario,
            @Field("ciFecha") String ciFecha,
            @Field("ciHora") String ciHora,
            @Field("ciTipo") String ciTipo,
            @Field("ciNota") String ciNota);


    @GET("citas/{idCita}/editar")
    Call<Citas> visualizarEditarCita(
            @Path("idCita") String idCita);

    @GET("citas/{idCita}/visualizar")
    Call<Citas> visualizarCita(
            @Path("idCita") String idCita);


    @PUT("citas/{idCita}/actualizar")
    @FormUrlEncoded
    Call<Success> actualizarCita(
            @Path("idCita") String idCita,
            @Field("idMascota") String idMascota,
            @Field("idVeterinario") String idVeterinario,
            @Field("ciFecha") String ciFecha,
            @Field("ciHora") String ciHora,
            @Field("ciTipo") String ciTipo,
            @Field("ciNota") String ciNota);

    @GET("citas/{idCita}/eliminar")
    Call<Success> eliminarCita(
            @Path("idCita") String idCita);


    @POST("vacuna/crear/nueva")
    @FormUrlEncoded
    Call<Success> registrarVacuna(
            @Field("idMascota") String idMascota,
            @Field("vaNombre") String vaNombre,
            @Field("vaFecha") String vaFecha,
            @Field("vaNota") String vaNota);

    @GET("mascota/{idMascota}/vacunas")
    Call<Vacunas> verListadoVacuna(
            @Path("idMascota") String idMascota);

    @GET("vacuna/{idVacuna}/eliminar")
    Call<Success> eliminarVacuna(
            @Path("idVacuna") String idVacuna);

    @GET("generar/{idMascota}/historial")
    Call<ResponseBody> descargarPdf(
            @Path("idMascota") String mascota);


    @PUT("vacuna/{idVacuna}/actualizar")
    @FormUrlEncoded
    Call<Success> editarVacuna(
            @Path("idVacuna") String idVacuna,
            @Field("idMascota") String idMascota,
            @Field("vaNombre") String vaNombre,
            @Field("vaFecha") String vaFecha,
            @Field("vaNota") String vaNota);

    @GET("vacuna/{idVacuna}/visualizar")
    Call<Vacunas> verVacuna(
        @Path("idVacuna") String idVacuna
    );



    @GET("historialmedico/{idHistorial}/registros")
    Call<Registros> susRegistrosMedicos(
            @Path("idHistorial") String idMascota
    );



}
