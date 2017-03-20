package com.example.yhisl.app_realm.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by yhisl on 19/03/2017.
 */

public class Note extends RealmObject{

    @PrimaryKey
    private int id;
    @Required
    private String description;
    @Required
    private Date CreateDate;

    //realm necesita trabajar con un constructor vacio
    public Note(){

    }
    public Note(String description){
        this.id = 0;
        this.description = description;
        this.CreateDate = new Date();  //fecha del momento exacto
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

}
