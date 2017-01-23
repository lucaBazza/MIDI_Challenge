package midiapp.midi_challenge;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class AccordatoreActivity extends AppCompatActivity {
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordatore);
        setTitle("Tool Accordatore");

        Button btnA440 = (Button) findViewById(R.id.button) ;
        btnA440.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNota();
            }
        });

    }
    public void onDestroy() {
        mPlayer.stop();
        super.onDestroy();

    }

    public void playNota(){
        Log.println(Log.ASSERT,"evento","playNota");
        //MediaPlayer.create(FakeCallScreen.this, R.raw.mysoundfile);
        mPlayer = MediaPlayer.create(this,R.raw.a440hz05sec);
        mPlayer.start();
    }
}