package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Login_Activity extends AppCompatActivity {

    static FunzioniDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        db = new FunzioniDatabase(getBaseContext());

        final GridView grigliaUtenti = (GridView) findViewById(R.id.layout_griglia_utenti);

        final List<Utente> listaUtenti = db.prendiTuttiUtenti();

        final ArrayAdapter ad = new ArrayAdapter(this,R.layout.users_grid_element,R.id.txt_NomeUtenteLogin, listaUtenti);
        grigliaUtenti.setAdapter(new ImageAdapter(this,listaUtenti));

        if (!listaUtenti.isEmpty()) { setTitle("Pagina login"); }
        else { setTitle("Crea il primo utente!"); }

        grigliaUtenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Utente u = db.trovaUtente(listaUtenti.get(i).getIdUtente());
                Intent activityUtente = new Intent(getBaseContext(),MainActivity.class);
                activityUtente.putExtra("id_utente",u.getIdUtente());
                startActivity(activityUtente);
            }
        });

        Button btnInserimentoUtente = (Button) findViewById(R.id.btn_nuovo_utente);
        btnInserimentoUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txt = (EditText) findViewById(R.id.edtTxt_nomeUtente);
                LinearLayout layoutInserimento = (LinearLayout)findViewById(R.id.layout_inserimento_dati);

                Utente u = new Utente(txt.getText().toString(),"","",0,0);
                long idNuovoUtente =  db.inserisci(u);
                if(idNuovoUtente != -1){
                    Toast.makeText(getBaseContext(),"Inserimento Avvenuto con Successo",Toast.LENGTH_SHORT).show();
                    listaUtenti.add(u);
                    ad.notifyDataSetChanged();
                    grigliaUtenti.refreshDrawableState();
                }
                else{
                    Toast.makeText(getBaseContext(),"Errore durante l'inserimento",Toast.LENGTH_SHORT).show();
                }
                layoutInserimento.setVisibility(View.INVISIBLE);
            }
        });

        //richiede i permessi per leggere la partizione esterna
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

        File externalRootDir = Environment.getExternalStorageDirectory();
        File appExternalDir = new File(externalRootDir,"Midi Challenge");
        if(!appExternalDir.exists())
            appExternalDir.mkdir();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.login_activity_action_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_user_button:
            LinearLayout layoutInserimento = (LinearLayout) findViewById(R.id.layout_inserimento_dati);

            if (layoutInserimento != null) {
                if (layoutInserimento.getVisibility() == View.INVISIBLE)
                    layoutInserimento.setVisibility(View.VISIBLE);
                else
                    layoutInserimento.setVisibility(View.INVISIBLE);
                return true;
            }
            break;
        }
        return false;
    }
}
