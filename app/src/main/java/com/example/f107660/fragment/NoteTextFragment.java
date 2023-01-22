package com.example.f107660.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.f107660.DataActivity;
import com.example.f107660.MyDataBaseHelper;
import com.example.f107660.NoteActivity;
import com.example.f107660.R;

public class NoteTextFragment extends Fragment {
    TextView textViewTitle;
    TextView textViewContent;
    Button insertButton;
    String titleNoteSaveStr;
    String contentNoteSaveStr;
    SQLiteDatabase database;
    MyDataBaseHelper dbHelper;
    Button deleteButton;

    public NoteTextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        EditText editTitle = (EditText) view.findViewById(R.id.edit_title_fragment);
        EditText editContent = (EditText) view.findViewById(R.id.edit_content_fragment);
        if (this.getArguments() != null && !this.getArguments().isEmpty()) {
            editTitle.setText(this.getArguments().get("edit_title_fragment").toString());
            editContent.setText(this.getArguments().get("edit_content_fragment").toString());

            String selectedId = this.getArguments().get("id").toString();

            textViewTitle = view.findViewById(R.id.edit_title_fragment);
            textViewContent = view.findViewById(R.id.edit_content_fragment);

            insertButton = view.findViewById(R.id.insert_button);
            // инициираме MyDataBaseHelper в onCreate метода на MainActivity.java
            dbHelper = new MyDataBaseHelper(this.getContext());

            // отваряме БД за четене и запис
            database = dbHelper.getWritableDatabase();

            insertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleNoteSaveStr = textViewTitle.getText().toString().trim();
                    contentNoteSaveStr = textViewContent.getText().toString().trim();
                    // обновявае на запис
                    String updateQuery = "UPDATE " + MyDataBaseHelper.TABLE_NAME + " SET " + MyDataBaseHelper.COLUMN_TITLE_NAME + " = '" + titleNoteSaveStr + "' , "
                            + MyDataBaseHelper.COLUMN_NOTE_CONTENT + " = '" + contentNoteSaveStr +
                            "' WHERE " + MyDataBaseHelper.UID + " = '" + selectedId + "'";

                    database.execSQL(updateQuery);
                    Toast.makeText(getContext().getApplicationContext(), getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
                }
            });


        deleteButton = view.findViewById(R.id.delete_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteQuery = "DELETE FROM  " + MyDataBaseHelper.TABLE_NAME +
                        " WHERE " + MyDataBaseHelper.UID + " = '" + selectedId + "'";

                database.execSQL(deleteQuery);
                Toast.makeText(getContext().getApplicationContext(), getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), DataActivity.class);
                startActivity(intent);
            }
        });
        }
    }

    /// Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static NoteTextFragment newInstance(String id, String title, String content) {
        NoteTextFragment fragmentDemo = new NoteTextFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("edit_title_fragment", title);
        args.putString("edit_content_fragment", content);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }
}
