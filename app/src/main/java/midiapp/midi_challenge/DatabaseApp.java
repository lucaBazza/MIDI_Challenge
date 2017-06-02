package midiapp.midi_challenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.StringBuilderPrinter;

/**
 * Created by luca on 04/01/17.
 */

public class DatabaseApp extends SQLiteOpenHelper {


    private static String NOME_DB = "databaseApp";

    private static String CREATE_USERS = "CREATE TABLE utente (idUtente INTEGER PRIMARY KEY, nickname VARCHAR NOT NULL,foto VARCHAR, strumento VARCHAR ,punteggioMassimo INTEGER DEFAULT -1, punteggioMedio INTEGER DEFAULT -1);";

    private static String CREATE_BRANI = "CREATE TABLE brano (idBrano INTEGER PRIMARY KEY, titolo VARCHAR NOT NULL,autore VARCHAR, nomeFile VARCHAR, arraySpartiti VARCHAR, difficoltà INTEGER DEFAULT -1 NOT NULL);";

    private static String CREATE_REL_UTENTE_BRANO = "CREATE TABLE relUtenteBrano (idUtente int(32), idBrano int(32), autovalutazione int(32),FOREIGN KEY (idBrano) REFERENCES brano(idBrano), FOREIGN KEY (idUtente) REFERENCES  utente(idUtente));";

    public DatabaseApp(Context context){
        super(context, NOME_DB,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //DROP TABLE usate per il test del database. Eliminare una volta stabile
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS utente");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS brano");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS relUtenteBrano");

        sqLiteDatabase.execSQL(CREATE_USERS);
        sqLiteDatabase.execSQL(CREATE_BRANI);
        sqLiteDatabase.execSQL(CREATE_REL_UTENTE_BRANO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE utente, brano, relUtentiBrano;");
        onCreate(sqLiteDatabase);
    }

    public String getNOME_DB(){ return getDatabaseName();}
}
