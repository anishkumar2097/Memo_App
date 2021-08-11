package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diary.repository.CustomDatabase;
import com.example.diary.repository.Note;
import com.example.diary.util.AppExecutors;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {


    private static final int REQUEST_CODE_SPEECH_INPUT =100 ;
    EditText editTextTitle,editTextDes;
    CustomDatabase database;
    int id;
    int i1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editTextDes=findViewById(R.id.edit_txt_desp);
        editTextTitle=findViewById(R.id.editText_title);
     Intent i=getIntent();
    i1= i.getIntExtra("key",-1);
        if(getIntent().getIntExtra("key",-1)==1 ){
            String title=getIntent().getStringExtra("title");
            id=getIntent().getIntExtra("id",0);
              String description=getIntent().getStringExtra("description");
            editTextDes.setText(description);
            editTextTitle.setText(title);
           getSupportActionBar().setTitle("Edit");
        }
        else {
            getSupportActionBar().setTitle("Add Note");
        }
        database=CustomDatabase.getInstance(this);

    }


    private void save() {
        String TextTitle = editTextTitle.getText().toString().trim();
        String des = editTextDes.getText().toString().trim();

        if(i1==0&&!TextTitle.isEmpty()&&!des.isEmpty()) {


            Note note = new Note(TextTitle, des);

            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {

                   database.userDao().insertNote(note);
                }
            });

        }else if(i1==1&&!TextTitle.isEmpty()&&!des.isEmpty()) {
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
               Note note=  database.userDao().getSingleNote(id);
               note.setDes(des);
               note.setTitle(TextTitle);
               database.userDao().update(note);
                }
            });
        }
        else{
            Toast.makeText(this,"No content to save",Toast.LENGTH_LONG).show();
        }
        editTextTitle.setText("");
        editTextDes.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.save){
            save();
            finish();
            return true;
        }
        if(item.getItemId()==R.id.mic){
            SpeechToText();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SpeechToText() {

        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e) {
            Toast
                    .makeText(MainActivity2.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable
                                     Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                int id = getCurrentFocus().getId();
                View view = getCurrentFocus();
                getCurrentFocus().findViewById(R.id.edit_txt_desp);
                if (getCurrentFocus().getId() == R.id.edit_txt_desp) {
                    editTextDes.setText(
                            Objects.requireNonNull(result).get(0));
                } else {
                    editTextTitle.setText(Objects.requireNonNull(result).get(0));
                }
            }
        }
    }
}



