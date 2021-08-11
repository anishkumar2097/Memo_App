package com.example.diary.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities ={Note.class},version = 1,exportSchema =false)
public abstract   class CustomDatabase extends RoomDatabase {


    private static final String DATABASE_NAME="bahi_khata";
    private static CustomDatabase instance;


    public static synchronized  CustomDatabase getInstance(Context context){

        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),CustomDatabase.class,DATABASE_NAME)
               //     .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract Dao userDao();

}
