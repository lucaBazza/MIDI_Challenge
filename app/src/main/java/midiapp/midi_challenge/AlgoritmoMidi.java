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
 *
 * “La musica è una pratica occulta dell’aritmetica, nella quale l’anima non sa di calcolare” (Gottfried Wilhelm von Leibniz)
 *
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
    private static ArrayList<String> outPut;
    static int [] tonalita = {1,3,5,6,8,10,11};                      // scala maggiore %12
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
        outPut = new ArrayList<>();

        while(it.hasNext()) {   //per ogni nota
            MidiEvent E = it.next();
            if(E.getClass().equals(NoteOn.class))   {
                NoteOn EveNota = (NoteOn)E;
                if(EveNota.getVelocity()!=0) { // per scremare le note "fantasma" con dinamica 0
                    int dinamica = EveNota.getVelocity();   //useless?
                    int nota = EveNota.getNoteValue();
                    long when = EveNota.getTick();
                    long durata = EveNota.getDelta();
                    LN.add(EveNota);
                    //aggiornaTonalita();



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
    private void aggiornaTonalita(){    //confronta l'ultima nota inserita con il vettore ton e dopo il vettore last, e decide se una nota è diatonica, non, o se è avvenuto un cambio ton
        NoteOn lastNota = LN.get(LN.size());
        boolean diatonica = true;

        for(int i=0;i<7;i++)    //controllo se diatonica
            if(lastNota.getNoteValue()%12 == tonalita[i]) diatonica = false;

        if(!diatonica)          //Se NON è diatonica procedo con un controllo per vedere se è necessario un cambio tonalità
            for(int i=LN.size()-1; i>0; i--){ //for dall'ultimo al primo
                if(lastNota.getNoteValue()%12 == LN.get(i).getNoteValue()%12) { //se è la stessa nota vuol dire che la nota diatonica è stata ripetuta, quindi la ton va aggiornata
                    outPut.add("Cambio tonalità a: "+lastNota.getDelta()/1000);
                }
            }
    }

}
