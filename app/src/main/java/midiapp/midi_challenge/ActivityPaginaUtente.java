package midiapp.midi_challenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

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

        ImageView imgProfilo = (ImageView) findViewById(R.id.imageViewFotoUtente);
        File imgFile = new File("/sdcard/MidiChallenge/paolo.jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgProfilo.setImageBitmap(myBitmap);
        }
        else {
            Toast.makeText(getBaseContext(),"Foto utente non trovata!",Toast.LENGTH_SHORT).show();
        }


        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityPaginaUtente.this,R.style.Theme_AppCompat);
        builder1.setMessage("Sicuro di cancellare la tua lista pezzi?"); builder1.setTitle("Attenzione!");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.ic_add_something);

        builder1.setPositiveButton( "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ((TextView) findViewById(R.id.textViewPU2)).append("YESSA");
                        funzioniDatabase.cancLinksTuttiBraniUtente(utente.idUtente); //CANCELLA TUTTI I PEZZI DI UN UTENTE!
                    }
                });
        builder1.setNegativeButton( "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ((TextView) findViewById(R.id.textViewPU2)).append("Noohh");
                    }
                });
        final AlertDialog alert = builder1.create();


        Button btnCancellaBrani = (Button)findViewById(R.id.buttonCancellaBrani);
        btnCancellaBrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"Cancella lista brani!",Toast.LENGTH_SHORT).show();
                alert.show();
            }
        });

        utente.braniUtente = funzioniDatabase.braniUtente(utente.idUtente);
        TextView tbPuntMax = (TextView) findViewById(R.id.textViewPU1);
        int max = 0; int media = 0; int tot = 0;
        for(int i =0; i<utente.braniUtente.size();i++){
            tot++;
            media+=utente.braniUtente.get(i).difficoltà;
            if(utente.braniUtente.get(i).difficoltà>max){
                max = utente.braniUtente.get(i).difficoltà;
                tbPuntMax.append(Integer.toString(max)+" - "+utente.braniUtente.get(i).titolo);
            }
        }

        TextView tbPuntMedio = (TextView) findViewById(R.id.textViewPU2);
        if(tot!= 0)
            { media/=tot; }
            tbPuntMedio.append(Integer.toString(media));
        TextView tvBRaniTot = (TextView) findViewById(R.id.textViewPU3);
            tvBRaniTot.append(Integer.toString(tot));


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
            case R.id.home :
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("id_utente",getIntent().getLongExtra("id_utente",-1));
                startActivity(i);
        }
        return true;
    }
}
