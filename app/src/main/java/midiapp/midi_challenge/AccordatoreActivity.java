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
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;

public class AccordatoreActivity extends GenericMIDIChallengeActivity {
    MediaPlayer mPlayer;

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
