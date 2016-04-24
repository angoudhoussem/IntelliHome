package com.itgate.intellihome.service;

import com.itgate.intellihome.model.Equipement;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface IEquipement {

    @GET("/select_equip.php")
    void getEquipement(@Query("limit") int limit, @Query("offset") int offset,
                       Callback<List<Equipement>> callback);
}

