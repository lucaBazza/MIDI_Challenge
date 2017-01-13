package midiapp.midi_challenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.params.StreamConfigurationMap;

import java.util.ArrayList;
import java.util.List;

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
        long newRowId = database.insert("utente","",cv);

        return newRowId;
    }

    public long inserisci(Brano b){
        ContentValues cv = new ContentValues();
        cv.put("titolo",b.titolo);
        cv.put("nomeFile",b.nomeFile);
        cv.put("difficoltà",b.difficoltà);

        long result = database.insert("brano","",cv);

        return result;
    }


    public Utente trovaUtente(long id){
        String[] colums = {"idUtente","nickname","foto","punteggioMassimo","punteggioMedio"};
        Cursor res = database.query(true,"utente",colums,"idUtente = ?",new String[] {Long.toString(id)},"","","","");

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

    public List<Brano> braniUtente(long idUtente){
        List<Brano> tmpList = new ArrayList<>();
        String selectionQuery = "SELECT idBrano,titolo,nomeFile,difficoltà, FROM Brano JOIN relUtenteBrano WHERE idUtente = ?";

        Cursor res = database.rawQuery(selectionQuery,new String[]{Long.toString(idUtente)});
        while(res.moveToNext()){
            tmpList.add(new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3)));
        }

        return tmpList;
    }

    public List<Brano> braniUtente(String nickName){
        List<Brano> tmpList = new ArrayList<>();
        String selectionQuery = "SELECT idBrano,titolo,nomeFile,difficoltà, FROM Brano JOIN relUtenteBrano JOIN Utente WHERE nickname = ?";

        Cursor res = database.rawQuery(selectionQuery,new String[]{nickName});
        while(res.moveToNext()){
            tmpList.add(new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3)));
        }

        return tmpList;
    }

    public long inserisciBranoPerUtente(Utente u, Brano b){
        ContentValues cv = new ContentValues();
        cv.put("idUtente",u.idUtente);
        cv.put("idBrano",b.idBrano);

        return database.insert("relUtenteBrano","",cv);
    }
}
