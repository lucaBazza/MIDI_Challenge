package midiapp.midi_challenge;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import midiapp.midi_challenge.accordatoreClasses.ConfigFlags;
import midiapp.midi_challenge.accordatoreClasses.SoundAnalyzer;
import midiapp.midi_challenge.accordatoreClasses.Tuning;
import midiapp.midi_challenge.accordatoreClasses.UiController;

public class AccordatoreActivity extends GenericMIDIChallengeActivity {   //No drawer view found with gravity LEFT
    // switch off gc logs.
    public static final String TAG = "Accordatore_tag";     // System.setProperty("log.tag.falvikvm", "SUPPRESS");
    private final boolean LAUNCHANALYZER = true;

    private ImageView guitar = null;
    private ImageView tune = null;
    private Spinner tuningSelector = null;
    private SoundAnalyzer soundAnalyzer = null ;
    private UiController uiController = null;
    private TextView mainMessage = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
        setContentView(R.layout.activity_accordatore);
        uiController = new UiController(this);
        setTitle("Accordatore");
        if(LAUNCHANALYZER) {
            try {
                soundAnalyzer = new SoundAnalyzer();
            } catch(Exception e) {
                Toast.makeText(this, "Ci sono problemi con il tuo microfono :(", Toast.LENGTH_LONG ).show();
                Log.e(TAG, "Exception when instantiating SoundAnalyzer: " + e.getMessage());
            }
            soundAnalyzer.addObserver(uiController);
        }
        guitar = (ImageView)findViewById(R.id.guitar);
        tune = (ImageView)findViewById(R.id.tune);
        mainMessage = (TextView)findViewById(R.id.mainMessage);
        tuningSelector = (Spinner)findViewById(R.id.spinner_tuner);
        Tuning.populateSpinner(this, tuningSelector);
        tuningSelector.setOnItemSelectedListener(uiController);
    }

    private Map<Integer, Bitmap> preloadedImages;
    private BitmapFactory.Options bitmapOptions;

    private Bitmap getAndCacheBitmap(int id) {
        if(preloadedImages == null)
            preloadedImages = new HashMap<Integer,Bitmap>();
        Bitmap ret = preloadedImages.get(id);
        if(ret == null) {
            if(bitmapOptions == null) {
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 4; // The higher it goes, the smaller the image.
            }
            ret = BitmapFactory.decodeResource(getResources(), id, bitmapOptions);
            preloadedImages.put(id, ret);
        }
        return ret;
    }

    public void dumpArray(final double [] inputArray, final int elements) {
        Log.d(TAG, "Starting File writer thread...");
        final double [] array = new double[elements];
        for(int i=0; i<elements; ++i)
            array[i] = inputArray[i];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { // catches IOException below
                    // Location: /data/data/your_project_package_structure/files/samplefile.txt
                    String name = "Chart_" + (int)(Math.random()*1000) + ".data";
                    FileOutputStream fOut = openFileOutput(name,
                            Context.MODE_WORLD_READABLE);
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);

                    // Write the string to the file
                    for(int i=0; i<elements; ++i)
                        osw.write("" + array[i] + "\n");
					/* ensure that everything is
					 * really written out and close */
                    osw.flush();
                    osw.close();
                    Log.d(TAG, "Successfully dumped array in file " + name);
                } catch(Exception e) {
                    Log.e(TAG,e.getMessage());
                }
            }
        }).start();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(ConfigFlags.menuKeyCausesAudioDataDump) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                Log.d(TAG,"Menu button pressed");
                Log.d(TAG,"Requesting audio data dump");
                soundAnalyzer.dumpAudioDataRequest();
                return true;
            }
        }
        return false;
    }

    private int [] stringNumberToImageId = new int[]{
            R.drawable.strings0,
            R.drawable.strings1,
            R.drawable.strings2,
            R.drawable.strings3,
            R.drawable.strings4,
            R.drawable.strings5,
            R.drawable.strings6
    };

    int oldString = 0;  // 1-6 strings (ascending frequency), 0 - no string.
    public void changeString(int stringId) {
        if(oldString!=stringId) {
            guitar.setImageBitmap(getAndCacheBitmap(stringNumberToImageId[stringId]));
            oldString=stringId;
        }
    }

    int [] targetColor =         new int[]{160,80,40};
    int [] awayFromTargetColor = new int[]{160,160,160};

    public void coloredGuitarMatch(double match) {
        tune.setBackgroundColor(
                Color.rgb((int)(match*targetColor[0]+ (1-match)*awayFromTargetColor[0]),
                        (int)(match*targetColor[1]+ (1-match)*awayFromTargetColor[1]),
                        (int)(match*targetColor[2]+ (1-match)*awayFromTargetColor[2])));

    }

    public void displayMessage(String msg, boolean positiveFeedback) {
        int textColor = positiveFeedback ? Color.rgb(34,139,34) : Color.rgb(255,36,0);
        mainMessage.setText(msg);
        mainMessage.setTextColor(textColor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
        if(soundAnalyzer!=null)
            soundAnalyzer.ensureStarted();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart()");
        if(soundAnalyzer!=null)
            soundAnalyzer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop()");
        if(soundAnalyzer!=null)
            soundAnalyzer.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }
}



/*import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AccordatoreActivity extends GenericMIDIChallengeActivity {
    MediaPlayer mPlayer;
    Spinner spinner_tuner;
    private TextView mainMessage = null;

    private Tuning tuning = new Tuning(0);
    private enum MessageClass {
        TUNING_IN_PROGRESS,
        WEIRD_FREQUENCY,
        TOO_QUIET,
        TOO_NOISY,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordatore);
        setTitle("Tool Accordatore");

        mainMessage = (TextView) findViewById(R.id.txt_accordatoreLog);

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

        spinner_tuner = (Spinner)findViewById(R.id.spinner_tuner);
        SoundAnalyzer soundAnalyzer = null;
        if(true) {
            try {
                soundAnalyzer = new SoundAnalyzer();
                soundAnalyzer.start();
                Tuning.populateSpinner(this, spinner_tuner);
                spinner_tuner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) //add int itemno? credo per scegliere l'accordatura, tipo wich selected
                    {
                        int itemno = 0;                                                 //buggato!!!
                        String selectedItem = parent.getItemAtPosition(position).toString();
                        if(selectedItem.equals("Add new category"))
                        {
                            if(tuning.getTuningId() != itemno)
                                tuning = new Tuning(itemno);
                            Log.d("Accordatore","Changed tuning to " + tuning.getName());
                        }
                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent){ /* niente */
                   /* }
                });

            } catch (Exception e) {
                Toast.makeText(this, "Ci sono problemi con il tuo microfono :(", Toast.LENGTH_LONG).show();
                Log.e("Accordatore", "Exception when instantiating SoundAnalyzer: " + e.getMessage());
                return;
            }
        }
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
*/