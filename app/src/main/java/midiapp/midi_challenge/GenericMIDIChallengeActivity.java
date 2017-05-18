package midiapp.midi_challenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class GenericMIDIChallengeActivity extends AppCompatActivity {

    FunzioniDatabase db = null;

    static File cartellaPredefinita = new File(Environment.getExternalStorageDirectory(), "MidiChallenge");

    protected Utente utenteCorrente = null;

    public FunzioniDatabase getDb() {
        return db;
    }

    public void setDb(FunzioniDatabase db) {
        this.db = db;
    }

    public File getCartellaPredefinita() {
        return cartellaPredefinita;
    }

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mActivityTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new FunzioniDatabase(this.getBaseContext());

        SharedPreferences sp = getPreferences(MODE_PRIVATE);

        if (getIntent().hasExtra("id_utente")) {
            utenteCorrente = db.trovaUtente(getIntent().getLongExtra("id_utente", -1));
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("id_utente", utenteCorrente.getIdUtente());
            editor.commit();
        }

        if (sp.contains("id_utente")) {
            utenteCorrente = db.trovaUtente(sp.getLong("id_utente", -1));
        }

        if (utenteCorrente != null) {
            Log.d("Utente","Utente caricato da generic midi!");

        }

        //setSupportActionBar(toolbar);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, mDrawerLayout, , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        /*ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = getActionBar().get;
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        */
        //inizializzaDrower();

    }

    //@Override
    public boolean onNavigationItemSelected(MenuItem item) {    // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent prossimaActivity=null;
        switch (id){
            case R.id.nav_item_home:    prossimaActivity = new Intent(getApplicationContext(), activity_MainRestyled.class);    break;
            case R.id.nav_impostazioni: prossimaActivity =  new Intent(getApplicationContext(), Impostazioni_Activity.class);   break;
            case R.id.nav_metronomo:    prossimaActivity = new Intent(getApplicationContext(), MetronomoActivity.class);        break;
            case R.id.nav_cambiaUtente:  ChooseUserDialog dg = new ChooseUserDialog();
                                        dg.show(getFragmentManager(), "Cambia Utente");                                         break;
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);

        if (prossimaActivity != null) {  //Log.println(Log.ASSERT,"activity","Pos: "+ position+"  activity: "+testIntent.toString());
            if (prossimaActivity.filterEquals(new Intent(getApplicationContext(), Login_Activity.class))) {//TODO: ugly, change it. ASAP.
                ChooseUserDialog dg = new ChooseUserDialog();
                dg.show(getFragmentManager(), "Cambia Utente");
            } else {
                prossimaActivity.putExtra("id_utente", utenteCorrente.getIdUtente());
                startActivity(prossimaActivity);
            }
        }
        return true;
    }

    private void inizializzaDrower(){
        mActivityTitles = new String[]{"Home", "Profilo", "Registratore", "Metronomo", "Accordatore", "Impostazioni", "Cambia Utente"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.show_navigation_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        if (mDrawerList != null) {
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
                        if (prossimaActivity.filterEquals(new Intent(getApplicationContext(), Login_Activity.class))) {//TODO: ugly, change it. ASAP.
                            ChooseUserDialog dg = new ChooseUserDialog();
                            dg.show(getFragmentManager(), "Cambia Utente");
                        } else {
                            prossimaActivity.putExtra("id_utente", utenteCorrente.getIdUtente());
                            startActivity(prossimaActivity);
                        }
                    }
                }
            });
        }

    }

    private void oldDrower() {
        mActivityTitles = new String[]{"Home", "Profilo", "Registratore", "Metronomo", "Accordatore", "Impostazioni", "Cambia Utente"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        if (mDrawerList != null) {
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
                        if (prossimaActivity.filterEquals(new Intent(getApplicationContext(), Login_Activity.class))) {//TODO: ugly, change it. ASAP.
                            ChooseUserDialog dg = new ChooseUserDialog();
                            dg.show(getFragmentManager(), "Cambia Utente");
                        } else {
                            prossimaActivity.putExtra("id_utente", utenteCorrente.getIdUtente());
                            startActivity(prossimaActivity);
                        }
                    }
                }
            });
        }
    }
}

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.show_navigation_drawer :
                DrawerLayout drw = (DrawerLayout)findViewById(R.id.drawer_layout);
                if(drw != null) {   //alcune activiy potrebbero avere il drw layout, altre no.
                    if (!drw.isDrawerOpen(Gravity.LEFT))
                        drw.openDrawer(Gravity.LEFT);
                    else
                        drw.closeDrawer(Gravity.LEFT);
                    break;
                }
                break;
            case android.R.id.home :
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("id_utente",getIntent().getLongExtra("id_utente",-1));
                startActivity(i);
                break;
        }
        return true;
    }*/