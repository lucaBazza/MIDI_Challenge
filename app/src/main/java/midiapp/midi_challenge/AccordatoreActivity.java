package midiapp.midi_challenge;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

public class AccordatoreActivity extends GenericMIDIChallengeActivity {
    MediaPlayer mPlayer;
    Spinner spinner_tuner;

    private Tuning tuning = new Tuning(0);
    private enum MessageClass {
        TUNING_IN_PROGRESS,
        WEIRD_FREQUENCY,
        TOO_QUIET,
        TOO_NOISY,
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordatore);
        setTitle("Tool Accordatore");

        ImageButton imgBtnDiapason = (ImageButton) findViewById(R.id.imgBtnDiapason);
        imgBtnDiapason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNota();
            }
        });
        mPlayer = MediaPlayer.create(this,R.raw.a440hz05sec);

        ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("Accordatore");

        spinner_tuner = (Spinner)findViewById(R.id.spinner_tuner);
        SoundAnalyzer soundAnalyzer = null;
        if(true) {
            try {
                soundAnalyzer = new SoundAnalyzer();
                soundAnalyzer.start();
                Tuning.populateSpinner(this, spinner_tuner);
                spinner_tuner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) //add int itemno? credo per scegliere l'accordatura, tipo wich selected
                    {
                        int itemno = 0;                                                 //buggato!!!
                        String selectedItem = parent.getItemAtPosition(position).toString();
                        if(selectedItem.equals("Add new category"))
                        {
                            if(tuning.getTuningId() != itemno)
                                tuning = new Tuning(itemno);
                            Log.d("Accordatore","Changed tuning to " + tuning.getName());
                        }
                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent){ /* niente */ }
                });

            } catch (Exception e) {
                Toast.makeText(this, "The are problems with your microphone :(", Toast.LENGTH_LONG).show();
                Log.e("Accordatore", "Exception when instantiating SoundAnalyzer: " + e.getMessage());
                return;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }
    
    public void onDestroy() {
        mPlayer.stop();
        super.onDestroy();
    }

    public void playNota(){
        Log.println(Log.ASSERT,"evento","playNota");        //MediaPlayer.create(FakeCallScreen.this, R.raw.mysoundfile);
        mPlayer.start();
    }
}
