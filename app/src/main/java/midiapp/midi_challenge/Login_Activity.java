package midiapp.midi_challenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;

public class Login_Activity extends AppCompatActivity {

    static FunzioniDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        db = new FunzioniDatabase(getBaseContext());

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

                Utente u = db.trovaUtente(listaUtenti.get(i).getIdUtente());
                Intent activityUtente = new Intent(getBaseContext(),MainActivity.class);
                activityUtente.putExtra("id_utente",u.getIdUtente());
                startActivity(activityUtente);
            }
        });
    }
}
