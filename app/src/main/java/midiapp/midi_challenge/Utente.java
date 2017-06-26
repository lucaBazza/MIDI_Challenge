package midiapp.midi_challenge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luca on 04/01/17.
 */

public class Utente {
    long idUtente = -1;
    String nickName = "";
    String foto = "";
    int punteggioMassimo = 0;
    int punteggioMedio = 0;
    String strumento = "";

    List<Brano> braniUtente;

    //'dummy' constructor used when creating a new user for the first time.
    public Utente(String nickName) {
        this.nickName = nickName;
        this.foto = "";
        this.punteggioMassimo = 0;
        this.punteggioMedio = 0;
        this.strumento = "";
    }

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
        this.setFoto(foto);
        this.nickName = nickName;
        this.punteggioMassimo = punteggioMassimo;
        this.punteggioMedio = punteggioMedio;
        this.braniUtente = new ArrayList<>();
        this.setStrumento(strumento);
    }

    public void aggiornaPunteggioMedio(int punteggio){
        this.punteggioMedio = (punteggioMedio + punteggio) / 2;
    }

    public void aggiornaPunteggioMedio(List<Integer> punteggio){
        int sum = 0;
        for(int p : punteggio){
            sum += p;
        }
        int media = sum / punteggio.size();
        this.punteggioMedio = (this.punteggioMedio + media) / 2;
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
        if(this.strumento==null)
            return "defaultInstrument"; //TODO: dammit, again. Find a better way to indicate that someone hasn't selected an instument at all
        else
            return strumento;
    }

    public void setStrumento(String strumento) {

        if(strumento!=null)
            this.strumento=strumento;
        else
            this.strumento = "defaultInstrument";
    }

    public String getFoto() {
        if(this.foto == null)
            return "/defaultImage.png"; //TODO:DAMMIT
        else
            return foto;
    }

    public void setFoto(String foto) {
        if(foto != null)
            this.foto = foto;
        else
            this.foto = "/defaultImage.png";
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
