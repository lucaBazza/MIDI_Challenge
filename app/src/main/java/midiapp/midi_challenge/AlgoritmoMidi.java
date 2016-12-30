package midiapp.midi_challenge;

import com.pdrogfer.mididroid.MidiFile;
import com.pdrogfer.mididroid.MidiTrack;
import com.pdrogfer.mididroid.event.MidiEvent;
import com.pdrogfer.mididroid.event.NoteOn;

import java.util.Iterator;

/**
 * Created by lucabazzanella on 30/12/16.
 */

public class AlgoritmoMidi {
    MidiFile mf;
    AlgoritmoMidi(){

    }

    public void loadFile(MidiFile x){
        mf = x;

    }
    public void calc(){
        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();
        MidiEvent[] ame = new MidiEvent[10];
        int difficolta=0;

        while(it.hasNext())
        {
            MidiEvent E = it.next();
            if(E.getClass().equals(NoteOn.class))
            {
                int i = ((NoteOn)E).getNoteValue();
                if(((NoteOn) E).getVelocity()!=0) { //non è bellissimo, solo perchè ci sono note "fantasma"
                    String vel =Integer.toString(((NoteOn) E).getVelocity());
                    //ogni nota letta aggunge un punteggio da aggiungere a quello totale
                    //punteggio = velocita * armonia * timeSignature (
                    //velocita = deltaTick^3 //più la velocità è alta più il punteggio aumenta -> Esponenzialmente
                    //armonia = sommatoria( nota[]) //quante note sono suonate contemporaneanemnte
                    //nota = intervallo (se diatonico) oppure intervallo*2 (se non diatonico) //per fare questo mi avvalgo di un vettore di note precendenti per trovare la tonalità corrente
                    //Time signature = [4/4 , 2/2 , 7/8 ...] ogniuno di questo inserisce un moltiplicatore di difficolta
                }
            }
        }

    }

    String convIntStrNota(int i){
        String[] note = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        String result = note[(i)%12];
        String octave = Integer.toString((i/12)-2);
        return result+octave;
    }

}
