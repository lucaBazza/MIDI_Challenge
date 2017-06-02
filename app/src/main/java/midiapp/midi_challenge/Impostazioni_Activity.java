package midiapp.midi_challenge;

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
import android.database.sqlite.SQLiteDatabase;


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
        boolean result = this.deleteDatabase(SAMPLE_DB_NAME);
        if (result==true) {
            Toast.makeText(this, "Dati cancellati!", Toast.LENGTH_LONG).show();
        }
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

    private void createDB() {
        SQLiteDatabase sampleDB =  this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " +
                SAMPLE_TABLE_NAME +
                " (LastName VARCHAR, FirstName VARCHAR," +
                " Rank VARCHAR);");
        sampleDB.execSQL("INSERT INTO " +
                SAMPLE_TABLE_NAME +
                " Values ('Kirk','James, T','Captain');");
        sampleDB.close();
        sampleDB.getPath();
        Snackbar.make(getWindow().getDecorView().getRootView(),"Dati importati"+sampleDB.getPath(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
}