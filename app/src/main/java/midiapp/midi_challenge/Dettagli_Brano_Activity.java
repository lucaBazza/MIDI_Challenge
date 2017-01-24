package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author lucabazzanella
 */
public class Dettagli_Brano_Activity extends AppCompatActivity {
    public Brano brano = null;
    public Utente utente =null;
    AlgoritmoMidi alMidi;
    MidiFile mf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli__brano);

        Button btnBrano = (Button) findViewById(R.id.buttonProvaCaricaBrano);
        btnBrano.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.println(Log.ASSERT,"OnlickPorva","OnlickPorva");
                eseguiAlgo();
            }
        });

        if(brano!=null) setTitle("Dettagli brano: "+brano.getTitolo());
        else setTitle("Dettagli brano: non disponibile!");
    }
    public void caricaBranoUtente(Brano brano, Utente utente){
        this.brano= brano;
        this.utente=utente;
    }
    private void eseguiAlgo(){
        ActivityCompat.requestPermissions( this  ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        if(mf!=null) {
            alMidi = new AlgoritmoMidi(mf);
            List<String> out = alMidi.calc();
            Iterator i = out.iterator();
            Log.println(Log.ASSERT,"Out Algo", "Algo Concluso!");
            while(i.hasNext()) Log.println(Log.ASSERT,"Out Algo", i.toString());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {  //chiede permessi lettura SD
        switch (requestCode) {
            case 1: { // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted, yay! Do the contacts-related task you need to do.
                    File sdcard = Environment.getExternalStorageDirectory();         // apro MIDI file
                    File input = new File(sdcard, "campanella.mid");
                    try {  mf = new MidiFile(input); }
                    catch (IOException e) {
                        System.err.println("Error parsing MIDI file:");
                        e.printStackTrace();        //log.setText(e.toString());
                        mf=null; return;
                    }
                }
                else {   // permission denied, boo! Disable the functionality that depends on this permission.
                    Log.println(Log.ASSERT,"Errore lettura","Permessi lettura SD negati!"); }
                return;
            }  // other 'case' lines to check for other permissions this app might request
        }
    }

}
