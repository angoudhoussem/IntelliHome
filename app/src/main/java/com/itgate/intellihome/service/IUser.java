package com.itgate.intellihome.service;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface IUser{
    @FormUrlEncoded /** il faut ajouter cette ligne pour envoyer des données POST au serveur*/
    @POST("/insert_user.php")
    void insertUser(
            @Field("FullName") String FullName,
            @Field("Email") String Email,
            @Field("Passwd") String Passwd,
            @Field("PhoneNumber") String PhoneNumber,
            Callback<Response> callback);
}
