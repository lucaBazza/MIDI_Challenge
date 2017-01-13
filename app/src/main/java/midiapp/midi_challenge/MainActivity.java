package midiapp.midi_challenge;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.*;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Iterator;

import android.os.*;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import com.pdrogfer.mididroid.MidiFile;
import com.pdrogfer.mididroid.MidiTrack;
import com.pdrogfer.mididroid.event.MidiEvent;
import com.pdrogfer.mididroid.event.NoteOff;
import com.pdrogfer.mididroid.event.NoteOn;
import com.pdrogfer.mididroid.event.meta.Tempo;

public class MainActivity extends AppCompatActivity {
    //Static Declarations
    static TextView log, tv;

    static FunzioniDatabase funzioniDatabase = null;

    MidiFile mf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        funzioniDatabase = new FunzioniDatabase(this.getBaseContext());

        setContentView(R.layout.activity_main);
        mf = new MidiFile();

        tv = (TextView)findViewById(R.id.textView);  //Find the view by its id
        tv.setMovementMethod(new ScrollingMovementMethod());
        log = (TextView)findViewById(R.id.textViewLog);
        log.setTextColor(Color.RED);

        ActivityCompat.requestPermissions( MainActivity.this  ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        //Testing databse
        long  idNuovaRiga = funzioniDatabase.inserisci(new Utente("Meta","url/foto/foto.jpg",10,0));
        long  idNuovaRiga2 = funzioniDatabase.inserisci(new Utente("Meta","url/foto/foto.jpg",10,0));

        long idNuovoBrano = funzioniDatabase.inserisci(new Brano("Stairway","location/system/m.mp3",5));



        //TESTS. Forse è meglio spostarli nelle assert. Un giorno.
        /*Utente test = funzioniDatabase.trovaUtente(7);
        Utente test1 = funzioniDatabase.trovaUtente(8);
        Utente test2 = funzioniDatabase.trovaUtente(9);
        Utente testNull = funzioniDatabase.trovaUtente(1);*/
        Utente testNuovoInserimento = funzioniDatabase.trovaUtente(idNuovaRiga);
        Utente test241 = funzioniDatabase.trovaUtente(idNuovaRiga2);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    leggiFile();
                }
                else {
                    log.setText("Permessi lettura SD negati!");
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void leggiFile() {
        File sdcard = Environment.getExternalStorageDirectory();         // 1. Open up a MIDI file
        File input = new File(sdcard, "campanella.mid");
        //input = new File(sdcard,"happyBirthD.mid");

        try {
            mf = new MidiFile(input);
        } catch (IOException e) {
            System.err.println("Error parsing MIDI file:");
            e.printStackTrace();
            log.setText(e.toString());
            return;
        }
        AlgoritmoMidi Am = new AlgoritmoMidi();
        Am.loadFile(mf);
        Am.calc();
        // 2. Do some editing to the file
        // 2a. Strip out anything but notes from track 1 //sostituoti con traccia 0

    }
}
