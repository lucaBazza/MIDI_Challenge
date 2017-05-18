package midiapp.midi_challenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import android.media.MediaPlayer;
import android.icu.text.DateFormat;
import java.sql.Date;
import android.icu.text.SimpleDateFormat;


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
    start_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            music = MediaPlayer.create(MainActivity.this, R.raw.beep); //aggiunto

            Timer timer = new Timer("MetronomeTimer", true);
            TimerTask tone = new TimerTask() {
                @Override
                public void run() {
                    //Log
                    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SS");
                    Date date = new Date();
                    j++;
                    Log.i("Beep", df.format(date) + "_____" + j);
                    //Play sound
                    music.start();
                }
            };
            timer.scheduleAtFixedRate(tone, 500, 500); // ogni 500 ms
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
