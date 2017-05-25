package midiapp.midi_challenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static midiapp.midi_challenge.MainActivity.funzioniDatabase;

public class ActivityPaginaUtente extends GenericMIDIChallengeActivity {
    Utente utente = null;
    ImageView imgProfilo;
    TextView tw_log_pagUser;
    Button btnChangeStr;
    TextView tbStrum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pagina_utente);
        super.onCreate(savedInstanceState);

        funzioniDatabase = getDb();

        if(getIntent().hasExtra("id_utente")){
            long idUtTmp = getIntent().getLongExtra("id_utente",-1);
            utente = funzioniDatabase.trovaUtente(idUtTmp);
        }

        if(utente!=null) setTitle("Pagina Utente: "+ utente.getNickName());
        else setTitle("Pagina Utente - NONAME");

        tw_log_pagUser = (TextView)findViewById(R.id.tw_log_pagUser);

        imgProfilo = (ImageView) findViewById(R.id.imageViewFotoUtente);    // CARICA FOTO UTENTE
        File imgFile = new File(utente.getFoto());
        if(!utente.getFoto().isEmpty() && imgFile.exists()){           // non trovando il file comunque entra nel if
            Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()) ,500,500,true);
            imgProfilo.setImageBitmap(myBitmap);
        }
        else {
            imgProfilo.setImageResource(R.mipmap.generic_user_mc);
        }

        Button btnCancellaBrani = (Button)findViewById(R.id.buttonCancellaBrani);           //CANCELLA TUTTI I BRANI
        btnCancellaBrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Toast.makeText(getBaseContext(),"Cancella lista brani!",Toast.LENGTH_SHORT).show();
                AltDlgCancellaTuttiBrani();
            }
        });

        utente.braniUtente = funzioniDatabase.braniUtente(utente.idUtente);     //AGGIORNA STATISTICHE
        TextView tbPuntMax = (TextView) findViewById(R.id.textViewPU1);
        int max = 0; int media = 0; int tot = 0;
        for(int i =0; i<utente.braniUtente.size();i++){
            tot++;
            media+=utente.braniUtente.get(i).difficoltà;
            if(utente.braniUtente.get(i).difficoltà>max){
                max = utente.braniUtente.get(i).difficoltà;
                tbPuntMax.setText("Punteggio massimo: "+ Integer.toString(max)+" - "+utente.braniUtente.get(i).getTitolo());  }
        }
        TextView tbPuntMedio = (TextView) findViewById(R.id.textViewPU2);
        if(tot!= 0)
            { media/=tot; }
            tbPuntMedio.append(Integer.toString(media));
        TextView tvBRaniTot = (TextView) findViewById(R.id.textViewPU3);
            tvBRaniTot.append(Integer.toString(tot));

        btnChangeStr = (Button) findViewById(R.id.btnChangeStr);        // STRUMENTO
        tbStrum = (TextView)findViewById(R.id.tbStrum);
        if(!utente.getStrumento().isEmpty())
            tbStrum.append(utente.getStrumento());
        btnChangeStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AltDlgCambiaStrum();
            }
        });

        Button btnCambiaFoto = (Button) findViewById(R.id.btnCambiaFoto);       //CAMBIA FOTO PROFILO
        btnCambiaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AltDlgCambiaFoto();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //crea menù sulla barra in alto
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }

    void AltDlgCancellaTuttiBrani(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityPaginaUtente.this,R.style.Theme_AppCompat_Dialog);
        builder1.setMessage("Sicuro di cancellare la tua lista pezzi?"); builder1.setTitle("Attenzione!");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.ic_menu_manage);

        builder1.setPositiveButton( "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        funzioniDatabase.cancLinksTuttiBraniUtente(utente.idUtente); //CANCELLA TUTTI I PEZZI DI UN UTENTE!
                        dialog.cancel(); //((TextView) findViewById(R.id.textViewPU2)).append("YESSA");
                    }
                });
        builder1.setNegativeButton( "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); //((TextView) findViewById(R.id.textViewPU2)).append("Noohh");
                    }
                });
        final AlertDialog alert = builder1.create();
        alert.show();
    }

    void AltDlgCambiaFoto(){
        final CharSequence[] cartelle = {"Download","Cartella predefinita","Galleria foto","WhatsApp","Cancella"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPaginaUtente.this);

        builder.setTitle("Scegli cartella");
        builder.setItems(cartelle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (cartelle[item].equals("Download")) {
                    AltDlgCambiaFotoPt2("Download");
                } else if (cartelle[item].equals("Cartella predefinita")) {
                    AltDlgCambiaFotoPt2("MidiChallenge");
                }
                else if (cartelle[item].equals("Galleria foto")) {
                    AltDlgCambiaFotoPt2("DCIM/Camera");
                }
                else if (cartelle[item].equals("WhatsApp")) {
                    AltDlgCambiaFotoPt2("WhatsApp/Media/WhatsApp Images");
                }
                else if (cartelle[item].equals("Cancella")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    void AltDlgCambiaFotoPt2(final String cartella){
        final ArrayList<String> ArrFoto = new ArrayList<>(); // = {"Paolo","Cartella predefinita","Cartella foto","Cancella"};
        switch(cartella){
            case "Download": {
                File downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File[] fotoFiles = downloadFolderPath.listFiles(photoFilter);
                if(fotoFiles != null){
                    for(File f : fotoFiles){
                        ArrFoto.add(f.getName());
                    }
                } break;
            }
            case "MidiChallenge": {
                File downloadFolderPath = Environment.getExternalStoragePublicDirectory("MidiChallenge");
                File[] fotoFiles = downloadFolderPath.listFiles(photoFilter);
                if(fotoFiles != null){
                    for(File f : fotoFiles){
                        ArrFoto.add(f.getName());
                    }
                } break;
            }
            case "DCIM/Camera": {
                File downloadFolderPath = Environment.getExternalStoragePublicDirectory("DCIM/Camera");
                File[] fotoFiles = downloadFolderPath.listFiles(photoFilter);
                if(fotoFiles != null){
                    for(File f : fotoFiles){
                        ArrFoto.add(f.getName());
                    }
                } break;
            }
            case "WhatsApp/Media/WhatsApp Images": {
                File downloadFolderPath = Environment.getExternalStoragePublicDirectory("WhatsApp/Media/WhatsApp Images");
                File[] fotoFiles = downloadFolderPath.listFiles(photoFilter);
                if(fotoFiles != null){
                    for(File f : fotoFiles){
                        ArrFoto.add(f.getName());
                    }
                } break;
            }
        }
        final CharSequence[] foto = ArrFoto.toArray(new CharSequence[ArrFoto.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPaginaUtente.this);
        builder.setTitle("Scegli foto");
        builder.setItems(foto, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                File imgFile = new File("/sdcard/"+cartella+"/"+foto[item]);
                Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()) ,500,500,true);
                imgProfilo.setImageBitmap(myBitmap);
                utente.foto = "/sdcard/"+cartella+"/"+foto[item];
                try{
                    if(funzioniDatabase.aggiornaUtente(utente)!=-1)
                        tw_log_pagUser.setText("Aggiornato database!");
                }
                catch (Exception ex){
                    Toast.makeText(getBaseContext(),"errore: "+ex.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("error",ex.toString());
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    void AltDlgCambiaStrum(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPaginaUtente.this,R.style.Theme_AppCompat_Dialog);
        builder.setTitle("Cambia strumento: ");
        final EditText input = new EditText(this);   // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tbStrum.setText("Strumento: "+input.getText().toString());
                utente.setStrumento(input.getText().toString());
                if(funzioniDatabase.aggiornaUtente(utente)!=-1)
                    tw_log_pagUser.setText("Aggiornato database!");
                else tw_log_pagUser.setText("Errore aggiornamento database!");
            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    FileFilter photoFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            if (file.getName().contains(".jpg")|| file.getName().contains(".png")) {
                return true;
            } else {
                return false;
            }
        }
    };
}