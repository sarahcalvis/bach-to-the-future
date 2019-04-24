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
				
			ArrayList<Integer> cCount = new ArrayList<Integer>();
			ArrayList<Integer> cSharpCount = new ArrayList<Integer>();
			ArrayList<Integer> dCount = new ArrayList<Integer>();
			ArrayList<Integer> dSharpCount = new ArrayList<Integer>();
			ArrayList<Integer> eCount = new ArrayList<Integer>();
			ArrayList<Integer> fCount = new ArrayList<Integer>();
			ArrayList<Integer> fSharpCount = new ArrayList<Integer>();
			ArrayList<Integer> gCount = new ArrayList<Integer>();
			ArrayList<Integer> gSharpCount = new ArrayList<Integer>();
			ArrayList<Integer> aCount = new ArrayList<Integer>();
			ArrayList<Integer> aSharpCount = new ArrayList<Integer>();
			ArrayList<Integer> bCount = new ArrayList<Integer>();
			ArrayList<Integer> bassCount = new ArrayList<Integer>();
			ArrayList<Integer> meterCount = new ArrayList<Integer>();

			for (int i = 0; i < chordLabels.size(); i++) {
				cCount.add(0);
				cSharpCount.add(0);
				dCount.add(0);
				dSharpCount.add(0);
				eCount.add(0);
				fCount.add(0);
				fSharpCount.add(0);
				gCount.add(0);
				gSharpCount.add(0);
				aCount.add(0);
				aSharpCount.add(0);
				bCount.add(0);
				bassCount.add(0);
				meterCount.add(0);
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
				if (trainChord.getCSharp() == testChord.getCSharp()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = cSharpCount.get(index) + 1;
						cSharpCount.set(index, newVal);
					}
				}
				if (trainChord.getD() == testChord.getD()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = dCount.get(index) + 1;
						dCount.set(index, newVal);
					}
				}
				if (trainChord.getDSharp() == testChord.getDSharp()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = dSharpCount.get(index) + 1;
						dSharpCount.set(index, newVal);
					}
				}
				if (trainChord.getE() == testChord.getE()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = eCount.get(index) + 1;
						eCount.set(index, newVal);
					}
				}
				if (trainChord.getF() == testChord.getF()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = fCount.get(index) + 1;
						fCount.set(index, newVal);
					}
				}
				if (trainChord.getFSharp() == testChord.getFSharp()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = fSharpCount.get(index) + 1;
						fSharpCount.set(index, newVal);
					}
				}
				if (trainChord.getG() == testChord.getG()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = gCount.get(index) + 1;
						gCount.set(index, newVal);
					}
				}
				if (trainChord.getGSharp() == testChord.getGSharp()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = gSharpCount.get(index) + 1;
						gSharpCount.set(index, newVal);
					}
				}
				if (trainChord.getA() == testChord.getA()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
						if (index != -1) {
						int newVal = aCount.get(index) + 1;
						aCount.set(index, newVal);
					}
				}
				if (trainChord.getASharp() == testChord.getASharp()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = aSharpCount.get(index) + 1;
						aSharpCount.set(index, newVal);
					}
				}
				if (trainChord.getB() == testChord.getB()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = bCount.get(index) + 1;
						bCount.set(index, newVal);
					}
				}
				if (trainChord.getBass().equals(testChord.getBass())) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = bassCount.get(index) + 1;
						bassCount.set(index, newVal);
					}
				}
				if (trainChord.getMeter() == testChord.getMeter()) {
					int index = chordLabels.indexOf(trainChord.getChordLabel());
					if (index != -1) {
						int newVal = meterCount.get(index) + 1;
						meterCount.set(index, newVal);
					}
				}
			}

			//update probabilities based on an added feature
			probabilities = updateProbabilities(probabilities, cCount);
			probabilities = updateProbabilities(probabilities, cSharpCount);
			probabilities = updateProbabilities(probabilities, dCount);
			probabilities = updateProbabilities(probabilities, dSharpCount);
			probabilities = updateProbabilities(probabilities, eCount);
			probabilities = updateProbabilities(probabilities, fCount);
			probabilities = updateProbabilities(probabilities, fSharpCount);
			probabilities = updateProbabilities(probabilities, gCount);
			probabilities = updateProbabilities(probabilities, gSharpCount);
			probabilities = updateProbabilities(probabilities, aCount);
			probabilities = updateProbabilities(probabilities, aSharpCount);
			probabilities = updateProbabilities(probabilities, bCount);
			probabilities = updateProbabilities(probabilities, bassCount);
			probabilities = updateProbabilities(probabilities, meterCount);
					
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
	
	//update the probabilities bassed on an added feature
	public static ArrayList<Double> updateProbabilities(ArrayList<Double> probabilities, ArrayList<Integer> newFeatureCounts) {
		//then divide it by the number of chords of that label in the train set
		for (int i = 0; i < chordLabels.size(); i++) {
			double newProb = Math.log((double)newFeatureCounts.get(i)/chordCounts.get(i));
			newProb += probabilities.get(i);
			probabilities.set(i, newProb);
		}
		return probabilities;
	}
}