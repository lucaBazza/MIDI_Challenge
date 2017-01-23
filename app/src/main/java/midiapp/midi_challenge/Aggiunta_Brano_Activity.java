package midiapp.midi_challenge;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Aggiunta_Brano_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta__brano);

        ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("Aggiugi Brano...");
    }
}
