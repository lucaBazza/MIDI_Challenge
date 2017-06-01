package midiapp.midi_challenge;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;


public class Impostazioni_Activity extends GenericMIDIChallengeActivity implements OnClickListener {

    private static  String SAMPLE_DB_NAME = "provaDbName"; //
    private static  String SAMPLE_TABLE_NAME = "provaDbTableName"; // utenti  brano relabranoutente

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
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_import:
                createDB();
                break;
            case R.id.btn_export:
                exportDB();
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
        Toast.makeText(this, "Dati Importati @ "+sampleDB.getPath(), Toast.LENGTH_LONG).show();
    }

    private void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        //String currentDBPath = "/data/"+ "com.authorwjf.sqliteexport" +"/databases/"+SAMPLE_DB_NAME; //    "/data/user/0/midiapp.midi_challenge/databases/"+SAMPLE_DB_NAME;
        String currentDBPath = "/user/0/midiapp.midi_challenge/databases/"+SAMPLE_DB_NAME;
        String backupDBPath = cartellaPredefinita.getAbsolutePath();
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel(); //errore
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Dati esportati in: "+cartellaPredefinita.getAbsolutePath()+"!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            Toast.makeText(this, "Errore esportazione", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
