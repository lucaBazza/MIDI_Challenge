package midiapp.midi_challenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paolo on 12/01/2017.
 */

public class FunzioniDatabase {
    SQLiteDatabase database = null;
    DatabaseApp dbHelper = null;

    private Context internalContextDatabase = null;

    /*
        Database Schema:
        Utente(rowid,nickname,foto,punteggioMassimo,punteggioMedio)
        Brano(rowid,titolo,nomeFile,difficoltà)
        relUtenteBrano(idUtente,idBrano)
     */

    public FunzioniDatabase(Context context) {
        dbHelper = new DatabaseApp(context);
        database = dbHelper.getWritableDatabase();
        internalContextDatabase = context;
    }

    //resetta il db e lo ricrea. Usare con cautela.
    public void dropAllTables(){
        database.execSQL("DROP TABLE IF EXISTS utente");
        database.execSQL("DROP TABLE IF EXISTS brano");
        database.execSQL("DROP TABLE IF EXISTS relUtenteBrano");
    }

    public long inserisci(Utente u){
        ContentValues cv = new ContentValues();
        cv.put("nickname",u.nickName);
        cv.put("foto",u.foto);
        cv.put("strumento",u.strumento);
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
        String[] colums = {"idUtente","nickname","foto","strumento","punteggioMassimo","punteggioMedio"};
        Cursor res = database.query(true,"utente",colums,"idUtente = ?",new String[] {Long.toString(id)},"","","","");

        if (res.moveToFirst()){
            return new Utente(res.getInt(0),res.getString(1),res.getString(2),res.getString(3),res.getInt(4),res.getInt(5));
        }else{
            return  new Utente(-1,"","","",0,0);
        }
    }

    public Utente trovaUtente(String nome){
        String[] colums = {"idUtente","nickname","foto","strumento","punteggioMassimo","punteggioMedio"};
        Cursor res = database.query(true,"utente",colums,"nickname = ?",new String[] {nome},"","","","");

        if (res.moveToFirst()){
            return new Utente(res.getInt(0),res.getString(1),res.getString(2),res.getString(3),res.getInt(4),res.getInt(5));
        }else{
            return  new Utente(-1,"","","",0,0);
        }
    }

    public int aggiornaUtente(Utente u){
        ContentValues cv = new ContentValues();
        cv.put("nickname",u.nickName); //These Fields should be your String values of actual column names
        cv.put("foto",u.foto);
        cv.put("strumento",u.strumento);
        cv.put("punteggioMassimo",u.punteggioMassimo);
        cv.put("punteggioMedio", u.punteggioMedio);
        Log.d("UPDATE DB",cv.toString());
        return database.update("utente", cv, "idUtente="+u.idUtente, null);
    }

    public Brano trovaBrano(long id){
        String[] colums = {"idBrano","titolo","nomeFile","difficoltà"};
        Cursor res = database.query(true,"brano",colums,"idBrano = ?",new String[] {Long.toString(id)},"","","","");

        if (res.moveToFirst()){
            return new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3));
        }else{
            return  new Brano(-1,"","",0);
        }
    }

    public Brano trovaBrano(String titolo){
        String[] colums = {"idBrano","titolo","nomeFile","difficoltà"};
        Cursor res = database.query(true,"brano",colums,"titolo = ?",new String[] {titolo},"","","","");

        if (res.moveToFirst()){
            return new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3));
        }else{
            return  new Brano(-1,"","",0);
        }
    }

    public List<Brano> braniUtente(long idUtente){
        List<Brano> tmpList = new ArrayList<>();
        String selectionQuery = "SELECT Brano.idBrano,titolo,nomeFile,difficoltà,autovalutazione FROM Brano JOIN relUtenteBrano WHERE idUtente = ?";

        Cursor res = database.rawQuery(selectionQuery,new String[]{Long.toString(idUtente)});
        while(res.moveToNext()){
            tmpList.add(new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3),res.getInt(4)));
        }

        return tmpList;
    }

    public List<Brano> braniUtente(String nickName){
        List<Brano> tmpList = new ArrayList<>();
        String selectionQuery = "SELECT Brano.idBrano,titolo,nomeFile,difficoltà,autovalutazione FROM Brano JOIN relUtenteBrano JOIN Utente WHERE nickname = ?";

        Cursor res = database.rawQuery(selectionQuery,new String[]{nickName});
        while(res.moveToNext()){
            tmpList.add(new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3),res.getInt(4)));
        }

        return tmpList;
    }

    public long inserisciBranoPerUtente(Utente u, Brano b, int autovalutazione){
        ContentValues cv = new ContentValues();
        cv.put("idUtente",u.idUtente);
        cv.put("idBrano",b.idBrano);
        cv.put("autovalutazione",autovalutazione);

        return database.insert("relUtenteBrano","",cv);
    }

    public List<Utente> prendiTuttiUtenti() {
        List<Utente> tmpList = new ArrayList<>();

        String[] colums = {"idUtente","nickname","foto","strumento","punteggioMassimo","punteggioMedio"};
        Cursor res = database.query(true,"utente",colums,"",new String[] {},"","","","");

        while(res.moveToNext()){
            tmpList.add(new Utente(res.getInt(0),res.getString(1),res.getString(2),res.getString(3),res.getInt(4),res.getInt(5)));
        }

        return tmpList;
    }

    public List<Brano> prendiTuttiBrani(){
        List<Brano> tmpList = new ArrayList<>();

        String[] columns = {"idBrano","titolo","nomeFile","difficoltà"};
        Cursor res = database.query(true,"brano",columns,"",new String[] {},"","","","");

        while(res.moveToNext()){
            tmpList.add(new Brano(res.getInt(0),res.getString(1),res.getString(2),res.getInt(3)));
        }
        return tmpList;
    }

    public long cancLinkBranoUtente(long idUtente, long idBrano){ //http://stackoverflow.com/questions/15027474/android-sqlite-deleting-a-specific-row-from-database
        String whereClause = "idUtente='"+idUtente +"' AND idBrano='" + idBrano+"'";
        return database.delete("relUtenteBrano",whereClause, null);
    }

    public long cancLinksTuttiBraniUtente(long idUtente){
        String whereClause = "idUtente='" + idUtente+"'";
        return database.delete("relUtenteBrano",whereClause, null);
    }
}