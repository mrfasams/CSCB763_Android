package com.example.f107660;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.f107660.entity.Note;
import com.example.f107660.fragment.NoteTextFragment;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Intent intent = getIntent();

        Note selectedNote = (Note) intent.getSerializableExtra("selectedNote");

        androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NoteTextFragment fragmentDemo =
                NoteTextFragment.newInstance(selectedNote.getId(),
                        selectedNote.getTitle(),
                        selectedNote.getContent());
        ft.replace(R.id.fragment_place, fragmentDemo);
        ft.commit();
    }
}
