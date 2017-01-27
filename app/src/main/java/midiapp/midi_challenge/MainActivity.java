package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.*;
import java.util.List;

import android.os.*;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pdrogfer.mididroid.MidiFile;

public class MainActivity extends AppCompatActivity {
    //Static Declarations
    static TextView log, tv;

    private String[] mActivityTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private Utente utente; //utente corrente
    private Brano brano; //BRANO DI PROVA  -- DEBUG
    static FunzioniDatabase funzioniDatabase = null;
    //MidiFile mf;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        funzioniDatabase = new FunzioniDatabase(this.getBaseContext());
        setContentView(R.layout.activity_main);

        //====================================================================================================================
        //public Utente(String nickName, String foto, String strumento, int punteggioMassimo, int punteggioMedio)
        utente = new Utente("Paolo","URLfoto","SEX",1205,324);                                                      //UTENTE DI DEBUG
        //public Brano(long idBrano, String titolo, String nomeFile, int difficoltà,int autovalutazione) {
        brano = new Brano(0,"campanella","campanella.mid",-1,-1);                                                   //BRANO DI DEBUG

        funzioniDatabase.inserisciBranoPerUtente(utente,brano,-1);                                                  //LINK UTENTE-BRANO DI DEBUG
        //====================================================================================================================

        ListView lista_brani = (ListView) findViewById(R.id.lista_brani_utente);
        List braniUtente = funzioniDatabase.braniUtente(utente.getIdUtente());
        ArrayAdapter<Brano> bn = new ArrayAdapter<Brano>(this,R.layout.brani_list_element,braniUtente);

        lista_brani.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"uh",Toast.LENGTH_LONG);
            }
        });

        lista_brani.setAdapter(bn);

        tv = (TextView)findViewById(R.id.textView);  //Find the view by its id
        tv.setMovementMethod(new ScrollingMovementMethod());

        //mf = new MidiFile();
        //ActivityCompat.requestPermissions( MainActivity.this  ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        mActivityTitles = new String[]{"Home","Profilo","Registratore","Metronomo","Accordatore", "Impostazioni","Logout"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        ArrayAdapter<String> xxxx  = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mActivityTitles);        mDrawerList.setAdapter(xxxx);
        // Set the list's click listener    //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent testIntent;
                switch (position){ // "Home","Profilo","Registratore","Metronomo","Accordatore, Impostazioni","Logout"
                    case 0: testIntent = new Intent(getApplicationContext(),MainActivity.class); break;
                    //case 1: testIntent = new Intent(getApplicationContext(),Login_Activity.class); break;
                    case 2: testIntent = new Intent(getApplicationContext(),Registratore_Activity.class); break;
                    //case 3: testIntent = new Intent(getApplicationContext(),MetronomoActivity.class); break;
                    case 4: testIntent = new Intent(getApplicationContext(),AccordatoreActivity.class); break;
                    case 5: testIntent = new Intent(getApplicationContext(),Impostazioni_Activity.class); break; //IMPOSTAZIONI ACTIVITY
                    case 6: testIntent = new Intent(getApplicationContext(),Login_Activity.class); break; //Domanda prima di uscire!
                    default: testIntent = null;
                }
                if(testIntent!= null) {  //Log.println(Log.ASSERT,"activity","Pos: "+ position+"  activity: "+testIntent.toString());
                    startActivity(testIntent);
                }
            }
        });
        setTitle(utente.getNickName());
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
                    drw.openDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }
}