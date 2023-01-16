package com.example.f107660;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    EditText editTextTitle;
    Button insertButton;
    Button viewAllNotestButton;
    String titleNoteSaveStr;
    String contentNoteSaveStr;
    MyDataBaseHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // инициираме MyDataBaseHelper в onCreate метода на MainActivity.java
        dbHelper = new MyDataBaseHelper(this);

        // отваряме БД за четене и запис
        database = dbHelper.getWritableDatabase();

        editText = findViewById(R.id.edit_text);
        editTextTitle = findViewById(R.id.edit_title);
        insertButton = findViewById(R.id.insert_button);
        viewAllNotestButton = findViewById(R.id.button_list_notes);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleNoteSaveStr = editTextTitle.getText().toString().trim();
                contentNoteSaveStr = editText.getText().toString().trim();

                if(editText.length() != 0 && editTextTitle.length() != 0) {

                    // добавяне на запис
                    String insertQuery = "INSERT INTO " +
                            MyDataBaseHelper.TABLE_NAME + " ("
                            + MyDataBaseHelper.COLUMN_TITLE_NAME + ","  + MyDataBaseHelper.COLUMN_NOTE_CONTENT  +") VALUES ('"
                            + titleNoteSaveStr + "','" + contentNoteSaveStr +"')";

                    database.execSQL(insertQuery);

//                    ContentValues values = new ContentValues();
//                    values.put(MyDataBaseHelper.COLUMN_USER_NAME, nameSaveStr);
//                    database.insert(MyDataBaseHelper.TABLE_NAME, null, values);

                    Intent intent = new Intent(MainActivity.this, DataActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.please_write_your_name), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewAllNotestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DataActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        // затваряме връзката с БД
        database.close();
        dbHelper.close();
        super.onDestroy();
    }

}