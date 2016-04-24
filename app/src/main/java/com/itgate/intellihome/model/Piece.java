package com.itgate.intellihome.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 01/03/16.
 */
public class Piece implements Serializable {

    long id;
    String titre;
    String image;
    String description;
    List<Equipement> equipementList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Equipement> getEquipementList() {
        return equipementList;
    }

    public void setEquipementList(List<Equipement> equipementList) {
        this.equipementList = equipementList;
    }

    public void setDescription(String description) {
        description = description;
    }


}
