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
import com.example.f107660.fragment.NoteTextFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    EditText editTextTitle;
    Button insertButton;
    Button viewAllNotestButton;
    String titleNoteSaveStr;
    String contentNoteSaveStr;
    MyDataBaseHelper dbHelper;
    SQLiteDatabase database;
    Button randomTextButton;
    Button randomQuoteButton;

    //TTS
    private TextToSpeech textToSpeech;
    private Button btn;




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
        randomTextButton = findViewById(R.id.random_button);
        randomQuoteButton = findViewById(R.id.random_button_quote);
        viewAllNotestButton = findViewById(R.id.button_list_notes);


        //try fragment

      /*  getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_place, new NoteTextFragment(), "SOMETAG").
                commit();

        NoteTextFragment noteTextFragment = (NoteTextFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_place);

        androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
       */
        //NoteTextFragment fragmentDemo = NoteTextFragment.newInstance(editTextTitle.getText().toString(), editText.getText().toString());
       //ft.replace(R.id.fragment_place, fragmentDemo);
        //ft.commit();

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

        randomTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetNotes().execute();
            }});

        randomQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetQuotes().execute();
            }});


        btn = (Button) findViewById(R.id.btn);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String data = editText.getText().toString();
                Log.i("TTS", "button clicked: " + data);
                int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);

                if (speechStatus == TextToSpeech.ERROR) {
                    Log.e("TTS", "Error in converting Text to Speech!");
                }
            }

        });

    }


    private class GetNotes extends AsyncTask<Void, Void, Void> {

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
            String url = "https://random-word-api.vercel.app/api?words=3";
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONArray listNotes = new JSONArray(jsonStr);
                    for(int i = 0 ; i < listNotes.length() - 1; i++) {
                        Note newNote = new Note();
                        newNote.setContent(listNotes.get(i).toString()) ;
                        newNote.setTitle(listNotes.get(i).toString());

                        insertNoteToDatabase(newNote);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
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
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }



}