package bach;

public class Chord {
	String sequence, bass, chordLabel;
	int eventNo, meter;
	boolean[] allNotes;
	String[] noteNames;
	public Chord(String sequence, String bass, String chordLabel, 
					boolean c, boolean cSharp, boolean d, boolean dSharp, boolean e, boolean f,
					boolean fSharp, boolean g, boolean gSharp, boolean a, boolean aSharp, boolean b,
					int eventNo, int meter) {
		this.bass = bass;
		this.meter = meter;
		this.chordLabel = chordLabel;
		this.sequence = sequence;
		allNotes = new boolean[] {c, cSharp, d, dSharp, e, f, fSharp, g, gSharp, a, aSharp, b};
		noteNames = new String[] {"c", "cSharp", "d", "dSharp", "e", "f", "fSharp", "g", "gSharp", "a", "aSharp", "b"};
	}
	
	public boolean[] getAllNotes() {
		return allNotes;
	}
	
	public boolean getNote(int i) {
		return allNotes[i];
	}
	
	public String getNoteName(int i) {
		return noteNames[i];
	}
	
	public String getChordLabel() {
		return chordLabel;
	}
	
	public String getBass() {
		return bass;
	}
	
	public String getSequence() {
		return bass;
	}
	
	public int getEventNo() {
		return eventNo;
	}
	
	public int getMeter() {
		return meter;
	}
}
