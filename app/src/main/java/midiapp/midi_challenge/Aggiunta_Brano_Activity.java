package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileFilter;
import java.security.Permission;

public class Aggiunta_Brano_Activity extends AppCompatActivity {

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

    ArrayAdapter<String> file_list_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta__brano);

        ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("Aggiugi Brano");

        Spinner sp = (Spinner) findViewById(R.id.spinner_aggiunta_brani);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.folders_aggiunta_brano,R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        sp.setAdapter(spinnerAdapter);
        File downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        ListView lv = (ListView)findViewById(R.id.lista_brani_trovati);
        lv.setAdapter(file_list_adapter);


        file_list_adapter = new ArrayAdapter<String>(this,R.layout.drawer_list_item);
        File[] midiFiles = downloadFolderPath.listFiles(midiFilter);
        if(midiFiles != null){
            for(File f : midiFiles){
                file_list_adapter.add(f.getName());
            }
            file_list_adapter.notifyDataSetChanged();
        }

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                File downloadFolderPath = null;
                switch (i){
                    case 0:
                        downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        break;
                    case 1:
                        downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                        break;
                    case 2: //TODO: implement file browsing activity
                        break;
                }
                File[] midiFiles = downloadFolderPath.listFiles(midiFilter);
                if(midiFiles != null){
                    for(File f : midiFiles){
                        file_list_adapter.add(f.getName());
                    }
                    file_list_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
