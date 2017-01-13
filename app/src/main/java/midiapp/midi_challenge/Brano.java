package midiapp.midi_challenge;

/**
 * Created by luca on 04/01/17.
 */

public class Brano {
    long idBrano;
    String titolo;
    String nomeFile;
    int difficoltà;

    public Brano(long idBrano, String titolo, String nomeFile, int difficoltà) {
        this.idBrano = idBrano;
        this.titolo = titolo;
        this.nomeFile = nomeFile;
        this.difficoltà = difficoltà;
    }


    public Brano(String titolo, String nomeFile, int difficoltà) {
        this.idBrano = -1;
        this.titolo = titolo;
        this.nomeFile = nomeFile;
        this.difficoltà = difficoltà;
    }

    public int getDifficoltà() {
        return difficoltà;
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
        return nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
}
