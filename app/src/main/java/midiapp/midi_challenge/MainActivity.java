package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.util.List;

import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends GenericMIDIChallengeActivity {
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
        funzioniDatabase = getDb();
        setContentView(R.layout.activity_main);

        //cartella predefinita in cui sono contenuti i file .midi
        File cartellaPredefinita = getCartellaPredefinita();

        if (!cartellaPredefinita.exists()) {
            cartellaPredefinita.mkdir();
        }

        if (getIntent().hasExtra("id_utente")) {
            utente = funzioniDatabase.trovaUtente(getIntent().getLongExtra("id_utente", -1));
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("id_utente",utente.getIdUtente());
            editor.commit();
        }

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        if(sp.contains("id_utente")) {
            utente = funzioniDatabase.trovaUtente(sp.getLong("id_utente", -1));
        }

        if (utente != null) {

            final ListView ListViewXmlListaBrani = (ListView) findViewById(R.id.lista_brani_utente);
            final List<Brano> braniUtente = funzioniDatabase.braniUtente(utente.getIdUtente());

            if (braniUtente != null) {
                ArrayAdapterListaBrani = new ArrayAdapter<Brano>(this, R.layout.brani_list_element, braniUtente);
                ListViewXmlListaBrani.setAdapter(ArrayAdapterListaBrani);
                if (braniUtente.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Nessun Brano Trovato!", Toast.LENGTH_LONG);
                }
            } else
                ArrayAdapterListaBrani.notifyDataSetChanged();

            ListViewXmlListaBrani.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Brano selezione = braniUtente.get(position);
                    Intent aperturaDettagliBrano = new Intent(getApplicationContext(), Dettagli_Brano_Activity.class);
                    aperturaDettagliBrano.putExtra("id_brano", selezione.getIdBrano());
                    aperturaDettagliBrano.putExtra("id_utente", utente.getIdUtente());
                    startActivity(aperturaDettagliBrano);
                }
            });

            tv = (TextView) findViewById(R.id.textView);
            tv.setMovementMethod(new ScrollingMovementMethod());

            mActivityTitles = new String[]{"Home", "Profilo", "Registratore", "Metronomo", "Accordatore", "Impostazioni", "Cambia Utente"};
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);

            // Set the adapter for the list view
            ArrayAdapter<String> xxxx = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mActivityTitles);
            mDrawerList.setAdapter(xxxx);
            // Set the list's click listener    //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent prossimaActivity;
                    switch (position) { // "Home","Profilo","Registratore","Metronomo","Accordatore, Impostazioni","Logout"
                        case 0:
                            prossimaActivity = new Intent(getApplicationContext(), MainActivity.class);
                            break;
                        case 1:
                            prossimaActivity = new Intent(getApplicationContext(), ActivityPaginaUtente.class);
                            break;
                        case 2:
                            prossimaActivity = new Intent(getApplicationContext(), Registratore_Activity.class);
                            break;
                        case 3:
                            prossimaActivity = new Intent(getApplicationContext(), MetronomoActivity.class);
                            break;
                        case 4:
                            prossimaActivity = new Intent(getApplicationContext(), AccordatoreActivity.class);
                            break;
                        case 5:
                            prossimaActivity = new Intent(getApplicationContext(), Impostazioni_Activity.class);
                            break; //IMPOSTAZIONI ACTIVITY
                        case 6:
                            prossimaActivity = new Intent(getApplicationContext(), Login_Activity.class);
                            break; //Domanda prima di uscire!
                        default:
                            prossimaActivity = null;
                    }
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    if (prossimaActivity != null) {  //Log.println(Log.ASSERT,"activity","Pos: "+ position+"  activity: "+testIntent.toString());
                        if (prossimaActivity.filterEquals(new Intent(getApplicationContext(),Login_Activity.class))) {//TODO: ugly, change it. ASAP.
                            ChooseUserDialog dg = new ChooseUserDialog();
                            dg.show(getFragmentManager(),"Cambia Utente");
                        }else{
                            prossimaActivity.putExtra("id_utente", utente.getIdUtente());
                            startActivity(prossimaActivity);
                        }
                    }
                }
            });

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Intent i = new Intent(getBaseContext(), Aggiunta_Brano_Activity.class);
                        i.putExtra("id_utente", utente.getIdUtente());
                        startActivity(i);
                    }
                }
            });
            setTitle(utente.getNickName());
        } else {
            ChooseUserDialog dg = new ChooseUserDialog();
            dg.show(getFragmentManager(), "Login");
        }
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

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(ArrayAdapterListaBrani!=null)
            ArrayAdapterListaBrani.notifyDataSetChanged();
        else {}
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(homeIntent);
        finish();
    }
}