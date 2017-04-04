package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileFilter;
import java.security.Permission;
import java.util.List;
import java.util.Random;

import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Static Declarations
    static TextView log, tv;

    private String[] mActivityTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<Brano> ArrayAdapterListaBrani;

    private Utente utente; //utente corrente
    //private Brano brano; //BRANO DI PROVA  -- DEBUG
    static FunzioniDatabase funzioniDatabase = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        funzioniDatabase = new FunzioniDatabase(this.getBaseContext());
        setContentView(R.layout.activity_main);

        //cartella predefinita in cui sono contenuti i file .midi
        File cartellaPredefinita = new File(Environment.getExternalStorageDirectory(),"MidiChallenge");

        if(! cartellaPredefinita.exists()){
            cartellaPredefinita.mkdir();
        }

        if(getIntent().hasExtra("id_utente")){
            utente = funzioniDatabase.trovaUtente(getIntent().getLongExtra("id_utente",-1));
        }

        final ListView ListViewXmlListaBrani = (ListView) findViewById(R.id.lista_brani_utente);
        final List<Brano> braniUtente = funzioniDatabase.braniUtente(utente.getIdUtente());

        if(braniUtente != null) {
            ArrayAdapterListaBrani = new ArrayAdapter<Brano>(this, R.layout.brani_list_element, braniUtente);
            ListViewXmlListaBrani.setAdapter(ArrayAdapterListaBrani);
            if(braniUtente.isEmpty()){
                Toast.makeText(getBaseContext(),"Nessun Brano Trovato!",Toast.LENGTH_LONG);
            }
        }
        else
            ArrayAdapterListaBrani.notifyDataSetChanged();

        ListViewXmlListaBrani.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brano selezione = braniUtente.get(position);
                Intent aperturaDettagliBrano = new Intent(getApplicationContext(),Dettagli_Brano_Activity.class);
                aperturaDettagliBrano.putExtra("id_brano",selezione.getIdBrano());
                aperturaDettagliBrano.putExtra("id_utente",utente.getIdUtente());
                startActivity(aperturaDettagliBrano);
            }
        });

        tv = (TextView)findViewById(R.id.textView);
        tv.setMovementMethod(new ScrollingMovementMethod());

        mActivityTitles = new String[]{"Home","Profilo","Registratore","Metronomo","Accordatore", "Impostazioni","Cambia Utente"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        ArrayAdapter<String> xxxx  = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mActivityTitles);        mDrawerList.setAdapter(xxxx);
        // Set the list's click listener    //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent testIntent;
                switch (position){ // "Home","Profilo","Registratore","Metronomo","Accordatore, Impostazioni","Logout"
                    case 0: testIntent = new Intent(getApplicationContext(),MainActivity.class); break;
                    case 1: testIntent = new Intent(getApplicationContext(),ActivityPaginaUtente.class); break;
                    case 2: testIntent = new Intent(getApplicationContext(),Registratore_Activity.class); break;
                    case 3: testIntent = new Intent(getApplicationContext(),MetronomoActivity.class); break;
                    case 4: testIntent = new Intent(getApplicationContext(),AccordatoreActivity.class); break;
                    case 5: testIntent = new Intent(getApplicationContext(),Impostazioni_Activity.class); break; //IMPOSTAZIONI ACTIVITY
                    case 6: testIntent = new Intent(getApplicationContext(),Login_Activity.class); break; //Domanda prima di uscire!
                    default: testIntent = null;
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                if(testIntent!= null) {  //Log.println(Log.ASSERT,"activity","Pos: "+ position+"  activity: "+testIntent.toString());
                    testIntent.putExtra("id_utente",utente.getIdUtente());
                    startActivity(testIntent);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getBaseContext(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Intent i = new Intent(getBaseContext(), Aggiunta_Brano_Activity.class);
                        i.putExtra("id_utente", utente.getIdUtente());
                        startActivity(i);
                }
            }
        });
        setTitle(utente.getNickName());
    }

    /**
     *  crea il pulsante menù nella action bar del programma
     * @param menu il menù xml in res/menu/ da usare
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 2 :
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else Toast.makeText(MainActivity.this, "Permessi non concessi!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * permette di visualizzare e/o nascondere il drawer/menùLaterale
     * @param item menù sul quale viene triggerato l'evento
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.show_navigation_drawer :
                    DrawerLayout drw = (DrawerLayout)findViewById(R.id.drawer_layout);
                    if(!drw.isDrawerOpen(Gravity.LEFT))
                        drw.openDrawer(Gravity.LEFT);
                    else
                        drw.closeDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(ArrayAdapterListaBrani!=null)
            ArrayAdapterListaBrani.notifyDataSetChanged();
        else {}
    }
}