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
        log = (TextView)findViewById(R.id.textViewLog);
        log.setTextColor(Color.RED);

        ActivityCompat.requestPermissions( MainActivity.this  ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
        File input = new File(sdcard,"campanella.mid");

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
        // 2a. Strip out anything but notes from track 1
        MidiTrack T = mf.getTracks().get(0);

        // It's a bad idea to modify a set while iterating, so we'll collect
        // the events first, then remove them afterwards
        Iterator<MidiEvent> it = T.getEvents().iterator();
        ArrayList<MidiEvent> eventsToRemove = new ArrayList<MidiEvent>();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(!E.getClass().equals(NoteOn.class) && !E.getClass().equals(NoteOff.class))
            {
                eventsToRemove.add(E);
                tv.append(E.toString());
            }
        }

        for(MidiEvent E : eventsToRemove)
        {
            T.removeEvent(E);
        }

        /*// 2b. Completely remove track 2
        mf.removeTrack(2);

        // 2c. Reduce the tempo by half
        T = mf.getTracks().get(0);

        it = T.getEvents().iterator();
        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(Tempo.class))
            {

                Tempo tempo = (Tempo) E;
                tempo.setBpm(tempo.getBpm() / 2);
            }
        }*/

    }
}
