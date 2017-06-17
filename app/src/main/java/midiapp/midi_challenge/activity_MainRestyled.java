package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class activity_MainRestyled extends GenericMIDIChallengeActivity   implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayAdapter<Brano> ArrayAdapterListaBrani; // per inserire i dati nella lista
    private Utente utente; //utente corrente
    static FunzioniDatabase funzioniDatabase = null;
    private TextView tv_mainActivity_log;

    DrawerLayout drawer; // autogenerato in on backpressed
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main_restyled);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent i = new Intent(getBaseContext(), Aggiunta_Brano_Activity.class);
                    i.putExtra("id_utente", utente.getIdUtente());
                    startActivity(i);
                }
                else {
                Snackbar.make(view, "No pulsante!", Snackbar.LENGTH_LONG).setAction("Action", null).show(); }
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tv_mainActivity_log = (TextView) findViewById(R.id.tv_mainActivity_log);

        inizializzaMainRestyled();  // metodo che raggruppa tutte le funzioni
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__main_restyled, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")


    private  void inizializzaMainRestyled(){
        funzioniDatabase = getDb();
        File cartellaPredefinita = getCartellaPredefinita();

        if (!cartellaPredefinita.exists())
            cartellaPredefinita.mkdir();

        utente = utenteCorrente;
        if (utente == null) {
            ChooseUserDialog dg = new ChooseUserDialog();
            dg.show(getFragmentManager(), "Login");
        }
        else{
            final ListView ListViewXmlListaBrani = (ListView) findViewById(R.id.lista_brani_utente);
            final List<Brano> braniUtente = funzioniDatabase.braniUtente(utente.getIdUtente());

            if (braniUtente != null) {
                ArrayAdapterListaBrani = new ArrayAdapter<Brano>(this, R.layout.brani_list_element, braniUtente);
                ListViewXmlListaBrani.setAdapter(ArrayAdapterListaBrani);
                if (braniUtente.isEmpty()) {
                    Toast.makeText(getBaseContext(),"Clicca su + per aggiungere dei brani alla tua lista!",Toast.LENGTH_LONG).show();
                    tv_mainActivity_log.setText("Non ha ancora studiato nessun brano...");
                }
                else
                    tv_mainActivity_log.setText("Totale brani: "+ArrayAdapterListaBrani.getCount());
            } else {
                ArrayAdapterListaBrani.notifyDataSetChanged();
            }
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

            setTitle(utente.getNickName());
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 2 :
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else Toast.makeText(activity_MainRestyled.this, "Permessi non concessi!", Toast.LENGTH_SHORT).show();
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
}