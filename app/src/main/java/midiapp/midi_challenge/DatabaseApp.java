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

    private static String CREATE_USERS = "CREATE TABLE utente (idUtente INTEGER NOT NULL PRIMARY KEY, nickname VARCHAR NOT NULL, foto VARCHAR, punteggioMassimo INTEGER DEFAULT -1, punteggioMedio INTEGER DEFAULT -1);";

    private static String CREATE_BRANI = "CREATE TABLE brano (idBrano INTEGER NOT NULL PRIMARY KEY, titolo VARCHAR NOT NULL, nomeFile VARCHAR, difficoltà INTEGER DEFAULT -1 NOT NULL);";

    private static String CREATE_REL_UTENTE_BRANO = "CREATE TABLE relUtenteBrano (idUtente int(32), idBrano int(32), FOREIGN KEY (idBrano) REFERENCES brano(idBrano), FOREIGN KEY (idUtente) REFERENCES  utente(idUtente));";

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

    public void addUtente(Utente u){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues c = new ContentValues();
        c.put("idUtente",u.getIdUtente());
        c.put("nickname",u.getNickName());
        c.put("foto",u.getFoto());
        c.put("punteggioMassimo",u.getPunteggioMassimo());
        c.put("punteggioMedio",u.getPunteggioMedio());

        try{
            db.insert("utente",null,c);
            //Log.println(Log.ASSERT,"sql","Insert andata a buon fine. IDUtente: " + u.getIdUtente());
            MainActivity.tv.setText("Insert andata a buon fine. IDUtente: " + u.getIdUtente());
        }catch (SQLException e){
            Log.println(Log.ERROR,"sql",e.toString());
        }finally {
            db.close();
        }
    }

    public String selectUtenteById(int id){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String [] qrColoumn ={ "idUtente, nickname, foto, punteggioMassimo, punteggioMedio"};
            cursor = db.query("utente", qrColoumn, "idUtente = ?", new String[] { String.valueOf(id) }, null, null, null);
            Log.println(Log.ASSERT,"sql","Lettura andata a buon fine.");

        }
        catch(SQLException e){ Log.println(Log.ERROR,"sql",e.toString());            MainActivity.tv.append("\n errore select: "+e.toString());        }

        try{
            if(cursor.getCount()==0){ MainActivity.tv.append("\n cursor vuoto!"); return "";}
            if(cursor != null)
                cursor.moveToFirst();

            MainActivity.tv.append("\n\t Select andata a buon fine. IDUtente: " + cursor.getString(1));
            //return cursor.getString(0);
            return "";
        }
        catch(SQLException e){ MainActivity.tv.append("\n errore select: "+e.toString()); return "";  }
        //Utente tmp = new Utente(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));

    }
}
