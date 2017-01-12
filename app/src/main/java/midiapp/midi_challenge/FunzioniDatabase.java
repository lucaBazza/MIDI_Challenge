package midiapp.midi_challenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.params.StreamConfigurationMap;

/**
 * Created by Paolo on 12/01/2017.
 */

public class FunzioniDatabase {
    SQLiteDatabase database = null;
    DatabaseApp dbHelper = null;

    /*
        Database Schema:
        Utente(rowid,nickname,foto,punteggioMassimo,punteggioMedio)
        Brano(rowid,titolo,nomeFile,difficoltà)
        relUtenteBrano(idUtente,idBrano)
     */

    public FunzioniDatabase(Context context) {
        dbHelper = new DatabaseApp(context);
        database = dbHelper.getWritableDatabase();
    }

    public long inserisci(Utente u){
        ContentValues cv = new ContentValues();
        cv.put("nickname",u.nickName);
        cv.put("foto",u.foto);
        cv.put("punteggioMassimo",u.punteggioMassimo);
        cv.put("punteggioMedio",u.punteggioMedio);
        long result = database.insert("utente","",cv);

        return result;
    }

    public long inserisci(Brano b){
        ContentValues cv = new ContentValues();
        cv.put("titolo",b.titolo);
        cv.put("nomeFile",b.nomeFile);
        cv.put("difficoltà",b.difficoltà);

        long result = database.insert("brano","",cv);

        return result;
    }


    public Utente trovaUtente(int id){
        String[] colums = {"idUtente","nickname","foto","punteggioMassimo","punteggioMedio"};
        Cursor res = database.query(true,"utente",colums,"idUtente = ?",new String[] {Integer.toString(id)},"","","","");

        if (res.moveToFirst()){
            return new Utente(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3),res.getInt(4));
        }else{
            return  null;
        }
    }

    public Utente trovaUtente(String nome){
        String[] colums = {"idUtente","nickname","foto","punteggioMassimo","punteggioMedio"};
        Cursor res = database.query(true,"utente",colums,"nickname = ?",new String[] {nome},"","","","");

        if (res.moveToFirst()){
            return new Utente(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3),res.getInt(4));
        }else{
            return  null;
        }
    }

    public Brano trovaBrano(int id){
        String[] colums = {"idBrano","titolo","nomeFile","difficoltà"};
        Cursor res = database.query(true,"brano",colums,"idBrano = ?",new String[] {Integer.toString(id)},"","","","");

        if (res.moveToFirst()){
            return new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3));
        }else{
            return  null;
        }
    }

    public Brano trovaBrano(String titolo){
        String[] colums = {"idBrano","titolo","nomeFile","difficoltà"};
        Cursor res = database.query(true,"brano",colums,"titolo = ?",new String[] {titolo},"","","","");

        if (res.moveToFirst()){
            return new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3));
        }else{
            return  null;
        }
    }

}
