package midiapp.midi_challenge;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
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


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @author lucabazzanella
 */
public class Dettagli_Brano_Activity extends AppCompatActivity {
    public Brano brano = null;
    public Utente utente =null;
    private String dir = "MidiChallenge/";
    AlgoritmoMidi alMidi;
    MidiFile midiFile;
    TextView tvLog;
    TextView tvInfo1;

    Camera camera;
    Button brnFotocamera;

    private static FunzioniDatabase funzioniDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli__brano);

        long l = getIntent().getLongExtra("id_brano",0);
        funzioniDatabase = new FunzioniDatabase(getBaseContext());
        brano = funzioniDatabase.trovaBrano(l);

        Button btnBrano = (Button) findViewById(R.id.buttonProvaCaricaBrano);       //BUTTON ELABORA MIDI
        btnBrano.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                File sdcard = Environment.getExternalStorageDirectory();         // apro MIDI file  //campanella.mid  Chopin_EtudesOp10n1.mid  happyBD.mid
                File input = new File(brano.nomeFile);
                try {  midiFile = new MidiFile(input);
                    if(midiFile!=null) {
                        alMidi = new AlgoritmoMidi(midiFile);
                        showResults();
                    }   else{ tvLog.setText("file midi null!"); }
                }
                catch (IOException e) {
                    System.err.println("Error parsing MIDI file:");
                    e.printStackTrace();        //log.setText(e.toString());
                    midiFile=null; return;
                }
            }
        });

        brnFotocamera = (Button)findViewById(R.id.btnFotocamera);
        brnFotocamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                shootFoto();
            }
        });

        if(brano!=null) setTitle("Dettagli brano: "+brano.getTitolo());     //TITOLO
        else setTitle("Dettagli brano: non disponibile!");
        tvLog = (TextView)findViewById(R.id.tvLog);
        TextView txtTitolo = (TextView) findViewById(R.id.txt_TitoloBrano);
        txtTitolo.setText(brano.getTitolo());

        tvInfo1 = (TextView)findViewById(R.id.lbl_Info1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void caricaBranoUtente(Brano brano, Utente utente){
        this.brano= brano;
        this.utente=utente;
    }

    private void showResults(){
        List<String> out = alMidi.calc();
        Iterator i = out.iterator();
        tvInfo1.setText("Livello di difficoltà brano: "+alMidi.punteggio);
        tvLog.setText("Algoritmo concluso! righe output: "+out.size());
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
                    //File input = new File(sdcard, dir+"Chopin_EtudesOp10n1.mid"); //campanella.mid  Chopin_EtudesOp10n1.mid  happyBD.mid
                    File input = new File(brano.nomeFile);
                    try {  midiFile = new MidiFile(input);
                        if(midiFile!=null) {
                            alMidi = new AlgoritmoMidi(midiFile);
                            showResults();
                        }
                        else{ tvLog.setText("file midi null!"); }
                    }
                    catch (IOException e) {
                        System.err.println("Error parsing MIDI file:");
                        e.printStackTrace();        //log.setText(e.toString());
                        midiFile=null; return;
                    }
                }
                else {   // permission denied, boo! Disable the functionality that depends on this permission.
                    Log.println(Log.ASSERT,"Errore lettura","Permessi lettura SD negati!"); }
                break;
            }  // other 'case' lines to check for other permissions this app might request
            default: break;
        }
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    void shootFoto(){
         int cameraId = 0;
        // do we have a camera?
        if (checkCameraHardware(this)){
            try {
                camera = android.hardware.Camera.open(cameraId);
                camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
            }
            catch (Exception e){
                Log.d("Debug", e.toString());
            }
        }
        else { Log.println(Log.ASSERT,"Foto","No camera found!");  }
    }
    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("Debug", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}
