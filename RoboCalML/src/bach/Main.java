package bach;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
	//holds all the data
	static ArrayList<Chord> data;
	//holds the train set
	static ArrayList<Chord> train;
	//holds the test set
	static ArrayList<Chord> test;
	//holds the possible chords
	static ArrayList<String> chordLabels;
	//holds the number of occurrences of each chord in chordLabels
	static ArrayList<Integer> chordCounts;
	
	public static void main(String[] args) {
		//read in the data
		try {
			data = ReadData();
		}
		catch (FileNotFoundException f) {
			System.out.println("File not found");
		}
		
		//shuffle the data, split it into a train set and a test set
		ShuffleAndSplit();
		
		//get all the chord options
		chordLabels = getPossibleChords();
		
		//get the frequency of each chord
		chordCounts = getChordCounts();
		
		for (int i = 0; i < chordLabels.size(); i++)  {
			System.out.println(chordLabels.get(i) + " occurs " + chordCounts.get(i) + " times");
		}
		
		classify();
	}
	
	//read in the data
	public static ArrayList<Chord> ReadData() throws FileNotFoundException {
		ArrayList<Chord> data = new ArrayList<Chord>();
		
		//data to describe each chord
		String sequence, bass, chordLabel;
		boolean c, cSharp, d, dSharp, e, f, fSharp, g, gSharp, a, aSharp, b;
		int eventNo, meter;
		
		//open the data file
		File chordsData = new File("data.txt");
		Scanner chordsScan = new Scanner(chordsData);
		
		//read in the file line by line
		while (chordsScan.hasNextLine()) {
			
			//each line in the file represents a chorale
			String chordStr = chordsScan.nextLine();
			
			//open a scanner for the chorale
			Scanner chordScan = new Scanner(chordStr);
			
			//there are no spaces in this file, just commas :)
			chordScan.useDelimiter(",");
			
			//get the sequence
			sequence = chordScan.next();
			
			//get the event number
			eventNo = chordScan.nextInt();
			
			//check if chord contains c
			if (chordScan.next().equals("YES")) {
				c = true;
			}
			else {
				c = false;
			}
			
			//check if chord contains c#
			if (chordScan.next().equals("YES")) {
				cSharp = true;
			}
			else {
				cSharp = false;
			}
			
			//check if chord contains d
			if (chordScan.next().equals("YES")) {
				d = true;
			}
			else {
				d = false;
			}

			//check if chord contains d#
			if (chordScan.next().equals("YES")) {
				dSharp = true;
			}
			else {
				dSharp = false;
			}

			//check if chord contains e
			if (chordScan.next().equals("YES")) {
				e = true;
			}
			else {
				e = false;
			}

			//check if chord contains f
			if (chordScan.next().equals("YES")) {
				f = true;
			}
			else {
				f = false;
			}
			
			//check if chord contains f#
			if (chordScan.next().equals("YES")) {
				fSharp = true;
			}
			else {
				fSharp = false;
			}

			//check if chord contains g
			if (chordScan.next().equals("YES")) {
				g = true;
			}
			else {
				g = false;
			}

			//check if chord contains g#
			if (chordScan.next().equals("YES")) {
				gSharp = true;
			}
			else {
				gSharp = false;
			}
		
			//check if chord contains a
			if (chordScan.next().equals("YES")) {
				a = true;
			}
			else {
				a = false;
			}
			
			//check if chord contains a#
			if (chordScan.next().equals("YES")) {
				aSharp = true;
			}
			else {
				aSharp = false;
			}

			//check if chord contains b
			if (chordScan.next().equals("YES")) {
				b = true;
			}
			else {
				b = false;
			}
			
			//find what note is the base of the chord
			bass = chordScan.next();
			
			//find meter of chorale
			meter = chordScan.nextInt();
			
			//find what chord it actually is
			chordLabel = chordScan.next();
			
			//close the cord scanner
			chordScan.close();
			
			//create the chord object 
			Chord newCord = new Chord(sequence, bass, chordLabel, 
					c, cSharp, d, dSharp, e, f,
					fSharp, g, gSharp, a, aSharp, b,
					eventNo, meter);
			
			//add the new chord to the arraylist
			data.add(newCord);
		}
		
		//close the chords scanner
		chordsScan.close();
		
		//return the arraylist
		return data;
	}
	
	//shuffle and split the data
	public static void ShuffleAndSplit() {
		//shuffle the data
		Collections.shuffle(data);
				
		//find where to split the data
		int eightyPercent = (data.size()/10) * 8;
		
		//make a train set
		train = new ArrayList<Chord>();
		for (int i = 0; i < eightyPercent; i++) {
			train.add(data.get(i));
		}
			
		//make a test set
		test = new ArrayList<Chord>();
		for (int i = eightyPercent; i < data.size(); i++) {
			test.add(data.get(i));
		}
	}
	
	//method to create a naive bayes classifier
	public static void classify() {
		ArrayList<Double> probabilities;
		
		int numCorrect = 0;
		for (Chord testChord: test) {
			//holds the probability that the chord has each label
			probabilities = new ArrayList<Double>();
				
			//initialize the array so nothing breaks :)
			for (int i = 0; i < chordLabels.size(); i++) {
				probabilities.add(0.0);
			}
			
			//get the prior probabilities by dividing the number of occurences of the chord by the size of the train set
			for (int i = 0; i < chordLabels.size(); i++) {
				probabilities.set(i, Math.log((double)chordCounts.get(i)/train.size()));
			}
				
				//*probability of c
				ArrayList<Integer> cCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					cCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getC() == testChord.getC()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = cCount.get(index) + 1;
							cCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)cCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of cSharp
				ArrayList<Integer> cSharpCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					cSharpCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getCSharp() == testChord.getCSharp()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = cSharpCount.get(index) + 1;
							cSharpCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)cSharpCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of d
				ArrayList<Integer> dCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					dCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getD() == testChord.getD()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = dCount.get(index) + 1;
							dCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)dCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of dSharp
				ArrayList<Integer> dSharpCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					dSharpCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getDSharp() == testChord.getDSharp()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = dSharpCount.get(index) + 1;
							dSharpCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)dSharpCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of e
				ArrayList<Integer> eCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					eCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getE() == testChord.getE()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = eCount.get(index) + 1;
							eCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)eCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of f
				ArrayList<Integer> fCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					fCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getF() == testChord.getF()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = fCount.get(index) + 1;
							fCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)fCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of f
				ArrayList<Integer> fSharpCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					fSharpCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getFSharp() == testChord.getFSharp()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = fSharpCount.get(index) + 1;
							fSharpCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)fSharpCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of g
				ArrayList<Integer> gCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					gCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getG() == testChord.getG()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = gCount.get(index) + 1;
							gCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)gCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of gSharp
				ArrayList<Integer> gSharpCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					gSharpCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getGSharp() == testChord.getGSharp()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = gSharpCount.get(index) + 1;
							gSharpCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)gSharpCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of a
				ArrayList<Integer> aCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					aCount.add(0);
				}
				//first count how many of each type of chord have the same c value as this chord
				for (Chord trainChord: train) {
					if (trainChord.getA() == testChord.getA()) {
						int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
							int newVal = aCount.get(index) + 1;
							aCount.set(index, newVal);
						}
					}
				}
				//then divide it by the number of chords of that label in the train set
				for (int i = 0; i < chordLabels.size(); i++) {
					double newProb = Math.log((double)aCount.get(i)/chordCounts.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
				
				//*probability of a sharp
				ArrayList<Integer> aSharpCount = new ArrayList<Integer>();
				for (int i = 0; i < chordLabels.size(); i++) {
					aSharpCount.add(0);
			}
			//first count how many of each type of chord have the same c value as this chord
			for (Chord trainChord: train) {
				if (trainChord.getASharp() == testChord.getASharp()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = aSharpCount.get(index) + 1;
						aSharpCount.set(index, newVal);
					}
				}
			}
			//then divide it by the number of chords of that label in the train set
			for (int i = 0; i < chordLabels.size(); i++) {
				double newProb = Math.log((double)aSharpCount.get(i)/chordCounts.get(i));
				newProb += probabilities.get(i);
				probabilities.set(i, newProb);
			}
			
			//*probability of b
			ArrayList<Integer> bCount = new ArrayList<Integer>();
			for (int i = 0; i < chordLabels.size(); i++) {
				bCount.add(0);
			}
			//first count how many of each type of chord have the same c value as this chord
			for (Chord trainChord: train) {
				if (trainChord.getB() == testChord.getB()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = bCount.get(index) + 1;
						bCount.set(index, newVal);
					}
				}
			}
			//then divide it by the number of chords of that label in the train set
			for (int i = 0; i < chordLabels.size(); i++) {
				double newProb = Math.log((double)bCount.get(i)/chordCounts.get(i));
				newProb += probabilities.get(i);
				probabilities.set(i, newProb);
			}
				
			//*probability of bass
			ArrayList<Integer> bassCount = new ArrayList<Integer>();
			for (int i = 0; i < chordLabels.size(); i++) {
				bassCount.add(0);
			}
				//first count how many of each type of chord have the same c value as this chord
			for (Chord trainChord: train) {
				if (trainChord.getBass().equals(testChord.getBass())) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = bassCount.get(index) + 1;
						bassCount.set(index, newVal);
					}
				}
			}
			//then divide it by the number of chords of that label in the train set
			for (int i = 0; i < chordLabels.size(); i++) {
				double newProb = Math.log((double)bassCount.get(i)/chordCounts.get(i));
				newProb += probabilities.get(i);
				probabilities.set(i, newProb);
			}
				
				//THIS ONE MIGHT NOT BE USEFUL
				//*probability of sequence
