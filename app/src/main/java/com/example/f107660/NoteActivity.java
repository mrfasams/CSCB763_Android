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

public class NoteActivity extends AppCompatActivity {

    TextView textViewTitle;
    TextView textViewContent;
    Button insertButton;
    String titleNoteSaveStr;
    String contentNoteSaveStr;
    SQLiteDatabase database;
    MyDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Intent intent = getIntent();

        Note selectedNote = (Note) intent.getSerializableExtra("selectedNote");
        textViewTitle  = findViewById(R.id.edit_title);
        textViewContent  = findViewById(R.id.edit_text);

        textViewTitle.setText(selectedNote.getTitle());
        textViewContent.setText(selectedNote.getContent());
        insertButton = findViewById(R.id.insert_button);
        // инициираме MyDataBaseHelper в onCreate метода на MainActivity.java
        dbHelper = new MyDataBaseHelper(this);

        // отваряме БД за четене и запис
        database = dbHelper.getWritableDatabase();

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleNoteSaveStr = textViewTitle.getText().toString().trim();
                contentNoteSaveStr = textViewContent.getText().toString().trim();
                    // обновявае на запис
                String updateQuery = "UPDATE " + MyDataBaseHelper.TABLE_NAME + " SET " + MyDataBaseHelper.COLUMN_TITLE_NAME + " = '"  + titleNoteSaveStr + "' , "
                        + MyDataBaseHelper.COLUMN_NOTE_CONTENT + " = '"  + contentNoteSaveStr +
                        "' WHERE " +  MyDataBaseHelper.UID + " = '" + selectedNote.getId() + "'";

                    database.execSQL(updateQuery);
                    Toast.makeText(getApplicationContext(), getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
