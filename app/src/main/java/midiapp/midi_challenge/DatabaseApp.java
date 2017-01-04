package midiapp.midi_challenge;

import android.content.ContentValues;
import android.content.Context;
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

    private static String CREATE_USERS = "CREATE TABLE utenti (idUtente INTEGER NOT NULL PRIMARY KEY, nickname VARCHAR NOT NULL, foto VARCHAR, punteggioMassimo INTEGER DEFAULT -1, punteggioMedio INTEGER DEFAULT -1);";

    private static String CREATE_BRANI = "CREATE TABLE brani (idBrano INTEGER NOT NULL PRIMARY KEY, titolo VARCHAR NOT NULL, nomeFile VARCHAR, difficoltà INTEGER DEFAULT -1 NOT NULL);";

    private static String CREATE_REL_UTENTE_BRANO = "CREATE TABLE relUtenteBrani (idUtente INTEGER FOREIGN KEY REFERENCES (utenti.idUtente) , idBrano INTEGER FOREIGN KEY REFERENCES (brani.idBrano));";

    public DatabaseApp(Context context){
        super(context, NOME_DB,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS);
        sqLiteDatabase.execSQL(CREATE_BRANI);
        sqLiteDatabase.execSQL(CREATE_REL_UTENTE_BRANO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE utenti, brani, relUtentiBrani;");
        onCreate(sqLiteDatabase);
    }

    public void addUtente(Utente u){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues c = new ContentValues();
        c.put("idUtente",u.getIdUtente());
        c.put("nickname",u.getNickName());
        c.put("foto",u.getFoto());
        c.put("punteggioMassimo",u.getPunteggioMassimo());
        c.put("punteggioMedio",u.getPunteggioMedio());

        try{
            db.insert("utenti",null,c);
        }catch (SQLException e){
            Log.println(Log.ERROR,"sql",e.toString());
        }finally {
            db.close();
        }
    }
}
