package com.example.diary.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.diary.repository.CustomDatabase;
import com.example.diary.repository.Note;

import java.util.List;

public class CustomViewModel extends AndroidViewModel {

     LiveData<List<Note>> notes;
    // CustomDatabase database;
    public CustomViewModel(@NonNull Application application) {
        super(application);
      CustomDatabase database= CustomDatabase.getInstance(this.getApplication());
       notes=database.userDao().getNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }
}
