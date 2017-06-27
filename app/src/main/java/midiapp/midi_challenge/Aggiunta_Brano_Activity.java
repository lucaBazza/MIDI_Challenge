package midiapp.midi_challenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.util.ArraySet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import static midiapp.midi_challenge.activity_MainRestyled.funzioniDatabase;

public class Aggiunta_Brano_Activity extends GenericMIDIChallengeActivity{

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
    TextView tv_addBraniDb;

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

        TabHost.TabSpec tabFileBroser = host.newTabSpec("Brani Recenti");
        tabFileBroser.setContent(R.id.tabBraniGiaAggiunti);
        tabFileBroser.setIndicator("Brani Recenti...");
        host.addTab(tabFileBroser);

        TabHost.TabSpec tabSelezione = host.newTabSpec("Selezione");
        tabSelezione.setContent(R.id.tabSelezioneCartella);
        tabSelezione.setIndicator("Da Cartella...");
        host.addTab(tabSelezione);

        host.setCurrentTab(0);

        final File[] midiFiles = downloadFolderPath.listFiles(midiFilter);
        if(midiFiles != null){
            for(File f : midiFiles){
                if(! braniUtente.contains(new Brano(f))) {
                    file_list_adapter.add(new Brano(f.getName(),0));
                }
            }
            file_list_adapter.notifyDataSetChanged();
            lv.setAdapter(file_list_adapter);
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


        sp.setSelection(2);                                                 //BAZZA
       
        final ListView lista_brani_trovatiDB = (ListView)findViewById(R.id.lista_brani_recenti);
        final List<Brano> braniDb = activity_MainRestyled.funzioniDatabase.prendiTuttiBrani();
        ArrayAdapter ArrayAdapterListaBrani = null;
        if (braniDb != null) {
            ArrayAdapterListaBrani = new ArrayAdapter<Brano>(this, R.layout.brani_list_element, braniDb);
            lista_brani_trovatiDB.setAdapter(ArrayAdapterListaBrani);
            if (braniDb.isEmpty()) {
                Toast.makeText(getBaseContext(),"Non ci sono titoli adatti!",Toast.LENGTH_LONG).show();
            }
        } else {
            ArrayAdapterListaBrani.notifyDataSetChanged();
        }
        lista_brani_trovatiDB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Brano selezione = braniDb.get(position);
                showMessageOKCancel("Vuoi inserire questo brano consigliato fra i tuoi? "+selezione.fileBrano.getName(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    funzioniDatabase.inserisciBranoPerUtente(utenteCorrente,selezione,0);
                                    Snackbar.make(getWindow().getDecorView().getRootView(),"brano inserito nel tuo profilo!", Snackbar.LENGTH_LONG).setAction("Action", null).show();}
                                catch(Exception sqlex){
                                    Snackbar.make(getWindow().getDecorView().getRootView(),"errore inserimento db", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    sqlex.printStackTrace();
                                }

                            }
                        });
                //Toast.makeText(getBaseContext(),"selezionato brano: "+position,Toast.LENGTH_LONG).show();
            }
        });

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

        ListView listaBraniRecenti = (ListView)findViewById(R.id.lista_brani_recenti);
        ArrayAdapter<Brano> adapterBraniRecenti = new ArrayAdapter<Brano>(this,R.layout.drawer_list_item,R.id.testo_list_item);
        List<Brano> tuttiBrani = db.prendiTuttiBrani();
        adapterBraniRecenti.addAll(tuttiBrani);
        listaBraniRecenti.setAdapter(adapterBraniRecenti);
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
