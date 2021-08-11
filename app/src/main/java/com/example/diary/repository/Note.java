package com.example.diary.repository;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    String des;

   public Note(int id,String title,String des){
        this.id=id;
        this.title=title;
        this.des=des;
    }

    @Ignore
    public Note(String title,String des){
       this.title=title;
       this.des=des;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
