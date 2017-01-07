package midiapp.midi_challenge;

import com.pdrogfer.mididroid.MidiFile;
import com.pdrogfer.mididroid.MidiTrack;
import com.pdrogfer.mididroid.event.MidiEvent;
import com.pdrogfer.mididroid.event.NoteOn;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lucabazzanella on 18/12/16.
 * //ogni nota letta aggunge un punteggio da aggiungere a quello totale
 //punteggio = velocita * armonia * timeSignature (
 //velocita = deltaTick^3 //più la velocità è alta più il punteggio aumenta -> Esponenzialmente
 //armonia = sommatoria( nota[]) //quante note sono suonate contemporaneanemnte
 //nota = intervallo (se diatonico) oppure intervallo*2 (se non diatonico)
 //                                                     per fare questo mi avvalgo di un vettore di note precendenti per trovare la tonalità corrente
 //                                                     ATTENZIONE a cambio direzione
 //Time signature = [4/4 , 2/2 , 7/8 ...] ogniuno di questo inserisce un moltiplicatore di difficolta
 */

public class AlgoritmoMidi {
    MidiFile mf;
    private static ArrayList <NoteOn> LN = new ArrayList<NoteOn>();  // la lista sarebbe meglio se dinamica in base al delta time
    static long punteggio = 0;
    long bestCombo =0;
    AlgoritmoMidi(){

    }
    public void loadFile(MidiFile x){
        mf = x;

    }
    public ArrayList<String> calc(){
        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();
        ArrayList<String> outPut = new ArrayList<>();

        while(it.hasNext()) {
            MidiEvent E = it.next();
            if(E.getClass().equals(NoteOn.class))   {
                NoteOn EveNota = (NoteOn)E;
                if(EveNota.getVelocity()!=0) { // per scremare le note "fantasma" con dinamica 0
                    int dinamica = EveNota.getVelocity();   //useless?
                    int nota = EveNota.getNoteValue();
                    long when = EveNota.getTick();
                    long durata = EveNota.getDelta();
                    LN.add(EveNota);
                    analizzaFraseggio();



                    long combo = durata * nota;
                    if(combo>bestCombo) outPut.add("Fraseggio difficile a: " + when/1000+" sec.");
                    punteggio +=  durata * nota;
                }
            }
        }
        punteggio = punteggio /1000;            //miniaturizzazione
        outPut.add(Long.toString(punteggio));
        return outPut;
    }

    String convIntStrNota(int i){
        String[] note = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        String result = note[(i)%12];
        String octave = Integer.toString((i/12)-2);
        return result+octave;
    }
    private int salto(int notaPartenza, int notaArrivo){
        return notaPartenza - notaArrivo;
    }
    private void analizzaFraseggio(){

    }

}
