package midiapp.midi_challenge;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.*;
import java.security.Permissions;

import android.os.*;
import android.widget.TextView;

import com.pdrogfer.mididroid.MidiFile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MidiFile mf = new MidiFile();

        TextView tv = (TextView)findViewById(R.id.textView);  //Find the view by its id
        TextView log = (TextView)findViewById(R.id.textViewLog);
        log.setTextColor(Color.RED);

        ActivityCompat.requestPermissions( this ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard,"file.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            log.setText(e.toString());
        }
        tv.setText(text);

        //Set the text
        /*if(text.length()>1)
            tv.setText(text);*/
        //else
        //    tv.setText(text);

    }
}
