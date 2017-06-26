package midiapp.midi_challenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GenericMIDIChallengeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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
            Log.d("Utente", "Utente caricato da generic midi!");

        }

        NavigationView v = (NavigationView) findViewById(R.id.nav_view);
        if (v != null){
            v.setNavigationItemSelectedListener(this);
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {    // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent prossimaActivity = null;
        switch (id) {
            case R.id.nav_item_home:
                prossimaActivity = new Intent(getApplicationContext(), activity_MainRestyled.class);
                break;
            case R.id.nav_profilo:
                prossimaActivity = new Intent(getApplicationContext(), ActivityPaginaUtente.class);
                prossimaActivity.putExtra("id_utente",utenteCorrente.getIdUtente());
                break;
            case R.id.nav_registratore:
                prossimaActivity = new Intent(getApplicationContext(), Registratore_Activity.class);
                break;
            case R.id.nav_metronomo:
                prossimaActivity = new Intent(getApplicationContext(), MetronomoActivity.class);
                break;
            case R.id.nav_accordatore:
                prossimaActivity = new Intent(getApplicationContext(), AccordatoreActivity.class);
                break;
            case R.id.nav_consigli:
                prossimaActivity = new Intent(getApplicationContext(),activity_consigli.class);
                break;
            case R.id.nav_impostazioni:
                prossimaActivity = new Intent(getApplicationContext(), Impostazioni_Activity.class);
                break;
            case R.id.nav_cambiaUtente:
                ChooseUserDialog dg = new ChooseUserDialog();
                dg.show(getFragmentManager(), "Cambia Utente");
                break;
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);

        if(prossimaActivity != null) {  
            prossimaActivity.putExtra("id_utente", utenteCorrente.getIdUtente());
            startActivity(prossimaActivity);
        }

        DrawerLayout dw = (DrawerLayout) findViewById(R.id.drawer_layout);   //tiene il drawer chiuso quando fa la create
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

        switch (item.getItemId()) {
            case R.id.show_navigation_drawer:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                if (drawer != null) {
                    if (!drawer.isDrawerOpen(Gravity.LEFT))
                        drawer.openDrawer(Gravity.LEFT | Gravity.START);
                    else
                        drawer.closeDrawer(Gravity.LEFT);
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (utenteCorrente != null) {
            aggiornaFoto_punteggioNavHeader();
        }
        // Sync the toggle state after onRestoreInstanceState has occurred.

    }

    private void aggiornaFoto_punteggioNavHeader() {
        View view = null;
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        if (nv == null) return;
        else
            view = nv.getHeaderView(0);
        tv_headerPunteggio = (TextView) view.findViewById(R.id.tv_header_score);
        if(tv_headerPunteggio!=null)
            if(utenteCorrente.getPunteggioMassimo()>0)
                tv_headerPunteggio.setText(Integer.toString(utenteCorrente.getPunteggioMassimo()));

            else
                tv_headerPunteggio.setText("Hai 0 punti!");
        iv_fotoUtente = (ImageView) view.findViewById(R.id.iv_header_foto_utente);
        if (iv_fotoUtente != null) {
            File imgFile = new File(utenteCorrente.getFoto());
            if (!utenteCorrente.getFoto().isEmpty() && imgFile.exists()) {
                Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 100, 100, true);
                iv_fotoUtente.setImageBitmap(myBitmap);
            } else {
                iv_fotoUtente.setImageResource(R.mipmap.generic_user_mc);
            }
        }

    }

    public void creaDbBraniFromResources(){
        File file = new File("res/raw/liszt_campanella.mid");
        InputStream in = getResources().openRawResource(R.raw.liszt_campanella);
        byte[] buff = new byte[1024];
        int read = 0;
        try{
            FileOutputStream out = new FileOutputStream(getCartellaPredefinita()+file.getName());
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        }
        catch(IOException ioex){
            ioex.printStackTrace();
            Snackbar.make(getWindow().getDecorView().getRootView(), "Non riesco a creare db nella cartella", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        Brano b = new Brano(file); //titolo , percorso , difficolta
        if(db.trovaBrano(b.idBrano) == null) {
            db.inserisci(b);
            Snackbar.make(getWindow().getDecorView().getRootView(), "Il file "+ b.getTitolo()+" è stato inserito nel db!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else{
            Snackbar.make(getWindow().getDecorView().getRootView(), "Il file "+ b.getTitolo()+" è gia presente nel DataBase!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
    void debugggerDb(){
        String debugTag= "debug_db";
        for(int i =0;i<5;i++)Log.i(debugTag,"======================================================" );

        List<Brano> lb = db.prendiTuttiBrani();
        List<Utente> lu = db.prendiTuttiUtenti();
        for (Brano b : lb ) {
            Log.i(debugTag,"Brano: "+b.getTitolo() + "\t\t\t\t\t auth: "+b.getAutore()+"\t\tdiff: "+b.getDifficoltà()+"\t\t\tid: "+b.getIdBrano()+"\t\tautoval:" +b.getAutovalutazione() );
        }
        for (Utente x : lu ) {
            Log.i(debugTag,"Utente id: "+x.getIdUtente() + "\t nome: "+x.getNickName()+"\t\t strum: "+x.getStrumento()
                    +"\t\tmaxP: "+x.getPunteggioMassimo()+"\t\tmedioP:" +x.getPunteggioMedio()+"\t\ttotbrani: "+x.getBraniUtente().size() );
        }
        for(int i =0;i<5;i++)Log.i(debugTag,"======================================================" );
    }
}
