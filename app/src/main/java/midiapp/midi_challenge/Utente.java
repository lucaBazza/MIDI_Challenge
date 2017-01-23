package midiapp.midi_challenge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luca on 04/01/17.
 */

public class Utente {
    long idUtente;
    String nickName;
    String foto;
    int punteggioMassimo;
    int punteggioMedio;
    String strumento;

    List<Brano> braniUtente;

    public Utente(long idUtente, String nickName, String foto, String strumento, int punteggioMassimo, int punteggioMedio) {
        this.foto = foto;
        this.idUtente = idUtente;
        this.nickName = nickName;
        this.punteggioMassimo = punteggioMassimo;
        this.punteggioMedio = punteggioMedio;
        this.braniUtente = new ArrayList<>();
        this.strumento = strumento;
    }

    public Utente(String nickName, String foto, String strumento, int punteggioMassimo, int punteggioMedio) {
        this.foto = foto;
        this.nickName = nickName;
        this.punteggioMassimo = punteggioMassimo;
        this.punteggioMedio = punteggioMedio;
        this.braniUtente = new ArrayList<>();
        this.strumento = strumento;
    }

    @Override
    public String toString() {
        return nickName.toUpperCase();
    }

    public long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(long idUtente) {
        this.idUtente = idUtente;
    }

    public String getStrumento() {
        return strumento;
    }

    public void setStrumento(String strumento) {
        this.strumento = strumento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPunteggioMedio() {
        return punteggioMedio;
    }

    public void setPunteggioMedio(int punteggioMedio) {
        this.punteggioMedio = punteggioMedio;
    }

    public int getPunteggioMassimo() {
        return punteggioMassimo;
    }

    public void setPunteggioMassimo(int punteggioMassimo) {
        this.punteggioMassimo = punteggioMassimo;
    }

    public List<Brano> getBraniUtente() {
        return braniUtente;
    }

    public void setBraniUtente(List<Brano> braniUtente) {
        this.braniUtente = braniUtente;
    }
}
