package midiapp.midi_challenge;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by luca on 04/01/17.
 */

public class Brano {
    long idBrano;
    File fileBrano;
    String titolo;
    int difficoltà;
    int autovalutazione;
    String autore;
    ArrayList<String> arraySpartiti;

    //idBrano INTEGER PRIMARY KEY, titolo VARCHAR NOT NULL,autore VARCHAR, nomeFile VARCHAR, arraySpartiti VARCHAR, difficoltà INTEGER
    /*public Brano(long idBrano, String nomeFile, int difficoltà,int autovalutazione, String author,ArrayList<String> arraySheets) { //Attenzione l'ordine argomenti è diverso da quello usato in sql!
        this.idBrano = idBrano;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        this.autovalutazione = autovalutazione;
        this.autore = author;
        this.arraySpartiti = arraySheets;
    }*/
    public Brano(long idBrano,String titolo,String autore, String nomeFile,ArrayList<String> arraySheets, int difficoltà) {
        this.idBrano = idBrano;
        this.titolo = titolo;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        this.autore = autore;
        if(arraySheets.contains("") && arraySheets.size() == 1)
            this.arraySpartiti = new ArrayList<>();
        else
            this.arraySpartiti = arraySheets;
    }

    //costruttore 'parziale'. ASSUME che il nome del file sia anche il titolo del brano
    public Brano(long idBrano, String nomeFile, int difficoltà) {
        this.idBrano = idBrano;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        this.autovalutazione = -1;
        this.arraySpartiti = new ArrayList<>();
        this.titolo = new File(nomeFile).getName();
    }

    /*public Brano(String nomeFile, int difficoltà,int autovalutazione) {
        this.idBrano = -1;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        //this.autovalutazione = autovalutazione;
    }*/


    public Brano(String nomeFile, int difficoltà) {
        this.idBrano = -1;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        this.autovalutazione = -1;
        this.arraySpartiti = new ArrayList<>();
    }

    public Brano(String titolo, File path, int difficoltà) {
        this.idBrano = -1;
        this.titolo = titolo;
        this.fileBrano = path;
        this.difficoltà = difficoltà;
        this.autovalutazione = -1;
        this.arraySpartiti = new ArrayList<>();
    }

    @Override
    public String toString() {
        if(titolo!=null || titolo!="")
            return titolo;
        else
            return fileBrano.getName();
        //return  getTitolo();
    }

    @Override
    public boolean equals(Object obj) {
        Brano b = (Brano) obj;
        return this.fileBrano.equals(b) || this.fileBrano.getName() == b.fileBrano.getName();
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getDifficoltà() {
        if(difficoltà>0)
            return difficoltà;
        else return -1;
    }

    public void setDifficoltà(int difficoltà) {
        this.difficoltà = difficoltà;
    }

    public long getIdBrano() {
        return idBrano;
    }

    public void setIdBrano(long idBrano) {
        this.idBrano = idBrano;
    }

    public String getNomeFile() {
        return this.fileBrano.getAbsolutePath();
    }

    public void setNomeFile(String nomeFile) {
        this.fileBrano = new File(nomeFile);
    }

    public String getTitolo() {
        return this.titolo;
    }

    public int getAutovalutazione() {
        return autovalutazione;
    }

    public void setAutovalutazione(int autovalutazione) {
        this.autovalutazione = autovalutazione;
    }

    public String getAutore(){
        return autore;
    }

    public void setAutore(String _autore){
        this.autore = _autore;
    }
}
