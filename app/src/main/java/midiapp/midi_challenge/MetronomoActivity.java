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

    public void addone(View v){
        int numtest = 0;
        TextView t = (TextView) findViewById(R.id.counter);
        t.setText("bo");
    }

    public void subone(View v){

        TextView p = (TextView) findViewById(R.id.counter);
        p.setText("");
    }

}
