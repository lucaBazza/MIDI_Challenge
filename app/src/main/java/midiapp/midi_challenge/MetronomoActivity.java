package midiapp.midi_challenge;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MetronomoActivity extends GenericMIDIChallengeActivity {

    private int sound = R.raw.click01;
    private int bpm = 120;
    Timer mainTimer;
    Timer subTimer;
    MyTimerTask mainTimerTask;
    MyTimerTask subTimerTask;
    int swing = ((60 * 1000)/bpm)/12;
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronomo);

        /*tv_counterBPM = (TextView) findViewById(R.id.counterBPM);
        tv_metronomoLog = (TextView) findViewById(R.id.tv_metronomoLog);
        bpm = Integer.parseInt(tv_counterBPM.getText().toString()); */

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void play() {
        mainTimer = new Timer("MetronomeTimer", true);
        subTimer = new Timer();
        mainTimerTask = new MyTimerTask();
        subTimerTask = new MyTimerTask();

        mainTimer.schedule(mainTimerTask, 0, 600000 / bpm);
        subTimer.schedule(subTimerTask, (300 * (100 + swing)) / bpm, 600000 / bpm);

    }


    private void playSound() {
        final MediaPlayer mplayer = MediaPlayer.create(this, sound);
        if(mplayer==null)
            Snackbar.make(getWindow().getDecorView().getRootView(), "mplayer null", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        try {
            mplayer.prepare();
            mplayer.start();
            mplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
        catch (IOException ioex){ ioex.printStackTrace();}
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            playSound();
        }
    }
}


/*

public class MetronomoActivity extends GenericMIDIChallengeActivity {
    private int sound = R.raw.a440hz05sec;
    private TextView tv_counterBPM = null;
    private TextView tv_metronomoLog = null;
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
        tv_metronomoLog = (TextView) findViewById(R.id.tv_metronomoLog);
        bpm = Integer.parseInt(tv_counterBPM.getText().toString());

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMetronomo();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void increaseInteger(View view) {
        if (bpm < 300) {
            bpm = bpm + 4;
            display(bpm);
        }
    }
    public void decreaseInteger(View view) {
        if(bpm > 0) {
            bpm = bpm - 4;
            display(bpm);
        }
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById( R.id.counterBPM);
        displayInteger.setText("" + number);
    }

    private  void startMetronomo(){

        final int periodoTimer = 500;
        Timer timer = new Timer("MetronomeTimer", true);

        TimerTask tone = new TimerTask() {
            @Override
            public void run() {
                try{
                    mplayer.prepare();
                    mplayer.start();
                }
                catch(Exception ex){
                    Snackbar.make(getWindow().getDecorView().getRootView(), ex.toString() , Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        };

        timer.scheduleAtFixedRate(tone, 600000/bpm, periodoTimer);

        new java.util.Timer().schedule(new TimerTask(){
            int contaTempoPassato = 0;
            @Override
            public void run() {
                try{
                    contaTempoPassato += periodoTimer;
                    tv_metronomoLog.setText("tempo passato: "+contaTempoPassato);
                    mplayer.start();
                }
                catch(Exception ex){
                    Snackbar.make(getWindow().getDecorView().getRootView(), ex.toString() , Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        },periodoTimer,periodoTimer);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }
}
*/