//				/*ArrayList<Integer> sequenceCount = new ArrayList<Integer>();
//				for (int i = 0; i < chordLabels.size(); i++) {
//					sequenceCount.add(0);
//				}
//				//first count how many of each type of chord have the same c value as this chord
//				for (Chord trainChord: train) {
//					if (trainChord.getSequence().equals(testChord.getSequence())) {
//						int index = chordLabels.indexOf(trainChord.getChordLabel());
//						if (index != -1) {
//							int newVal = sequenceCount.get(index) + 1;
//							sequenceCount.set(index, newVal);
//						}
//					}
//				}
//				//then divide it by the number of chords of that label in the train set
//				for (int i = 0; i < chordLabels.size(); i++) {
//					double newProb = Math.log((double)sequenceCount.get(i)/chordCounts.get(i));
//					newProb += probabilities.get(i);
//					probabilities.set(i, newProb);
//				}*/
//				//this one appears to be unhelpful
//				/*
//				//*probability of eventNo
//				ArrayList<Integer> eventCount = new ArrayList<Integer>();
//				for (int i = 0; i < chordLabels.size(); i++) {
//					eventCount.add(0);
//				}
//				//first count how many of each type of chord have the same c value as this chord
//				for (Chord trainChord: train) {
//					if (trainChord.getEventNo() == testChord.getEventNo()) {
//						int index = chordLabels.indexOf(trainChord.getChordLabel());
//						if (index != -1) {
//							int newVal = eventCount.get(index) + 1;
//							eventCount.set(index, newVal);
//						}
//					}
//				}
//				//then divide it by the number of chords of that label in the train set
//				for (int i = 0; i < chordLabels.size(); i++) {
//					double newProb = Math.log((double)eventCount.get(i)/chordCounts.get(i));
//					newProb += probabilities.get(i);
//					probabilities.set(i, newProb);
//				}
				
				//*probability of eventNo
