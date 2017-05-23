package midiapp.midi_challenge;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.media.MediaPlayer;

public class MetronomoActivity extends GenericMIDIChallengeActivity {
    int cont = 120;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronomo);
    }

    public void increaseInteger(View view) {
        if (cont < 300) {
            cont = cont + 4;
            display(cont);
        } else {}

    }public void decreaseInteger(View view) {
        if(cont > 0) {
            cont = cont - 4;
            display(cont);
        } else {}
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.counter);
        displayInteger.setText("" + number);

        
    }


    /**

     //da finire creazione suono

    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200); //200ms

    Button start_button = (Button) findViewById(R.id.playButton);

    start_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Timer timer = new Timer("MetronomeTimer", true);
            TimerTask tone = new TimerTask() {
                @Override
                public void run() {
                    //Log
                    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SS");
                    Date date = new Date();
                    Log.i("Beep", df.format(date) + "_____");
                    //Play sound
                    music = MediaPlayer.create(MetronomoActivity.this, R.raw.beep);
                    music.start();
                }
            };
            timer.scheduleAtFixedRate(tone, 500, 500); // ogni 500ms
        }
    });

*/


}



/*
    public void suonoBPM(View v){
        TextView t = (TextView) findViewById(R.id.counter);
        int bpm = Integer.getInteger(t.getText().toString());
        //prendi audio
        //riproduci ogni min/bpm
    }
*/
