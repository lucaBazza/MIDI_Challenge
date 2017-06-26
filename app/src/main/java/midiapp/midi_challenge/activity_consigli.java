package midiapp.midi_challenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static midiapp.midi_challenge.activity_MainRestyled.funzioniDatabase;


public class activity_consigli extends GenericMIDIChallengeActivity {

    Button btnLinkJazzAdvice;
    Button btnLinkJazzitalia;
    ListView lista_braniSuggeriti;
    TextView tv_logConsigli;
    Utente utente;
    ArrayAdapter ArrayAdapterListaBrani = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consigli_layout);          //Toast.makeText(getBaseContext(),"messaggio",Toast.LENGTH_LONG).show();

        ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("Consigli");

        if(getIntent().hasExtra("id_utente")){
            long idUtTmp = getIntent().getLongExtra("id_utente",-1);
            utente = funzioniDatabase.trovaUtente(idUtTmp);
        }
        else
            Toast.makeText(getBaseContext(),"Non trovo utente in <extra> intent",Toast.LENGTH_LONG).show();
        tv_logConsigli = (TextView) findViewById(R.id.tv_logConsigli);


        btnLinkJazzAdvice = (Button)findViewById(R.id.btnLinkJazzAdvice);
        btnLinkJazzAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.jazzadvice.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        btnLinkJazzitalia = (Button)findViewById(R.id.btnLinkJazzitalia);
        btnLinkJazzitalia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.jazzitalia.net/lezioni.asp";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        lista_braniSuggeriti = (ListView) findViewById(R.id.lista_braniSuggeriti);
        List <Brano> braniConsigliatiDb = new ArrayList<>();
        final List<Brano> braniDb = activity_MainRestyled.funzioniDatabase.prendiTuttiBrani();     //occorre creare lista filtrata di brani con difficolta simile a quella dell'utente
        /*Collections.sort(braniDb, new Comparator<Brano>() {
            public int compare(Brano c1, Brano c2) {
                if (c1.getDifficoltà() > c2.getDifficoltà()) return 1;  //ordinati con difficolta crescente
                if (c1.getDifficoltà() < c2.getDifficoltà()) return -1;
                //if((c1.getDifficoltà()-c2.getDifficoltà()) > utenteCorrente.getPunteggioMedio() ) return 1;
                //if((c1.getDifficoltà()-c2.getDifficoltà()) <= utenteCorrente.getPunteggioMedio() ) return -1;
                return 0;
            }
        }); */
        Log.i("cons_log","Punteggio medio utente: "+utenteCorrente.getPunteggioMedio());
        for(Brano b : braniDb){
            if(b.getDifficoltà()==-1) continue;
            //int delta = b.getDifficoltà()-utenteCorrente.getPunteggioMedio();
            //Log.i("cons_log",b.getTitolo()+"\t\t\t\t\t\t\t\t\t\tdiff: "+b.getDifficoltà()+"\t\t delta: "+delta);
            braniConsigliatiDb.add(b);
        }
        Collections.sort(braniConsigliatiDb, new Comparator<Brano>() {
            @Override
            public int compare(Brano o1, Brano o2) {
                int delta1 = o1.getDifficoltà()-utenteCorrente.getPunteggioMedio();
                int delta2 = o2.getDifficoltà()-utenteCorrente.getPunteggioMedio();

                if (delta1 >  delta2) return 1;  //ordinati con difficolta crescente
                if (delta1 <= delta2) return -1;
                return 0;
            }
        });
        for(Brano b : braniConsigliatiDb) {
            Log.i("cons_log","diff ordinata: "+ b.getDifficoltà());
        }


        /*int deltaDiff = 1;
        for(Brano x : braniDb){
            if(x.difficoltà>1)
            {
                Log.i("cons_log","Consigli: "+x.fileBrano.getName()+"\t\t\t\t livello user: "+utenteCorrente.getPunteggioMedio()+ "\t difficolta: "+x.getDifficoltà()+"\t\t delta diff: "+(utenteCorrente.getPunteggioMedio()-x.getDifficoltà()));
                if((utenteCorrente.getPunteggioMedio()-x.getDifficoltà())>deltaDiff){
                    deltaDiff = (utenteCorrente.getPunteggioMedio()-x.getDifficoltà());
                    Log.i("cons_log","Consigli: aumento diff!");
                }
            }
        }*/

        if (braniDb != null) {
            ArrayAdapterListaBrani = new ArrayAdapter<Brano>(this, R.layout.brani_list_element, braniConsigliatiDb);
            lista_braniSuggeriti.setAdapter(ArrayAdapterListaBrani);
            if (braniConsigliatiDb.isEmpty()) {
                Toast.makeText(getBaseContext(),"Non ci sono titoli adatti!",Toast.LENGTH_LONG).show();
                tv_logConsigli.setText("Non ha ancora studiato nessun brano...");
            }
            else
                tv_logConsigli.setText("Totale brani nel db: "+ArrayAdapterListaBrani.getCount());
        } else {
            ArrayAdapterListaBrani.notifyDataSetChanged();
        }
        lista_braniSuggeriti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Brano selezione = braniDb.get(position);
                showMessageOKCancel("Vuoi inserire questo brano consigliato fra i tuoi? "+selezione.fileBrano.getName(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    funzioniDatabase.inserisciBranoPerUtente(utente,selezione,0);
                                    Snackbar.make(getWindow().getDecorView().getRootView(),"brano inserito nel tuo profilo!", Snackbar.LENGTH_LONG).setAction("Action", null).show();}
                                catch(Exception sqlex){
                                    Snackbar.make(getWindow().getDecorView().getRootView(),"errore inserimento db", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    sqlex.printStackTrace();
                                }

                            }
                        });
            }
        });
        if(utenteCorrente.getPunteggioMedio()<100){
            tv_logConsigli.setText("Per adesso i pezzi salvati sono troppo difficili, esercitati solo primo brano!");
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        debugggerDb();                                                                                                      //TOGLIERE NELLA VERSIONE FINALE!!!
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(this,activity_MainRestyled.class);
                i.putExtra("id_utente",getIntent().getLongExtra("id_utente",-1));
                NavUtils.navigateUpTo(this,i);
                break;
        }
        super.onOptionsItemSelected(item);
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
