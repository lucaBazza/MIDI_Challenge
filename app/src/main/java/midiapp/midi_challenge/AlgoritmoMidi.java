package midiapp.midi_challenge;

import android.util.Log;

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
    MidiTrack midiTrack;
    private static ArrayList <NoteOn> ln = new ArrayList<NoteOn>();  // la lista sarebbe meglio se dinamica in base al delta time
    private static int sizeOfLN=20;
    //static int [] tonalita = {1,3,5,6,8,10,11};                      // scala maggiore %12
    static int [] contNoteMod12 = new int[11];
    static long punteggio = 0;
    int contatorenNoteTotale=0;
    int contatoreAppoggiature =0;
    int contatoreAccordi =0;
    long bestPuntTemp = 0;

    AlgoritmoMidi(MidiFile x){
        midiTrack = x.getTracks().get(0);
    } //seleziona la traccia 0 del file midi, è quella contentente la traccia da analizzare

    public ArrayList<String> calc(){
        Iterator<MidiEvent> it = midiTrack.getEvents().iterator();
        ArrayList<String> outPut = new ArrayList<>();

        long puntTemp = 0;

        while(it.hasNext()) {   //per ogni nota nella traccia
            MidiEvent E = it.next();
            if(E.getClass().equals(NoteOn.class) &&   ((NoteOn)E).getVelocity()!=0 ) {  // per scremare le note "fantasma" con dinamica 0
                NoteOn EveNota = (NoteOn)E;                                             //int dinamica = EveNota.getVelocity();   //useless?
                int nota = EveNota.getNoteValue();
                long when = EveNota.getTick();
                long durata = EveNota.getDelta();

                if(ln.size()>sizeOfLN) ln.clear();      //ogni X note viene resettato il contatore, deve (in più) cancellare dimanicaente in base a nota.tick
                ln.add(EveNota);                        //aggiungo la nota nel vettore temporaneo
                contatorenNoteTotale++;
                contNoteMod12[EveNota.getNoteValue()%11]++;

                puntTemp +=  punteggioVelocita() * punteggioMelArm();
                if(puntTemp>bestPuntTemp)  {
                    bestPuntTemp = puntTemp;
                    outPut.add("Fraseggio difficile a: " + when/1000+" sec.");
                }
                punteggio +=  punteggioVelocita() * punteggioMelArm();
                puntTemp = 0;
            }
        }


        punteggio /= 1000*1000;            //miniaturizzazione

        outPut.add("Punteggio totale realizzato: \t"+ Long.toString(punteggio));
        Log.println(Log.ASSERT,"Algoritmo midi","numero di note in totale: "+contatorenNoteTotale);
        Log.println(Log.ASSERT,"Algoritmo midi","numero di appoggiature in totale: "+contatoreAppoggiature);
        Log.println(Log.ASSERT,"Algoritmo midi","numero di accordi in totale: "+contatoreAccordi);
        Log.println(Log.ASSERT,"Algoritmo midi","Punteggio totale realizzato: 	"+ Long.toString(punteggio));
        return outPut;
    }

    String convIntStrNota(int i){
        String[] note = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        String result = note[(i)%12];
        String octave = Integer.toString((i/12)-2);
        return result+octave;
    }

    /*private void aggiornaTonalita(){    //confronta l'ultima nota inserita con il vettore ton e dopo il vettore last, e decide se una nota è diatonica, non, o se è avvenuto un cambio ton
        NoteOn lastNota = ln.get(ln.size());
        boolean diatonica = true;
        for(int i=0;i<7;i++)   {} //controllo se diatonica
            //if(lastNota.getNoteValue()%12 == tonalita[i]) diatonica = false;

        if(!diatonica)          //Se NON è diatonica procedo con un controllo per vedere se è necessario un cambio tonalità
            for(int i=ln.size()-1; i>0; i--){ //for dall'ultimo al primo
                if(lastNota.getNoteValue()%12 == ln.get(i).getNoteValue()%12) { //se è la stessa nota vuol dire che la nota diatonica è stata ripetuta, quindi la ton va aggiornata
                    Log.println(Log.ASSERT,"Evento refreshTonalita","Cambio tonalità a: "+lastNota.getDelta()/1000);
                }
            }
    }
    */

    private double calcolaBiasTonalità(){
        double punteggio = 0.0;
        return punteggio;
    }

    /**
     *  Per calcolare la velocità viene dedotto da noteOn.tick rispetto alla precendente:
     *  -> in primis viene confrontanta la velocità con dei valori static
     *
     * @return punteggio di velocità di esecuzione della nota proecessata, ne restituisce un valore compreso fra { 0.01 e 1.00 }
     */
    private double punteggioVelocita(){
        double punteggio = 0.00;
        NoteOn nota = ln.get(ln.size()-1);
        punteggio = 1 / nota.getDelta();
        return punteggio;
    }
    /**
     *  Questo metodo analizza l'ultima nota processata, la confronta con il vettore ln -> note recenti:
     *      -> assegna un punto per ogni semitono che c'è di differenza fra la nota precendente e quella successiva
     *      ->se la nota viene suonata contemporaneamente -o quasi- a quella precendente (o precedenti) viene dato un bonus di 0.2x per ogni voce aggiunta
     * @return punteggio melodico + armonico della nota processata
     */
    private int punteggioMelArm(){
        int points =0;
        int salto = 0;
        float moltAcco =1;
        int vociAccordo = 0;

        NoteOn notaCorrente =ln.get(ln.size()-1);
        if(ln.size()>1) { //se il vettore non contiene  più di una nota
            NoteOn notaPrec = ln.get(ln.size() - 2);
            if(notaCorrente.getNoteValue() > notaPrec.getNoteValue())
                salto += (notaCorrente.getNoteValue() - notaPrec.getNoteValue())%11;    //il salto, sia ascendente che discendente
            else                                                                        // può dare max 11 punti (poi si va nell'ottava successiva)
                salto += (notaPrec.getNoteValue() - notaCorrente.getNoteValue())%11;
            Iterator<NoteOn> it = ln.iterator();
            while (it.hasNext()){
                if ((notaCorrente.getTick() - it.next().getTick()) < 1) {  //se c'è meno di 5ms fra la nota corrente e la precendente c'è un possibile accordo/appoggio
                    moltAcco += 0.1;
                    contatoreAppoggiature++;
                    vociAccordo++;
                    if(vociAccordo>=3) { moltAcco += 0.1; break;}
                }
                else vociAccordo=0;
            }
        }
        if(vociAccordo>=3) {   //aggiorno contatore accordi e lo notifico nel log e/o output
            contatoreAccordi++;
            //Log.println(Log.ASSERT,"Algoritmo midi","Trovato un accordo a: "+notaCorrente.getTick());
        }
        points =  Float.floatToIntBits(salto*moltAcco);
        return points;
    }

}
