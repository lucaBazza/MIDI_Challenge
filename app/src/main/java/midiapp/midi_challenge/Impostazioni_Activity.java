package midiapp.midi_challenge;

import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;


public class Impostazioni_Activity extends GenericMIDIChallengeActivity implements OnClickListener {

    private static String SAMPLE_DB_NAME = "provaDbName"; //
    private static String SAMPLE_TABLE_NAME = "provaDbTableName"; // utenti  brano relabranoutente

    private Button btn_import;
    private Button btn_export;
    private Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni_);
        this.setTitle("Impostazioni");

        //SAMPLE_DB_NAME = FunzioniDatabase.getname();
        SAMPLE_DB_NAME = "databaseApp";
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
                createDB();
                break;
            case R.id.btn_export:
                exportDB("ExportDB.xml");
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
        Toast.makeText(this, "Dati importati @ "+sampleDB.getPath(), Toast.LENGTH_LONG).show();
    }

    private void exportDB(String nomeFileExport) {
        FileChannel source = null;
        FileChannel destination = null;
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {    //String currentDBPath = "//data//" + "<package name>" + "//databases//" + "<db name>";
                String currentDBPath = "/user/0/midiapp.midi_challenge/databases/"+SAMPLE_DB_NAME;
                String backupDBPath = cartellaPredefinita.getAbsolutePath();
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                source = new FileInputStream(currentDB).getChannel();
                try{
                    destination = new FileOutputStream(backupDB+nomeFileExport).getChannel();}
                catch(Exception ex){
                    Log.e("ErrFileOutStr","Errore!");}
                if(destination!=null) {
                    destination.transferFrom(source, 0, source.size());
                    destination.close();
                    Toast.makeText(getApplicationContext(), "Export Successful!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), "Export Failed!", Toast.LENGTH_SHORT).show();
                source.close();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Export Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}