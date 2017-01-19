package midiapp.midi_challenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
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

        List<Utente> listaUtenti = db.prendiTuttiUtenti();

        if (!listaUtenti.isEmpty()) {
            ArrayAdapter ad = new ArrayAdapter(this,R.layout.users_grid_element ,R.id.txt_NomeUtenteLogin, listaUtenti);

            grigliaUtenti.setAdapter(ad);

            setTitle("Selezione Utente");
        }
        else {
            setTitle("Nessun Utente Trovato!");
        }
    }
}
