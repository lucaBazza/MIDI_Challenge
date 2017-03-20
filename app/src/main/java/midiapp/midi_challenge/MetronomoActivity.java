package midiapp.midi_challenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MetronomoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronomo);
    }

    public void changeBPM(View view) {
        ((TextView)findViewById(R.id.textBPM)).setText("ciaooooneBPM");
    }

}
