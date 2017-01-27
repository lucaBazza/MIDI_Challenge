package midiapp.midi_challenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Login_Activity extends AppCompatActivity {

    static FunzioniDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        db = new FunzioniDatabase(getBaseContext());

        //controllare valore id utente. DAMMIT
/*        Utente u = new Utente("Paolo","percorso_foto","Strumento",40,10);
        Brano b = new Brano("Titolo","percorso",20);
        db.inserisci(u);
        db.inserisci(b);
        db.inserisciBranoPerUtente(u,b,20);*/
        //

        GridView grigliaUtenti = (GridView) findViewById(R.id.layout_griglia_utenti);

        final List<Utente> listaUtenti = db.prendiTuttiUtenti();

        if (!listaUtenti.isEmpty()) {
            ArrayAdapter ad = new ArrayAdapter(this,R.layout.users_grid_element,R.id.txt_NomeUtenteLogin, listaUtenti);

            grigliaUtenti.setAdapter(ad);

            setTitle("Selezione Utente");
        }
        else {
            setTitle("Nessun Utente Trovato!");
        }

        grigliaUtenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utente u = listaUtenti.get(i);
                Intent loginIntent = new Intent(getBaseContext(),MainActivity.class);
                loginIntent.putExtra("id_utente",u.getIdUtente());
                startActivity(loginIntent);
            }
        });
    }
}
