package midiapp.midi_challenge;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;


public class Impostazioni_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni_);
        this.setTitle("Impostazioni");

        android.support.v7.app.ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);

    }

    public void openNewActivity(View view) {
        Intent startActivity = new Intent(this, Info_Activity.class);
        startActivity(startActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("id_utente",getIntent().getLongExtra("id_utente",-1));
                NavUtils.navigateUpTo(this,i);
                break;
        }
        return true;
    }

}
