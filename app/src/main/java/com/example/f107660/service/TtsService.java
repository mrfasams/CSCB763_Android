package com.example.f107660.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.f107660.MyDataBaseHelper;
import com.example.f107660.entity.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TtsService extends Service implements TextToSpeech.OnInitListener{

    private String str = "";
    private TextToSpeech mTts;
    private static final String TAG="TtsService";

    MyDataBaseHelper dbHelper;
    SQLiteDatabase database;

    @Override

    public IBinder onBind(Intent arg0) {

        return null;
    }


    @Override
    public void onCreate() {

        mTts = new TextToSpeech(this,
                this  // OnInitListener
        );
        mTts.setSpeechRate(0.5f);
        Log.v(TAG, "oncreate_service");
       // str ="turn left please ";
        str = getDataToRead();
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {


        readAllNotes(str);

        Log.v(TAG, "onstart_service");
        super.onStart(intent, startId);
    }

    @Override
    public void onInit(int status) {
        Log.v(TAG, "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v(TAG, "Language is not available.");
            } else {

                readAllNotes(str);

            }
        } else {
            Log.v(TAG, "Could not initialize TextToSpeech.");
        }
    }
    private void readAllNotes(String str) {
        mTts.speak(str,
                TextToSpeech.QUEUE_FLUSH,
                null);
    }

    private String getDataToRead() {

        List<Note> dataList = new ArrayList<>();
        dbHelper = new MyDataBaseHelper(this);
        database = dbHelper.getReadableDatabase();
        Cursor dataCursor = database.rawQuery("SELECT * FROM " + MyDataBaseHelper.TABLE_NAME, null);

        if (dataCursor.getCount() == 0) {
            return "Note list is empty";
        } else {
            while (dataCursor.moveToNext()) {
                Note note = new Note();
                note.setId(dataCursor.getString(dataCursor.getColumnIndexOrThrow(MyDataBaseHelper.UID)));
                note.setTitle(dataCursor.getString(dataCursor.getColumnIndexOrThrow(MyDataBaseHelper.COLUMN_TITLE_NAME)));
                note.setContent(dataCursor.getString(dataCursor.getColumnIndexOrThrow(MyDataBaseHelper.COLUMN_NOTE_CONTENT)));
                dataList.add(note);

            }

            dataCursor.close();
            // Accumulate names into a List
            String allNoteData = "";
            for (Note n : dataList) {
                allNoteData = allNoteData + n.toString();
            }

            return allNoteData;
        }
    }
}
