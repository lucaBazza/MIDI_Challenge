package midiapp.midi_challenge;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
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
    TextView log, tv;
    MidiFile mf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mf = new MidiFile();

        tv = (TextView)findViewById(R.id.textView);  //Find the view by its id
        tv.setMovementMethod(new ScrollingMovementMethod());
        log = (TextView)findViewById(R.id.textViewLog);
        log.setTextColor(Color.RED);

        ActivityCompat.requestPermissions( MainActivity.this  ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        //Codice testing databsae

        DatabaseApp dbHandler = new DatabaseApp(this);
        dbHandler.addUtente(new Utente(6,"Paolo","foto.jpg",-1,-1));
        dbHandler.selectUtenteById(4);
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

    void leggiFile(){
        // 1. Open up a MIDI file
        File sdcard = Environment.getExternalStorageDirectory();
        //File input = new File(sdcard,"campanella.mid");
        File input = new File(sdcard,"happyBirthD.mid");

        try
        {
            mf = new MidiFile(input);
        }
        catch(IOException e)
        {
            System.err.println("Error parsing MIDI file:");
            e.printStackTrace();
            log.setText(e.toString());
            return;
        }

        // 2. Do some editing to the file
        // 2a. Strip out anything but notes from track 1 //sostituoti con traccia 0

        MidiTrack T = mf.getTracks().get(0);

        // It's a bad idea to modify a set while iterating, so we'll collect
        // the events first, then remove them afterwards
        Iterator<MidiEvent> it = T.getEvents().iterator();

        int count =0;
        while(it.hasNext())
        {
            MidiEvent E = it.next();
            if(E.getClass().equals(NoteOn.class))
            {
                int i = ((NoteOn)E).getNoteValue();
                if(((NoteOn) E).getVelocity()!=0) { //non è bellissimo, solo perchè ci sono note "fantasma"
                    String vel =Integer.toString(((NoteOn) E).getVelocity());
                    tv.append("\t\t"+ Integer.toString(count)+ " : "+ convIntStrNota(i)+"\t\t\t" + E.toString() +"\t\t vel: "+ vel);
                    //if(count%4==0) //organizzazione a tabella
                        tv.append("\n");
                    count++;
                }
            }

        }
    }

    String convIntStrNota(int i){
        String[] note = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        String result = note[(i)%12];
        String octave = Integer.toString((i/12)-2);
        return result+octave;
    }
}
