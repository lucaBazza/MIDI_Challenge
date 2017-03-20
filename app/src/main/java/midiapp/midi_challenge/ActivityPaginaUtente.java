package midiapp.midi_challenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ActivityPaginaUtente extends AppCompatActivity {

    Utente utente = null;
    ImageView imgProfilo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_utente);

        if(getIntent().hasExtra("id_utente")){
            utente = funzioniDatabase.trovaUtente(getIntent().getLongExtra("id_utente",-1));
        }

        if(utente!=null) setTitle("Pagina Utente: "+ utente.getNickName());
        else setTitle("Pagina Utente");

        imgProfilo = (ImageView) findViewById(R.id.imageViewFotoUtente);
        File imgFile = new File("/sdcard/"+ utente.getFoto());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgProfilo.setImageBitmap(myBitmap);
        }
        else {
            imgFile = new File("/sdcard/MidiChallenge/user.png");
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgProfilo.setImageBitmap(myBitmap);
            Toast.makeText(getBaseContext(),"Foto utente non trovata!",Toast.LENGTH_SHORT).show();
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
                tbPuntMax.append(Integer.toString(max)+" - "+utente.braniUtente.get(i).titolo);
            }
        }
        TextView tbPuntMedio = (TextView) findViewById(R.id.textViewPU2);
        if(tot!= 0)
            { media/=tot; }
            tbPuntMedio.append(Integer.toString(media));
        TextView tvBRaniTot = (TextView) findViewById(R.id.textViewPU3);
            tvBRaniTot.append(Integer.toString(tot));



        Button btnCambiaFoto = (Button) findViewById(R.id.btnCambiaFoto);       //CAMBIA FOTO PROFILO
        btnCambiaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Toast.makeText(getBaseContext(),"Cancella lista brani!",Toast.LENGTH_SHORT).show();
                AltDlgCambiaFoto();
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
            case R.id.home :
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("id_utente",getIntent().getLongExtra("id_utente",-1));
                startActivity(i);
        }
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
                Toast.makeText(getBaseContext(),"Nuova foto selezionata! "+item,Toast.LENGTH_SHORT).show();
                File imgFile = new File("/sdcard/"+cartella+"/"+foto[item]);
                //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()) ,500,500,true);
                imgProfilo.setImageBitmap(myBitmap);
                dialog.dismiss();
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