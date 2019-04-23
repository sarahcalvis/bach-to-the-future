package bach;

public class Chord {
	String sequence, bass, chordLabel;
	Boolean c, cSharp, d, dSharp, e, f, fSharp, g, gSharp, a, aSharp, b;
	int eventNo, meter;
	public Chord(String sequence, String bass, String chordLabel, 
					Boolean c, Boolean cSharp, Boolean d, Boolean dSharp, Boolean e, Boolean f,
					Boolean fSharp, Boolean g, Boolean gSharp, Boolean a, Boolean aSharp, Boolean b,
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
	}
}
