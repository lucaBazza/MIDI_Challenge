package midiapp.midi_challenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.util.ArraySet;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class Aggiunta_Brano_Activity extends AppCompatActivity{

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

    ArrayAdapter<Brano> file_list_adapter = null;

    ArraySet<Brano> selectedFiles = new ArraySet<Brano>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta__brano);
        db = new FunzioniDatabase(getBaseContext());
        List<Brano> braniUtente = db.trovaUtente(getIntent().getLongExtra("id_utente",-1)).getBraniUtente();
        
        ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("Aggiugi Brano");

        Spinner sp = (Spinner) findViewById(R.id.spinner_aggiunta_brani);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.cartelle_aggiunta_brano,R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        sp.setAdapter(spinnerAdapter);
        File downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        file_list_adapter = new ArrayAdapter<Brano>(this,R.layout.checkbox_item_multiple_selection,R.id.textview_multiple_selection_item);
        final ListView lv = (ListView)findViewById(R.id.lista_brani_trovati);

        lv.setAdapter(file_list_adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SparseBooleanArray selected = lv.getCheckedItemPositions();
                if(selected.get(i)){
                    View layout = (LinearLayout)lv.getChildAt(i);
                    layout.setBackgroundColor(R.color.colorPrimaryDark);
                    TextView t = (TextView) findViewById(R.id.textview_multiple_selection_item);
                    t.setTextColor(Color.WHITE);
                }else{
                    View layout = (LinearLayout)lv.getChildAt(i);
                    layout.setBackgroundColor(Color.TRANSPARENT);
                    TextView t = (TextView) findViewById(R.id.textview_multiple_selection_item);
                    t.setTextColor(Color.BLACK);
                }
            }
        });

        TabHost host = (TabHost) findViewById(R.id.aggiunta_brano_tab_host);
        host.setup();


        TabHost.TabSpec tabSelezione = host.newTabSpec("Selezione");
        tabSelezione.setContent(R.id.tabSelezioneCartella);
        tabSelezione.setIndicator("Da Cartella...");
        host.addTab(tabSelezione);


        TabHost.TabSpec tabFileBroser = host.newTabSpec("File");
        tabFileBroser.setContent(R.id.tabFileBrowser);
        tabFileBroser.setIndicator("Da File...");
        host.addTab(tabFileBroser);

        host.setCurrentTab(0);

        final File[] midiFiles = downloadFolderPath.listFiles(midiFilter);
        if(midiFiles != null){
            for(File f : midiFiles){
                if(! braniUtente.contains(f)) {
                    file_list_adapter.add(new Brano(f.getName(),0));
                }
            }
            file_list_adapter.notifyDataSetChanged();
        }

        for(File f : midiFiles){
            Brano b = new Brano(f.getName(),f,-1); //titolo , percorso , difficolta
            if(db.trovaBrano(b.getTitolo()) == null) {
                db.inserisci(b);
            }
            else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Il file "+ b.getTitolo()+" è gia presente nel DataBase!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
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
                    case 2:
                        downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        break;
                    case 3: //TODO: implement file browsing activity
                        break;
                }
                file_list_adapter.clear();
                File[] midiFiles = downloadFolderPath.listFiles(midiFilter);
                if(midiFiles != null){
                    for(File f : midiFiles){
                        file_list_adapter.add(new Brano(f.getName(),f,-1));
                    }
                    ListView lv = (ListView) findViewById(R.id.lista_brani_trovati);
                    lv.setAdapter(file_list_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btn = (Button)findViewById(R.id.btn_aggiungi_brano);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray selected = lv.getCheckedItemPositions();
                for(int i = 0; i < selected.size(); i++){
                    if(selected.valueAt(i)){
                        selectedFiles.add(file_list_adapter.getItem(i));
                    }
                }
                for(Brano b : selectedFiles){
                    Utente utenteCorrente = db.trovaUtente(getIntent().getLongExtra("id_utente",-1));
                    db.inserisciBranoPerUtente(utenteCorrente,b,0);
                }
                Intent i = new Intent(getBaseContext(),activity_MainRestyled.class);
                i.putExtra("id_utente", getIntent().getLongExtra("id_utente", 1));
                startActivity(i);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), activity_MainRestyled.class);
                i.putExtra("id_utente", getIntent().getLongExtra("id_utente", 1));
                NavUtils.navigateUpTo(this,i);
                break;
        }
        return true;
    }
}
