package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;

public class Aggiunta_Brano_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta__brano);

        ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("Aggiugi Brano");

        ActivityCompat.requestPermissions(Aggiunta_Brano_Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        File downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        FileFilter midiFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().contains(".mid")) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] midiFiles = downloadFolderPath.listFiles(midiFilter);

        ListView lv = (ListView)findViewById(R.id.lista_brani_trovati);

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this,R.layout.drawer_list_item);
        lv.setAdapter(ad);

        if(midiFiles != null){
            for(File f : midiFiles){
                ad.add(f.getName());
            }
            ad.notifyDataSetChanged();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                i.putExtra("id_utente",getIntent().getLongExtra("id_utente",-1));   //find a better way to do dis
                startActivity(i);
                break;
        }
        return true;
    }
}
