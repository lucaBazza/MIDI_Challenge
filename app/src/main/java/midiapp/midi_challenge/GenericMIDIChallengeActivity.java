package midiapp.midi_challenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompatSideChannelService;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

    private TextView tv_drawer_nome = null;
    private TextView tv_headerPunteggio = null;
    private ImageView iv_fotoUtente = null;

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
            case R.id.nav_item_home:        prossimaActivity = new Intent(getApplicationContext(), activity_MainRestyled.class);    break;
            case R.id.nav_profilo:          prossimaActivity = new Intent(getApplicationContext(),ActivityPaginaUtente.class);      break;
            case R.id.nav_registratore:     prossimaActivity = new Intent(getApplicationContext(), Registratore_Activity.class);    break;
            case R.id.nav_metronomo:        prossimaActivity = new Intent(getApplicationContext(), MetronomoActivity.class);        break;
            case R.id.nav_accordatore:      prossimaActivity = new Intent(getApplicationContext(),AccordatoreActivity.class);       break;
            case R.id.nav_impostazioni:     prossimaActivity =  new Intent(getApplicationContext(), Impostazioni_Activity.class);   break;
            case R.id.nav_cambiaUtente:     ChooseUserDialog dg = new ChooseUserDialog();
                                                    dg.show(getFragmentManager(), "Cambia Utente");                                 break;
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

        DrawerLayout dw = (DrawerLayout)findViewById(R.id.drawer_layout);   //tiene il drawer chiuso quando fa la create
        dw.closeDrawer(Gravity.LEFT | Gravity.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__main_restyled, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        if(drawer != null)
            if(drawer.isDrawerOpen(Gravity.LEFT))
                drawer.openDrawer(Gravity.LEFT | Gravity.START);
            else
                drawer.closeDrawer(Gravity.LEFT);
        return true;

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        aggiornaFoto_punteggioNavHeader();
        // Sync the toggle state after onRestoreInstanceState has occurred.

    }

    private void aggiornaFoto_punteggioNavHeader(){
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        View view = nv.getHeaderView(0);
        tv_headerPunteggio = (TextView) view.findViewById(R.id.tv_header_score);
        if(tv_headerPunteggio!=null)
            if(utenteCorrente.getPunteggioMassimo()>0)
                tv_headerPunteggio.setText((utenteCorrente.getPunteggioMassimo()));
            else
                tv_headerPunteggio.setText("Hai 0 punti!");
        iv_fotoUtente = (ImageView)view.findViewById(R.id.iv_header_foto_utente);
        if(iv_fotoUtente!=null){
            File imgFile = new File(utenteCorrente.getFoto());
            if(!utenteCorrente.getFoto().isEmpty() && imgFile.exists()){
                Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()) ,100,100,true);
                iv_fotoUtente.setImageBitmap(myBitmap);
            }
            else {
                iv_fotoUtente.setImageResource(R.mipmap.generic_user_mc);
            }
        }

    }

    private void inizializzaDrower(){
        mActivityTitles = new String[]{"Home", "Profilo", "Registratore", "Metronomo", "Accordatore", "Impostazioni", "Cambia Utente"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.show_navigation_drawer);
        mDrawerList = (ListView) findViewById(R.id.nav_view);

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
                            prossimaActivity = new Intent(getApplicationContext(), activity_MainRestyled.class);
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

    /*private void oldDrower() {
        mActivityTitles = new String[]{"Home", "Profilo", "Registratore", "Metronomo", "Accordatore", "Impostazioni", "Cambia Utente"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_view);

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
                            prossimaActivity = new Intent(getApplicationContext(), activity_MainRestyled.class);
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
    }*/