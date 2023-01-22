package com.example.f107660;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.f107660.entity.Note;
import com.example.f107660.service.TtsService;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText;
    EditText editTextTitle;
    Button insertButton;
    Button viewAllNotestButton;
    String titleNoteSaveStr;
    String contentNoteSaveStr;
    MyDataBaseHelper dbHelper;
    SQLiteDatabase database;
    Button randomQuoteButton;

    // declaring objects of Button class service
    private Button start, stop;


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
        randomQuoteButton = findViewById(R.id.random_button_quote);
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

        randomQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetQuotes().execute();
            }});

        // assigning ID of startButton
        // to the object start
        start = (Button) findViewById( R.id.startButton );

        // assigning ID of stopButton
        // to the object stop
        stop = (Button) findViewById( R.id.stopButton );

        // declaring listeners for the
        // buttons to make them respond
        // correctly according to the process
        start.setOnClickListener( this );
        stop.setOnClickListener( this );

    }


    private class GetQuotes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            //String url = "https://random-word-api.herokuapp.com/word";//working
            String url = "https://zenquotes.io/api/random";
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONArray listNotes = new JSONArray(jsonStr);
                    JSONObject reader = new JSONObject(listNotes.get(0).toString());
                    String quote = reader.getString("q");
                    String author = reader.getString("a");
                    Note newNote = new Note();
                    newNote.setTitle(author);
                    newNote.setContent(quote);

                    insertNoteToDatabase(newNote);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    private void insertNoteToDatabase(Note newNote) {
        // добавяне на запис
        String insertQuery = "INSERT INTO " +
                MyDataBaseHelper.TABLE_NAME + " ("
                + MyDataBaseHelper.COLUMN_TITLE_NAME + ","  + MyDataBaseHelper.COLUMN_NOTE_CONTENT  +") VALUES ('"
                + newNote.getTitle() + "','" + newNote.getContent() +"')";

        database.execSQL(insertQuery);
    }

    @Override
    protected void onDestroy() {
        // затваряме връзката с БД
        database.close();
        dbHelper.close();
        super.onDestroy();
    }

    public void onClick(View view) {

        // process to be performed
        // if start button is clicked
        if(view == start){

            // starting the service
            startService(new Intent( this, TtsService.class ) );
        }

        // process to be performed
        // if stop button is clicked
        else if (view == stop){

            // stopping the service
            stopService(new Intent( this, TtsService.class ) );

        }
    }

}