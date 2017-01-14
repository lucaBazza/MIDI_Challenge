package midiapp.midi_challenge;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.*;
import android.os.*;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pdrogfer.mididroid.MidiFile;

public class MainActivity extends AppCompatActivity {
    //Static Declarations
    static TextView log, tv;

    private String[] mActivityTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    static FunzioniDatabase funzioniDatabase = null;

    MidiFile mf;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        funzioniDatabase = new FunzioniDatabase(this.getBaseContext());

        setContentView(R.layout.activity_main);
        mf = new MidiFile();

        tv = (TextView)findViewById(R.id.textView);  //Find the view by its id
        tv.setMovementMethod(new ScrollingMovementMethod());
        log = (TextView)findViewById(R.id.textViewLog);
        log.setTextColor(Color.RED);

        ActivityCompat.requestPermissions( MainActivity.this  ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        mActivityTitles = new String[]{"act1,act2,act3"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // Set the adapter for the list view
        ArrayAdapter<String> xxxx  = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mActivityTitles);
        mDrawerList.setAdapter(xxxx);
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast t = Toast.makeText(getBaseContext(),"EHEH",Toast.LENGTH_LONG);
            }
        });


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