//				ArrayList<Integer> eventCount = new ArrayList<Integer>();
//				for (int i = 0; i < chordLabels.size(); i++) {
//					eventCount.add(0);
//				}
//				//first count how many of each type of chord have the same c value as this chord
//				for (Chord trainChord: train) {
//					if (trainChord.getEventNo() == testChord.getEventNo()) {
//						int index = chordLabels.indexOf(trainChord.getChordLabel());
//						if (index != -1) {
//							int newVal = eventCount.get(index) + 1;
//							eventCount.set(index, newVal);
//						}
//					}
//				}
//				//then divide it by the number of chords of that label in the train set
//				for (int i = 0; i < chordLabels.size(); i++) {
//					double newProb = Math.log((double)eventCount.get(i)/chordCounts.get(i));
//					newProb += probabilities.get(i);
//					probabilities.set(i, newProb);
//				}
//				
				
			//not sure if this one helps or not - might want to collect data on that
			//*probability of meter
			ArrayList<Integer> meterCount = new ArrayList<Integer>();
			for (int i = 0; i < chordLabels.size(); i++) {
				meterCount.add(0);
			}
			//first count how many of each type of chord have the same c value as this chord
			for (Chord trainChord: train) {
				if (trainChord.getMeter() == testChord.getMeter()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = meterCount.get(index) + 1;
						meterCount.set(index, newVal);
					}
				}
			}
			//then divide it by the number of chords of that label in the train set
			for (int i = 0; i < chordLabels.size(); i++) {
				double newProb = Math.log((double)meterCount.get(i)/chordCounts.get(i));
				newProb += probabilities.get(i);
				probabilities.set(i, newProb);
			}
					
			//calculate accuracy
			int ind = probabilities.indexOf(Collections.max(probabilities));
			if (chordLabels.get(ind).contentEquals(testChord.getChordLabel())) {
				numCorrect++;
			}
		}
		System.out.println("Number Correct: " + numCorrect + "\nTotal: " + test.size());
		System.out.println("Accuracy: " + (double)numCorrect/test.size());
	}
		
	//get all the chords available in the train set
	public static ArrayList<String> getPossibleChords() {
		ArrayList<String> chordOptions = new ArrayList<String>();
		for (Chord chord: train) {
			String label = chord.getChordLabel();
			if(!chordOptions.contains(label)) {
				chordOptions.add(label);
			}
		}
		return chordOptions;
	}
	
	//get the number of occurrences of each chord in the train set
	public static ArrayList<Integer> getChordCounts() {
		ArrayList<Integer> chordCounts = new ArrayList<Integer>();
		for (int i = 0; i < chordLabels.size(); i++)  {
			chordCounts.add(0);
		}
		for (Chord trainChord: train) {
			String label = trainChord.getChordLabel();
			int index = chordLabels.indexOf(label);
			int newCount = chordCounts.get(index) + 1;
			chordCounts.set(index, newCount);
		}
		return chordCounts;
	}
}
