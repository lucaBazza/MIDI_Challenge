package midiapp.midi_challenge;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class Impostazioni_Activity extends GenericMIDIChallengeActivity implements OnClickListener {

    private static String SAMPLE_DB_NAME =null; //
    private static String SAMPLE_TABLE_NAME = null; // utenti  brano relabranoutente

    private Button btn_import;
    private Button btn_export;
    private Button btn_delete;

    File backupDB=null;
    File currentDB =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni_);
        this.setTitle("Impostazioni");

        SAMPLE_DB_NAME = getDb().getNomeDatabase();
        SAMPLE_TABLE_NAME  = "utente";

        android.support.v7.app.ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);

        btn_import  = (Button) findViewById(R.id.btn_import);
        btn_import.setOnClickListener(this);

        btn_export = (Button) findViewById(R.id.btn_export);
        btn_export.setOnClickListener(this);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        NavigationView v = (NavigationView) findViewById(R.id.nav_view);
        if (v != null){
            v.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_import:
                importDB();
                break;
            case R.id.btn_export:
                exportDB("exportDB.db");
                break;
            case R.id.btn_delete:
                deleteDB();
                break;
        }
    }

    public void openNewActivity(View view) {
        Intent startActivity = new Intent(this, Info_Activity.class);
        startActivity(startActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(this,activity_MainRestyled.class);
                i.putExtra("id_utente",getIntent().getLongExtra("id_utente",-1));
                NavUtils.navigateUpTo(this,i);
                break;
        }
        super.onOptionsItemSelected(item);
        return true;
    }

    private void deleteDB(){
        showMessageOnCancel("Sicuro di voler cancellare tutti i dati? Non potrai tornare indietro.",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.dropAllTables();
                    if (db.dropAllTables()) {
                        SharedPreferences sp = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.apply();
                        Snackbar.make(getWindow().getDecorView().getRootView(),"DATABASE ELIMINATO!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Intent mainActivityIntent = new Intent(getBaseContext(),activity_MainRestyled.class);
                        startActivity(mainActivityIntent);
                    } else {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "DATABASE NON ELIMINATO!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            });
    }

    private void importDB() {
        try{
            if(currentDB!=null){
                new importDataBase(this,backupDB,currentDB);
                Snackbar.make(getWindow().getDecorView().getRootView(),"Dati importati", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
        catch (Exception e){
            Snackbar.make(getWindow().getDecorView().getRootView(),"Dati non importati", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void exportDB(String nomeFileExport) {
        FileChannel source = null;
        FileChannel destination = null;
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        long sizeSource=-1;
        String currentDBPath = "/user/0/midiapp.midi_challenge/databases/"+SAMPLE_DB_NAME+".db3";
        String backupDBPath = cartellaPredefinita.getAbsolutePath();
        currentDB = new File(data, currentDBPath);
        if(currentDB.exists()){ Toast.makeText(getApplicationContext(), "Non trovo il db locale: "+SAMPLE_DB_NAME, Toast.LENGTH_SHORT).show(); return; }
        backupDB = new File(sd, backupDBPath);

        try{
            source = new FileInputStream(currentDB).getChannel();
            sizeSource = source.size();
        }
        catch (Exception ex){ Log.e("ErrFileInputStr","Errore! \n"+ ex.toString()); }

        try{  destination = new FileOutputStream(backupDB+nomeFileExport).getChannel();}
        catch(Exception ex){ Log.e("ErrFileOutStr","Errore! \n"+ ex.toString());}

        if(destination!=null && source!=null && sizeSource!=-1) {
            try{
                destination.transferFrom(source, 0,sizeSource);
                destination.close();
                source.close();
                Toast.makeText(getApplicationContext(), "Export Successful!", Toast.LENGTH_SHORT).show();
            }
            catch(IOException ex){
                Log.e("ErrFileTransfer","Errore! \n"+ ex.toString());
            }
        }
        else Toast.makeText(getApplicationContext(), "Export fallito, non trovo destinazione", Toast.LENGTH_SHORT).show();
    }

    public static boolean deleteDirectory(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    private void showMessageOnCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}