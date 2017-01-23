package midiapp.midi_challenge;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class Impostazioni_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni_);
        this.setTitle("Impostazioni");

        ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
    }

    public void openNewActivity(View view) {
        Intent startActivity = new Intent(this, Activity_info.class);
        startActivity(startActivity);
    }
}
