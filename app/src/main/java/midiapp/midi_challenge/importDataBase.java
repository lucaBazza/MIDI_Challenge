package midiapp.midi_challenge;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.io.*;

/**
 * Created by lucabazzanella on 02/06/17.
 */

public class importDataBase extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "Dragonfly.db";
    public  static String DATABASE_PATH = "/data/data/com.kan.linnaeus/databases/";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase dataBase;
    private final Context dbContext;

    public importDataBase(Context context, File backup, File db) {  // backup -> da importare   db -> da sostituire
        //super(context, DBActivity.DatabaseName, null, DATABASE_VERSION);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.dbContext = context;
        //DATABASE_NAME = dbName;
        //DATABASE_PATH = dbPath;
        //DATABASE_NAME = DBActivity.DatabaseName;
        // checking database and open it if exists
        if (checkDataBase()) {
            openDataBase();
        } else
        {
            try {
                this.getReadableDatabase();
                copyDataBase();
                this.close();
                openDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
            Toast.makeText(context, "Initial database is created", Toast.LENGTH_LONG).show();
        }
    }

    private void copyDataBase() throws IOException{
        InputStream myInput = dbContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String dbPath = DATABASE_PATH + DATABASE_NAME;
        dataBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        boolean exist = false;
        try {
            String dbPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.d("db log", "database does't exist");
        }

        if (checkDB != null) {
            exist = true;
            checkDB.close();
        }
        return exist;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}