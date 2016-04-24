package com.itgate.intellihome.model;

import java.io.Serializable;

/**
 * Created by root on 01/03/16.
 */
public class Equipement implements Serializable {

    long Id_eq;
    String Name_eq;
    boolean Status_eq;
    String Desc_eq;

    public Equipement(long id_eq, String name_eq, boolean status_eq, String desc_eq) {
        Id_eq = id_eq;
        Name_eq = name_eq;
        Status_eq = status_eq;
        Desc_eq = desc_eq;
    }

    public String getName_eq() {
        return Name_eq;
    }

    public void setName_eq(String name_eq) {
        Name_eq = name_eq;
    }

    public long getId_eq() {
        return Id_eq;
    }

    public void setId_eq(long id_eq) {
        Id_eq = id_eq;
    }

    public boolean isStatus_eq() {
        return Status_eq;
    }

    public void setStatus_eq(boolean status_eq) {
        Status_eq = status_eq;
    }

    public String getDesc_eq() {
        return Desc_eq;
    }

    public void setDesc_eq(String desc_eq) {
        Desc_eq = desc_eq;
    }
}
