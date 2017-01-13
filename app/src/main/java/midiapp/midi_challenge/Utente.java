package midiapp.midi_challenge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luca on 04/01/17.
 */

public class Utente {
    int idUtente;
    String nickName;
    String foto;
    int punteggioMassimo;
    int punteggioMedio;

    List<Brano> braniUtente;

    public Utente(int idUtente, String nickName, String foto, int punteggioMassimo, int punteggioMedio) {
        this.foto = foto;
        this.idUtente = idUtente;
        this.nickName = nickName;
        this.punteggioMassimo = punteggioMassimo;
        this.punteggioMedio = punteggioMedio;
        this.braniUtente = new ArrayList<>();
    }

    public Utente(String nickName, String foto, int punteggioMassimo, int punteggioMedio) {
        this.foto = foto;
        this.nickName = nickName;
        this.punteggioMassimo = punteggioMassimo;
        this.punteggioMedio = punteggioMedio;
        this.braniUtente = new ArrayList<>();
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
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
