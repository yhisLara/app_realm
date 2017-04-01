package com.example.yhisl.app_realm.models;

import com.example.yhisl.app_realm.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by yhisl on 19/03/2017.
 */

public class Board extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private Date CreateDate;

    //esto crea la relación entre notas y pizarra
    private RealmList<Note> notes;


    public Board(){

    }
    public Board(String title){
        this.id = MyApplication.BoardID.incrementAndGet();
        this.title = title;
        this.CreateDate = new Date();
        this.notes = new RealmList<Note>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

}
