package midiapp.midi_challenge;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import static midiapp.midi_challenge.MainActivity.funzioniDatabase;

public class ActivityPaginaUtente extends AppCompatActivity {

    Utente utente = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_utente);

        if(getIntent().hasExtra("id_utente")){
            utente = funzioniDatabase.trovaUtente(getIntent().getLongExtra("id_utente",-1));
        }

        if(utente!=null) setTitle("Pagina Utente: "+ utente.getNickName());
        else setTitle("Pagina Utente");

        ImageButton imgBtn = (ImageButton)findViewById(R.id.ImgBtnCancellaListaBrani);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Cancella lista brani!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.show_navigation_drawer :
                DrawerLayout drw = (DrawerLayout)findViewById(R.id.drawer_layout);
                if(!drw.isDrawerOpen(Gravity.LEFT))
                    drw.openDrawer(Gravity.LEFT);
                else
                    drw.closeDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }
}
