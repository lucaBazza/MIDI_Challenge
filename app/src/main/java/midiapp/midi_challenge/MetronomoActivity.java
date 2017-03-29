package midiapp.midi_challenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MetronomoActivity extends AppCompatActivity {
    int minteger = 120;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronomo);
    }

    public void increaseInteger(View view) {
        minteger = minteger + 4;
        display(minteger);

    }public void decreaseInteger(View view) {
        minteger = minteger - 4;
        display(minteger);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.counter);
        displayInteger.setText("" + number);
    }
}


/*
    public void suonoBPM(View v){
        TextView t = (TextView) findViewById(R.id.counter);
        int bpm = Integer.getInteger(t.getText().toString());
        //prendi audio
        //riproduci ogni min/bpm
    }
*/
