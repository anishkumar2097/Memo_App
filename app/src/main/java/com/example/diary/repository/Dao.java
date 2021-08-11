package com.example.diary.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Query("SELECT *FROM Note")
    LiveData<List<Note>> getNotes();

    @Insert
    void insertNote(Note note);

    @Query("SELECT *FROM Note WHERE id=:id")
     Note getSingleNote(int id);
   @Update
  void update (Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM NOTE")
    void DeleteAllNotes();

}
