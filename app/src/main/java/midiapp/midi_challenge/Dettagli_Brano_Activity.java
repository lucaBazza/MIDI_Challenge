package midiapp.midi_challenge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.widget.Toast;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * @author lucabazzanella
 */
public class Dettagli_Brano_Activity extends GenericMIDIChallengeActivity {
    public Brano brano = null;
    //public Utente utente =null;
    private String dir = "MidiChallenge/";
    String[] arraySheets = null;
    AlgoritmoMidi alMidi;
    MidiFile midiFile;
    int tracciaSelezionata = 0;
    int autovalutazione = 0;
    TextView tvLog;
    TextView txt_dettagliAlgo;
    //TextView tvInfo2;
    ImageButton imgBtnDeleteSpartiti;
    Button btnBrano;
    Button btn_cambiaAutoreBrano;
    Button btn_autovalutazione;
    FloatingActionButton fab_share_dettagli_brano;
    FloatingActionButton fab_playmidi;
    Button btnShareMidi;

    Camera camera;
    Button brnFotocamera;
    private static final int REQUEST_GET_ACCOUNT = 112;
    private static final int PERMISSION_REQUEST_CODE = 200;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private static FunzioniDatabase funzioniDatabase = null;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli__brano);

        long l = getIntent().getLongExtra("id_brano",0);
        funzioniDatabase = new FunzioniDatabase(getBaseContext());
        brano = funzioniDatabase.trovaBrano(l);

        if(brano.idBrano==-1) {
            Log.d("Errore caricamento", "non trovo il brano in qeustione!: ");
            Toast.makeText(this, "Errore non trovo il brano in qeustione!!!", Toast.LENGTH_LONG).show();
        }

        txt_dettagliAlgo = (TextView)findViewById(R.id.txt_dettagliAlgo);
        //tvInfo2 = (TextView)findViewById(R.id.lbl_Info2);
        imgBtnDeleteSpartiti = (ImageButton)findViewById(R.id.id_imgBtnDeleteSpartiti);
        btnBrano = (Button) findViewById(R.id.buttonProvaCaricaBrano);       //BUTTON ELABORA MIDI
        btn_autovalutazione = (Button) findViewById(R.id.btn_autovalutazione);
        brnFotocamera = (Button)findViewById(R.id.btnFotocamera);
        btn_cambiaAutoreBrano = (Button) findViewById(R.id.btn_cambiaAutoreBrano);

        btnBrano.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            File input = new File(brano.getNomeFile());
            try { midiFile = new MidiFile(input);
                if(midiFile!=null) {
                    if (midiFile.getTrackCount() > 1){
                        Snackbar.make(view, "N. di tracce midi: " + midiFile.getTrackCount(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        tracciaSelezionata = dialogScegliTraccia(midiFile);
                    }
                    alMidi = new AlgoritmoMidi(midiFile,tracciaSelezionata); //default 0, ma se ci sono più tracce va cambiato!
                    List<String> out = alMidi.calcolaAlgoritmo();
                    brano.setDifficoltà((int)alMidi.getPunteggioFinale());
                    tvLog.setText("Algoritmo concluso! righe output: "+out.size());
                    txt_dettagliAlgo.setText("Livello di difficoltà brano: "+brano.getDifficoltà());
                    for(String x : out)
                        txt_dettagliAlgo.append(x+"\n");
                }
                else{ tvLog.setText("file midi null!"); }
            }
            catch (IOException e) {
                System.err.println("Error parsing MIDI file:");
                e.printStackTrace();        //log.setText(e.toString());
                midiFile=null; return;
            }
            }
        });


        btn_autovalutazione.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogAutovalutazione();
            }
        });

        brnFotocamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                shootFoto();
            }
        });
        fab_share_dettagli_brano = (FloatingActionButton) findViewById(R.id.fab_share_dettagli_brano) ; //share button
        fab_share_dettagli_brano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Punteggio ottenuto da: "+utenteCorrente.getNickName() +"\n";
                shareBody += "\t nome brano: "+ (brano.getTitolo().isEmpty() ? brano.getTitolo():"brano senza nome")+"\n";
                shareBody += "\t punteggio: "+ (brano.getDifficoltà()>0?brano.getDifficoltà():"non calcolata!");
                String shareSub = "Subject: punteggio";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Condividi punteggio!"));
            }
        });

        imgBtnDeleteSpartiti.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CancellaArraySpartiti();
            }
        });
        if(brano!=null) setTitle(brano.getTitolo());
        else setTitle("Dettagli brano: non disponibile!");
        tvLog = (TextView)findViewById(R.id.tvLog);
        TextView txtTitolo = (TextView) findViewById(R.id.txt_TitoloBrano);
        txtTitolo.setText(brano.getTitolo());

        ImageView imgViewSpartitoDemo = (ImageView) findViewById(R.id.imgViewSpartitoDemo);    // CARICA FOTO spartito
        if(!brano.arraySpartiti.isEmpty() ) { //&& !brano.arraySpartiti.isEmpty()
            Log.d("Carico spartiti_","totale spartiti trovati: " +brano.arraySpartiti.size());
            for(int i =0;i<brano.arraySpartiti.size();i++) { //carica tutti gli spartiti
                File imgFile = new File(brano.arraySpartiti.get(i));
                if (imgFile.exists()) {  // non trovando il file comunque entra nel if
                    Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 500, 500, true);
                    imgViewSpartitoDemo.setImageBitmap(myBitmap);
                } else {
                    Log.d("Errore spartito", "non trovo spartito: " + brano.arraySpartiti.get(0));
                    Toast.makeText(this, "Errore non trovo spartito!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else { Log.d("Errore spartito","non trovo spartiti in array! _++_ NULL"); }

        btnShareMidi = (Button) findViewById(R.id.btnShareMidi);
        btnShareMidi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                Uri uri = Uri.fromFile(brano.fileBrano);
                String shareBody = "File midi: "+brano.fileBrano.getName() +"\n";
                shareBody += "\t nome brano: "+ (brano.getTitolo().isEmpty() ? brano.getTitolo():" brano senza nome")+"\n";
                shareBody += "\t punteggio: "+ (brano.getDifficoltà() > 0 ? brano.getDifficoltà():" non calcolata!");
                String shareSub = "File midi"+brano.fileBrano.getName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Condividi file midi con punteggio!"));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab_playmidi = (FloatingActionButton)findViewById(R.id.fab_playmidi);
        fab_playmidi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Play midi!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(),activity_MainRestyled.class);
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted, yay! Do the contacts-related task you need to do.
                    File sdcard = Environment.getExternalStorageDirectory();         // apro MIDI file
                    //File input = new File(sdcard, dir+"Chopin_EtudesOp10n1.mid"); //campanella.mid  Chopin_EtudesOp10n1.mid  happyBD.mid

                    File input = new File(brano.getNomeFile());
                    try {
                        midiFile = new MidiFile(input);

                        /*
                        if (midiFile != null) {
                            alMidi = new AlgoritmoMidi(midiFile,0); //traccia default

                        } else {
                            tvLog.setText("file midi null!");
                        }*/
                    } catch (IOException e) {
                        System.err.println("Error parsing MIDI file:");
                        e.printStackTrace();        //log.setText(e.toString());
                        midiFile = null;
                        return;
                    }
                } else {   // permission denied, boo! Disable the functionality that depends on this permission.
                    Log.println(Log.ASSERT, "Errore lettura", "Permessi lettura SD negati!");
                }
                break;
            }  // other 'case' lines to check for other permissions this app might request
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data and camera", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
            }
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
         //int cameraId = 0;
        if (checkCameraHardware(this)){ // do we have a camera?
            ActivityCompat.requestPermissions(this, new String[]{GET_ACCOUNTS, CAMERA}, REQUEST_GET_ACCOUNT);
            try {
                    /*CameraManager manager = (CameraManager) getBaseContext().getSystemService(Context.CAMERA_SERVICE);   String[] cameraIds = manager.getCameraIdList();
                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[cameraId]);*/
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File fileImmagine = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"midi_challenge");
                    takePictureIntent.putExtra("percorso", FileProvider.getUriForFile(getBaseContext(),"midiapp.midi_challenge.fileprovider",fileImmagine));
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
            }
            catch (Exception e){
                Log.d("Debug", e.toString());
            }
        }
        else { Log.println(Log.ASSERT,"Foto","No camera found!");  }
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){ // this device has a camera
            return true;
        }
        else {  // no camera on this device
            return false;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //restituisce l'immagine dopo che è stato chiamato l'intent alla fotocamera
        boolean fotoCaricata = false;
        File fileSalvataggio=null;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView)findViewById(R.id.imgViewSpartitoDemo)).setImageBitmap(imageBitmap); //visualizza l'immagine scattata su un imageview demo
            try {
                String path = GenericMIDIChallengeActivity.cartellaPredefinita.toString();
                //String nomeFile = "sheets_"+brano.getTitolo()+"_" +new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())+ ".jpg";
                String nomeFile = "sheets_"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())+ ".jpg";
                fileSalvataggio = new File(path,nomeFile); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

                Log.d("SalvataggioSheets:_",path);
                Log.d("SalvataggioSheets:_",nomeFile);
                Log.d("SalvataggioSheets:_", fileSalvataggio.getAbsolutePath());

                //OutputStream fOut = null;
                OutputStream fOut = new FileOutputStream(fileSalvataggio);
                Bitmap pictureBitmap = (Bitmap) extras.get("data"); // obtaining the Bitmap//Bitmap pictureBitmap = getImageBitmap(myurl); // obtaining the Bitmap
                pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with x% compression rate
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream  //MediaStore.Images.Media.insertImage(getContentResolver(), fileSalvataggio.getAbsolutePath(), fileSalvataggio.getName(), fileSalvataggio.getName());
                fotoCaricata=true;  //passa alla fase di aggiornamento del DB
            }
            catch (Exception ex){
                Toast.makeText(this, "Errore salvataggio immagine!", Toast.LENGTH_LONG).show();
                Log.d("Errore i/o",ex.toString());
            }

            if(fotoCaricata){ //Aggiorna brano, inserendo la nuova foto nella rispettiva colonna
                if(brano.arraySpartiti==null)
                    brano.arraySpartiti = new ArrayList<String>();
                brano.arraySpartiti.add(fileSalvataggio.getAbsolutePath()); //brano.arraySpartiti+=";"+path+nomeFile;

                brano.autore = "Giovanni muchacha!"; //modificare!
                Log.d("DatabaseLog_",brano.arraySpartiti.toString());
                if(funzioniDatabase.aggiornaBrano(brano)==1) Log.d("DatabaseLog: ","Riuscito  aggiorna brano!");
                else Log.d("DatabaseLog: ","Failure !");
            }
        }
    }

    public void editTitoloBrano(View v){                    //manca  brano.setTitolo( ..  )
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aggiorna Titolo");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().length()>3){
                    brano.setTitolo(input.getText().toString());
                    ((TextView)findViewById(R.id.txt_TitoloBrano)).setText(input.getText().toString());
                    getSupportActionBar().setTitle(input.getText().toString());
                    getDb().aggiornaBrano(brano);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    public void editAutoreBrano(View v){                    //manca  brano.setAutore( ..  )
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aggiorna Autore brano!");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().length()>3){
                    brano.setAutore(input.getText().toString());
                    ((TextView)findViewById(R.id.txt_TitoloAutore)).setText(input.getText().toString());
                    getDb().aggiornaBrano(brano);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void CancellaArraySpartiti(){
        brano.arraySpartiti.clear();
        if(funzioniDatabase.aggiornaBrano(brano)==1) Log.d("DatabaseLog: ","Riuscito  aggiorna brano!");
        else Log.d("DatabaseLog: ","Failure !");
    }

    public int dialogScegliTraccia(MidiFile x) {
        //final int trac =0;
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Dettagli_Brano_Activity.this);
        builderSingle.setIcon(R.drawable.ic_menu_send);
        builderSingle.setTitle("Seleziona la traccia: ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Dettagli_Brano_Activity.this, android.R.layout.select_dialog_item);
        for(int i =0;i < x.getTrackCount();i++){
            arrayAdapter.add(i+".: \t"+ x.getTracks().get(i).toString());
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String strName = arrayAdapter.getItem(which);
                tracciaSelezionata = which;
            }
        });
        builderSingle.show();
        return 0;
    }

    public void dialogAutovalutazione() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Dettagli_Brano_Activity.this);
        builderSingle.setIcon(R.drawable.ic_menu_send);
        builderSingle.setTitle("Valuta la tua performance ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Dettagli_Brano_Activity.this, android.R.layout.select_dialog_item);
        arrayAdapter.add("1: \t grandi difficolta");
        arrayAdapter.add("2: \t piccole difficolta");
        arrayAdapter.add("3: \t corretta didatticamente");
        arrayAdapter.add("4: \t controllo del brano");
        arrayAdapter.add("5: \t master");

        builderSingle.setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String strName = arrayAdapter.getItem(which);
                autovalutazione = which+1;
                brano.setAutovalutazione(which+1);
                utenteCorrente.setPunteggioMassimo(utenteCorrente.getPunteggioMassimo()+brano.difficoltà); //da modificare!!!

                if(db.aggiornaUtente(utenteCorrente)==-1)
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Aggiornamento profilo utente nel DB non riuscito!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });
        builderSingle.show();
    }
}