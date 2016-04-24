package com.itgate.intellihome.service;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by root on 08/03/16.
 */
public interface IAuthenticate {
    @FormUrlEncoded/** il faut ajouter cette ligne pour envoyer des données POST au serveur*/
    @POST("/Sign_up.php")
    void checkUser(
            @Field("Email") String Email,
            @Field("Passwd") String Passwd,
            Callback<Response> callback);
}
