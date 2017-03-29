package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private String dir = "MidiChallenge/";
    AlgoritmoMidi alMidi;
    MidiFile mf;
    TextView tvLog;



    private static FunzioniDatabase funzioniDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli__brano);

        long l = getIntent().getLongExtra("id_brano",0);

        funzioniDatabase = new FunzioniDatabase(getBaseContext());
        brano = funzioniDatabase.trovaBrano(l);

        Button btnBrano = (Button) findViewById(R.id.buttonProvaCaricaBrano);
        btnBrano.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.println(Log.ASSERT,"OnClick","BTNbrano_dettagliBranoActivity");
                eseguiAlgo();
            }
        });
        if(brano!=null) setTitle("Dettagli brano: "+brano.getTitolo());
        else setTitle("Dettagli brano: non disponibile!");
        tvLog = (TextView)findViewById(R.id.tvLog);

        TextView txtTitolo = (TextView) findViewById(R.id.txt_TitoloBrano);

        txtTitolo.setText(brano.getTitolo());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void caricaBranoUtente(Brano brano, Utente utente){
        this.brano= brano;
        this.utente=utente;
    }

    private void eseguiAlgo(){
        ActivityCompat.requestPermissions( this  ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1); //CHIEDO PERMESSI LETTURA FILE
        if(mf!=null) {
            alMidi = new AlgoritmoMidi(mf);
            List<String> out = alMidi.calc();
            Iterator i = out.iterator();
            Log.println(Log.ASSERT,"Out Algo", "Algo Concluso!");
            tvLog.setText("Algoritmo concluso! righe output: "+out.size());
            //while(i.hasNext()) Log.println(Log.ASSERT,"Out Algo", i.toString());
        }
        else{ tvLog.setText("file midi null!"); }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                i.putExtra("id_utente",getIntent().getLongExtra("id_utente",-1));   //find a better way to do dis
                NavUtils.navigateUpTo(this,i);
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {  //chiede permessi lettura SD
        switch (requestCode) {
            case 1: { // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted, yay! Do the contacts-related task you need to do.
                    File sdcard = Environment.getExternalStorageDirectory();         // apro MIDI file
                    File input = new File(sdcard, dir+"Chopin_EtudesOp10n1.mid"); //campanella.mid  Chopin_EtudesOp10n1.mid  happyBD.mid
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
