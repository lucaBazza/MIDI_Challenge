package midiapp.midi_challenge;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MetronomoActivity extends GenericMIDIChallengeActivity {
    private int sound = R.raw.a440hz05sec;
    private TextView tv_counterBPM = null;
    private int bpm = 120;
    private MediaPlayer mplayer;
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronomo);

        display(bpm);
        mplayer = MediaPlayer.create(this,sound);

        tv_counterBPM = (TextView) findViewById(R.id.counterBPM);
        bpm = Integer.parseInt(tv_counterBPM.getText().toString());

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mplayer.start();
                startMetronomo();
            }
        });
    }

    public void increaseInteger(View view) {
        if (bpm < 300) {
            bpm = bpm + 4;
            display(bpm);
        } else {}
    }
    public void decreaseInteger(View view) {
        if(bpm > 0) {
            bpm = bpm - 4;
            display(bpm);
        } else {}
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById( R.id.counterBPM);
        displayInteger.setText("" + number);
    }

    private  void startMetronomo(){
        int periodoTimer = 500;
        Timer timer = new Timer("MetronomeTimer", true);
        TimerTask tone = new TimerTask() {
            @Override
            public void run() {  //riproduzione
                try{
                    mplayer.start();
                }
                catch(Exception ex){
                    Snackbar.make(getWindow().getDecorView().getRootView(), ex.toString() , Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        };
        timer.scheduleAtFixedRate(tone, 600000/bpm, periodoTimer);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }
}
