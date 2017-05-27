package midiapp.midi_challenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Registratore_Activity extends GenericMIDIChallengeActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    //private RecordButton mRecordButton = null;
    MediaRecorder mRecorder = null;
    //private PlayButton mPlayButton = null;
    private MediaPlayer mPlayer = null;

    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    TextView tvlogRec;
    FloatingActionButton fab_shareRec;
    Button btnReg;
    Button btnRip;

    @Override
    public void onCreate(Bundle icicle) {
        setContentView(R.layout.activity_registratore_);
        super.onCreate(icicle);
        tvlogRec = (TextView)findViewById(R.id.textView3);
        fab_shareRec = (FloatingActionButton)findViewById(R.id.fab_shareRec);
        mStartRecording = false; //non sta registrando
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        //if(mFileName.isEmpty()) return;
        //if(this.tvlog == null) Log.e(LOG_TAG, "can't complete log file!");

        btnReg = (Button) findViewById(R.id.btnReg);
        if (mStartRecording) { btnReg.setText("Stop"); }
            else { btnReg.setText("Registra"); }
            btnReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFileName = getCartellaPredefinita().getAbsolutePath() + "rec. " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis()) + ".m4a";
                    onRecord(mStartRecording);
                    mStartRecording = !mStartRecording;
                    if (mStartRecording) { btnReg.setText("Stop"); }
                    else { btnReg.setText("Registra"); }
            }
        });
        btnRip = (Button) findViewById(R.id.btnRip);
        btnRip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                mStartPlaying = !mStartPlaying;
            }
        });

        /*if (mStartPlaying) {
            btnRip.setText("Stop playing");
        } else {
            btnRip.setText("Start playing");
        }*/

        fab_shareRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFileName != null) {
                    File _file = new File(mFileName);
                    Uri uri = Uri.fromFile(_file);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("audio/*");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(share, "Invia registrazione!"));
                }
                else
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Non hai una registrazione!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });

        /*ActionBar ac = this.getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("Registratore");*/
    }


    private void onRecord(boolean start) {
        if (!start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            Log.d(LOG_TAG, "mplayer started!");
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
            mRecorder.start();
            Toast.makeText(getBaseContext(), "Sto registrando...", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed" + e.toString());

        }
    }

    private void stopRecording() {
        if(mRecorder == null) {Toast.makeText(getBaseContext(), "mRecorder è null", Toast.LENGTH_LONG).show();}
        else {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            //Toast.makeText(getBaseContext(), "Registrazione salvata! "+mFileName.toString(), Toast.LENGTH_LONG).show();
            Snackbar.make(getWindow().getDecorView().getRootView(), "Registrazione salvata! "+mFileName.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            tvlogRec.setText(mFileName.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.button_action_bar,menu);
        return true;
    }
}