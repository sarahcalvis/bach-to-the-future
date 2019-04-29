package bach;

public class Chord {
	String sequence, bass, chordLabel;
	boolean c, cSharp, d, dSharp, e, f, fSharp, g, gSharp, a, aSharp, b;
	int eventNo, meter;
	boolean[] allNotes;
	public Chord(String sequence, String bass, String chordLabel, 
					boolean c, boolean cSharp, boolean d, boolean dSharp, boolean e, boolean f,
					boolean fSharp, boolean g, boolean gSharp, boolean a, boolean aSharp, boolean b,
					int eventNo, int meter) {
		this.sequence = sequence;
		this.bass = bass;
		this.chordLabel = chordLabel;
		this.c = c;
		this.cSharp = cSharp;
		this.d = d;
		this.dSharp = dSharp;
		this.e = e;
		this.f = f;
		this.fSharp = fSharp;
		this.g = g;
		this.gSharp = gSharp;
		this.a = a;
		this.aSharp = aSharp;
		this.b = b;
		this.eventNo = eventNo;
		this.meter = meter;
		allNotes = new boolean[] {c, cSharp, d, dSharp, e, f, fSharp, g, gSharp, a, aSharp, b};
	}
	
	public String getChordLabel() {
		return chordLabel;
	}
	
	public boolean getC() {
		return c;
	}
	
	public boolean getCSharp() {
		return cSharp;
	}
	
	public boolean getD() {
		return d;
	}
	
	public boolean getDSharp() {
		return dSharp;
	}
	
	public boolean getE() {
		return e;
	}
	
	public boolean getF() {
		return f;
	}
	
	public boolean getFSharp() {
		return fSharp;
	}
	
	public boolean getG() {
		return g;
	}
	
	public boolean getGSharp() {
		return gSharp;
	}
	
	public boolean getA() {
		return a;
	}
	
	public boolean getASharp() {
		return aSharp;
	}
	
	public boolean getB() {
		return b;
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
	
	public boolean getNote(int i) {
		return allNotes[i];
	}
	
	public int getBassNo() {
		if(bass.equals("C#") | bass.equals("Db")) { return 1; }
		else if(bass.equals("D")) { return 2; }
		else if(bass.equals("D#") | bass.equals("Eb")) { return 3; }
		else if(bass.equals("E")) { return 4; }
		else if(bass.equals("F")) { return 5; }
		else if(bass.equals("F#") | bass.equals("Gb")) { return 6; }
		else if(bass.equals("G")) { return 7; }
		else if(bass.equals("G#") | bass.equals("Ab")) { return 8;}
		else if(bass.equals("A")) { return 9; }
		else if(bass.equals("A#") | bass.equals("Bb")) { return 10; }
		else if(bass.equals("B")) { return 11; }
		return 0;
	}
	
	public boolean firstThirdFifthMBass(Chord other) {
		if (this.getBassNo() == other.getBassNo()) {
			int i = this.getBassNo();
			if (this.getNote(i) == other.getNote(i) &&
					this.getNote((i + 4) % 12) == other.getNote((i + 4) % 12) &&
					this.getNote((i + 7) % 12) == other.getNote((i + 7) % 12)) {
					return true;
			}
		}
		return false;
	}
	public boolean firstThirdFifthmBass(Chord other) {
		if (this.getBassNo() == other.getBassNo()) {
			int i = this.getBassNo();
			if (this.getNote(i) == other.getNote(i) &&
					this.getNote((i + 3) % 12) == other.getNote((i + 3) % 12) &&
					this.getNote((i + 7) % 12) == other.getNote((i + 7) % 12)) {
					return true;
			}
		}
		return false;
	}
	
	public boolean firstThirdFifthM(Chord other) {
		for (int i = 0; i < 12; i++) {
			if (this.getNote(i) == other.getNote(i) &&
				this.getNote((i + 4) % 12) == other.getNote((i + 4) % 12) &&
				this.getNote((i + 7) % 12) == other.getNote((i + 7) % 12)) {
				return true;
			}
		}	
		return false;
	}
	
	public boolean firstThirdFifthm(Chord other) {
		for (int i = 0; i < 12; i++) {
			if (this.getNote(i) == other.getNote(i) &&
				this.getNote((i + 3) % 12) == other.getNote((i + 3) % 12) &&
				this.getNote((i + 7) % 12) == other.getNote((i + 7) % 12)) {
				return true;
			}
		}	
		return false;
	}
}
