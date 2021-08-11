package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.diary.model.CustomViewModel;
import com.example.diary.repository.CustomDatabase;
import com.example.diary.repository.Note;
import com.example.diary.util.AppExecutors;
import com.example.diary.util.CustomItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton btn;
    RecyclerView recyclerView;
    List<Note>noteList=new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
   TextView emptyView;
    RecycleAdapter adapter;
    CustomDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         coordinatorLayout=findViewById(R.id.cordinator_layout);
    //  emptyView=findViewById(R.id.emptyView);


        recyclerView=findViewById(R.id.recylerView);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter=new RecycleAdapter(this,noteList);
        recyclerView.setAdapter(adapter);
        

        database=CustomDatabase.getInstance(getApplicationContext());
        SetUpViewModel();
        enableSwipeToDeleteAndUndo();
    }

    private void SetUpViewModel() {
        CustomViewModel viewModel= new ViewModelProvider(this).get(CustomViewModel.class);
        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteList=notes;

                adapter.setData(notes);

            }
        });
    }
    private  void enableSwipeToDeleteAndUndo() {

        CustomItemTouchHelper customItemTouchHelper=new CustomItemTouchHelper(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                 int position=viewHolder.getAdapterPosition();
                 final Note note=adapter.getNoteList().get(position);
                      adapter.removeItem(position);
                     removeFromDatabase(note,position);
                      Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapter.restoreItem(note, position);
                        recyclerView.scrollToPosition(position);
                        insertIntoDatabase(note,position);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(customItemTouchHelper);
        itemTouchhelper.attachToRecyclerView(recyclerView);

    }

    private void insertIntoDatabase(Note note, int position) {
           AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
               @Override
               public void run() {
                   database.userDao().insertNote(note);
               }
           });
    }

    private void removeFromDatabase(Note note ,int position) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.userDao().delete(note);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add){
            Intent i=new Intent(MainActivity.this,MainActivity2.class);
            i.putExtra("key",0);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
