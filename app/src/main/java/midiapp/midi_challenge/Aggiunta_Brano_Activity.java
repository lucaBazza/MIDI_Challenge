package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.graphics.Path;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.util.ArraySet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileFilter;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class Aggiunta_Brano_Activity extends AppCompatActivity {

    FunzioniDatabase db = null;

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

    ArraySet<File> selectedFiles = new ArraySet<File>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta__brano);

        db = new FunzioniDatabase(getBaseContext());

        ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("Aggiugi Brano");

        Spinner sp = (Spinner) findViewById(R.id.spinner_aggiunta_brani);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.folders_aggiunta_brano,R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        sp.setAdapter(spinnerAdapter);
        File downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        file_list_adapter = new ArrayAdapter<String>(this,R.layout.checkbox_item_multiple_selection,R.id.textview_multiple_selection_item);
        ListView lv = (ListView)findViewById(R.id.lista_brani_trovati);

        lv.setAdapter(file_list_adapter);

        final File[] midiFiles = downloadFolderPath.listFiles(midiFilter);
        if(midiFiles != null){
            for(File f : midiFiles){
                file_list_adapter.add(f.getName());
            }
            file_list_adapter.notifyDataSetChanged();
        }

        List<Brano> braniPresenti = db.trovaUtente(getIntent().getLongExtra("id_utente",-1)).getBraniUtente();
        for(Brano b : braniPresenti){
            File f = new File(b.getNomeFile());

        }

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                File downloadFolderPath = null;
                switch (i){
                    case 0:
                        downloadFolderPath = new File(Environment.getExternalStorageDirectory(),"MidiChallenge");
                        break;
                    case 1:
                        downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                        break;
                    case 2: //TODO: implement file browsing activity
                        downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        break;
                    case 3:
                        break;
                }
                file_list_adapter.clear();
                File[] midiFiles = downloadFolderPath.listFiles(midiFilter);
                if(midiFiles != null){
                    for(File f : midiFiles){
                        file_list_adapter.add(f.getName());
                    }
                    ListView lv = (ListView) findViewById(R.id.lista_brani_trovati);
                    lv.setAdapter(file_list_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox_multiple_selection_item);
                cb.toggle();
                selectedFiles.add(midiFiles[i]);    //assunzione: l'ordine dei file non cambia durante l'esecuzione della activity
            }
        });

        Button btn = (Button)findViewById(R.id.btn_aggiungi_brano);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedFiles.isEmpty()) {
                    Utente u = db.trovaUtente(getIntent().getLongExtra("id_utente",-1));
                    List<Brano> braniUtente = db.braniUtente(u.getIdUtente());  //dammit

                    for (File f : selectedFiles) {
                        Brano b = new Brano(f.getName(), f.getPath(), 0);
                        if(! braniUtente.contains(b)) {
                            db.inserisci(b);
                            db.inserisciBranoPerUtente(u,b,0);
                        }
                    }
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    i.putExtra("id_utente", getIntent().getLongExtra("id_utente", 1));
                    startActivity(i);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("id_utente", getIntent().getLongExtra("id_utente", 1));
                NavUtils.navigateUpTo(this,i);
                break;
        }
        return true;
    }
}
