package midiapp.midi_challenge;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by luca on 04/01/17.
 */

public class Brano {
    long idBrano;
    File fileBrano;
    int difficoltà;
    int autovalutazione;
    String autore;
    ArrayList<String> arraySpartiti;

    public Brano(long idBrano, String nomeFile, int difficoltà,int autovalutazione, String author,ArrayList<String> arraySheets) {
        this.idBrano = idBrano;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        this.autovalutazione = autovalutazione;
        this.autore = author;
        this.arraySpartiti = arraySheets;
    }

    public Brano(long idBrano, String nomeFile, int difficoltà) {
        this.idBrano = idBrano;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        this.autovalutazione = -1;
    }

    public Brano(String nomeFile, int difficoltà,int autovalutazione) {
        this.idBrano = -1;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        this.autovalutazione = autovalutazione;
    }


    public Brano(String nomeFile, int difficoltà) {
        this.idBrano = -1;
        this.fileBrano = new File(nomeFile);
        this.difficoltà = difficoltà;
        this.autovalutazione = -1;
    }

    public Brano(String nomeFile, File path, int difficoltà) {
        this.idBrano = -1;
        this.fileBrano = path;
        this.difficoltà = difficoltà;
        this.autovalutazione = -1;
    }

    @Override
    public String toString() {
        return this.fileBrano.getName();
    }

    @Override
    public boolean equals(Object obj) {
        Brano b = (Brano) obj;
        return this.fileBrano.equals(b) || this.fileBrano.getName() == b.fileBrano.getName();
    }

    public int getAutovalutazione() {
        return autovalutazione;
    }

    public void setAutovalutazione(int autovalutazione) {
        this.autovalutazione = autovalutazione;
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
        return this.fileBrano.getName();
    }
}
