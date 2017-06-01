package midiapp.midi_challenge.accordatoreClasses;

public class CircularBuffer {
	private short [] array;
	private int head;
	private int size;
	private int availableElements;
	
	public CircularBuffer(int s) {
		size = s;
		array = new short[size];
		head = 0;
		availableElements = 0;
	}
	
	public int getSize() {
		return size;
	}
	
	public synchronized void push(short x) {
		array[head++] = x;
		if(head>=size) head-=size;
		availableElements = Math.min(availableElements+1, size);
	}
	
	public synchronized int getElements(double [] result, int offset, int maxElements) {
		int toRead = Math.min(maxElements, availableElements);
		int current = head - 1;
		for(int i=offset+toRead-1; i>=offset; --i) {
			if(current < 0) current+=size;
			result[i]=array[current--];
		}
		return toRead;
	}

	public static class FrequencySmoothener {
        static final double frequencyForgetting = 0.9; // how fast forget frequency
        static final int invalidDataAllowed = 6; // if this many uncertain data sets, assume frequency
                                           // not available.
        static private double smoothFrequency=0.0;
        static private int invalidDataCounter;

        public static double getSmoothFrequency(AnalyzedSound result) {
            if(!result.frequencyAvailable) {
                invalidDataCounter=Math.min(invalidDataCounter+1, 2*invalidDataAllowed);
            } else {
                if(smoothFrequency == 0.0) {
                    smoothFrequency = result.frequency;
                } else {
                    smoothFrequency = (1-frequencyForgetting)*smoothFrequency +
                                       frequencyForgetting*result.frequency;
                }
                invalidDataCounter=Math.max(invalidDataCounter-invalidDataAllowed, 0);
            }
            if(invalidDataCounter <= invalidDataAllowed) {
                return smoothFrequency;
            } else {
                smoothFrequency = 0.0;
                return Double.NaN;
            }
        }
    }
}
