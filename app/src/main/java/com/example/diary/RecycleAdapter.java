package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diary.repository.Note;

import java.util.List;
import java.util.Locale;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.holder> {
    TextToSpeech textToSpeech;
  List<Note> noteList;
    Context context;
    public RecycleAdapter(Context context,List<Note>noteList) {
        this.context=context;
        this.noteList=noteList;
        textToSpeech=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
               textToSpeech.setLanguage(Locale.UK);

            }
        });
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false);
       return new holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
          holder.textViewdes.setText(noteList.get(position).getDes());

          holder.textViewtitle.setText(noteList.get(position).getTitle());

          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i=new Intent(context,MainActivity2.class);
                  i.putExtra("key",1);
                  i.putExtra("id",noteList.get(position).getId());

                  i.putExtra("title",noteList.get(position).getTitle());
                  i.putExtra("description",noteList.get(position).getDes());
                  context.startActivity(i);

              }
          });
          holder.imageView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 holder.imageView.setImageResource(R.drawable.volume_on);
                  textToSpeech.speak(noteList.get(position).getDes(),TextToSpeech.QUEUE_FLUSH,null);
                     textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                         @Override
                         public void onStart(String utteranceId) {

                         }

                         @Override
                         public void onDone(String utteranceId) {
                              holder.imageView.setImageResource(R.drawable.volume_off);
                              textToSpeech.shutdown();
                         }

                         @Override
                         public void onError(String utteranceId) {

                         }
                     });

              }
          });

    }

    public List<Note>getNoteList(){
        return noteList;
    }

    @Override
    public int getItemCount() {

        return noteList.size();
    }


   void setData(List<Note> note){
        this.noteList=note;
        notifyDataSetChanged();

    }


    public void removeItem(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Note note, int position) {
        noteList.add(position, note);
        notifyItemInserted(position);
    }

    public class holder extends RecyclerView.ViewHolder{


        TextView textViewtitle,textViewdes;
        ImageView imageView;
        public holder(@NonNull View itemView) {
            super(itemView);
            textViewdes=itemView.findViewById(R.id.description);
            imageView=itemView.findViewById(R.id.imageView);
            textViewtitle=itemView.findViewById(R.id.text_title);
        }
    }

}
