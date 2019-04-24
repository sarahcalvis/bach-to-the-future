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
	static ArrayList<Integer> chordCountsTrain;
	//holds the number of occurrences of each chord in chordLabels
	static ArrayList<Integer> chordCountsTest;
	
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
		chordCountsTrain = getChordCountsTrain();
		
		//get the frequency of each chord
		chordCountsTest = getChordCountsTest();
		
//		for (int i = 0; i < chordLabels.size(); i++)  {
//			System.out.println(chordLabels.get(i) + " occurs " + chordCounts.get(i) + " times");
//		}
		
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
		//number of features
		int numFeatures = 14;
		
		//probability that a chord is of each type
		ArrayList<Double> probabilities;
		
		//count number of correct guesses
		int numCorrect = 0;
		
		//count number of correct guesses for each chord label
		ArrayList<Integer> correctByChord = new ArrayList<Integer>();
		//initialize the array so nothing breaks :)
		for (int i = 0; i < chordLabels.size(); i++) {
			correctByChord.add(0);
		}
		
		for (Chord testChord: test) {
			//holds the probability that the chord has each label
			probabilities = new ArrayList<Double>();
				
			//initialize the array so nothing breaks :)
			for (int i = 0; i < chordLabels.size(); i++) {
				probabilities.add(0.0);
			}
			
			//get the prior probabilities by dividing the number of occurences of the chord by the size of the train set
			for (int i = 0; i < chordLabels.size(); i++) {
				probabilities.set(i, Math.log((double)chordCountsTrain.get(i)/train.size()));
			}
			
			//counts the number of each feature we have
			ArrayList<ArrayList<Integer>> featureCounts = new ArrayList<ArrayList<Integer>>();			
			for (int j = 0; j < numFeatures; j++) {
				featureCounts.add(new ArrayList<Integer>());
				for (int i = 0; i < chordLabels.size(); i++) {
					featureCounts.get(j).add(0);
				}
			}
			
			//first count how many of each type of chord have the same c value as this chord
			for (Chord trainChord: train) {
				int i = 0;
				if (trainChord.getC() == testChord.getC()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getCSharp() == testChord.getCSharp()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getD() == testChord.getD()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getDSharp() == testChord.getDSharp()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getE() == testChord.getE()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getF() == testChord.getF()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getFSharp() == testChord.getFSharp()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getG() == testChord.getG()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getGSharp() == testChord.getGSharp()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getA() == testChord.getA()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getASharp() == testChord.getASharp()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getB() == testChord.getB()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getBass().equals(testChord.getBass())) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
				i++;
				if (trainChord.getMeter() == testChord.getMeter()) {
					featureCounts = updateFeatureCounts(trainChord, featureCounts, i);
				}
			}

			//update probabilities based on an added feature
			probabilities = updateProbabilities(probabilities, featureCounts);
					
			//update number of correct answers
			int ind = probabilities.indexOf(Collections.max(probabilities));
			if (chordLabels.get(ind).equals(testChord.getChordLabel())) {
				numCorrect++;
				int newNum = correctByChord.get(ind) + 1;
				correctByChord.set(ind, newNum);
			}
		}
		//print accuracy
		System.out.println("Number Correct: " + numCorrect + "\nTotal: " + test.size());
		System.out.println("Accuracy: " + (double)numCorrect/test.size());
		
		//print accuracy by chord
		for (int i = 0; i < correctByChord.size(); i++) {
			System.out.println("\n" + chordLabels.get(i) + " Number Correct: " + correctByChord.get(i) + "\nTotal: " + chordCountsTest.get(i));
			System.out.println("Accuracy: " + (double)correctByChord.get(i)/chordCountsTest.get(i));
		}
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
	public static ArrayList<Integer> getChordCountsTrain() {
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
	
	//get the number of occurrences of each chord in the test set
	public static ArrayList<Integer> getChordCountsTest() {
		ArrayList<Integer> chordCounts = new ArrayList<Integer>();
		for (int i = 0; i < chordLabels.size(); i++)  {
			chordCounts.add(0);
		}
		for (Chord trainChord: test) {
			String label = trainChord.getChordLabel();
			int index = chordLabels.indexOf(label);
			if (index != -1) {
				int newCount = chordCounts.get(index) + 1;
				chordCounts.set(index, newCount);
			}
		}
		return chordCounts;
	}
	
	//update the probabilities based on an added feature
	public static ArrayList<Double> updateProbabilities(ArrayList<Double> probabilities, ArrayList<ArrayList<Integer>> newFeatureCounts) {
		//then divide it by the number of chords of that label in the train set
		for (int j = 0; j < newFeatureCounts.size(); j++) {
			for (int i = 0; i < chordLabels.size(); i++) {
				double newProb = Math.log((double)newFeatureCounts.get(j).get(i)/chordCountsTrain.get(i));
				newProb += probabilities.get(i);
				probabilities.set(i, newProb);
			}
		}
		return probabilities;
	}
	
	//update the count of a particular feature
	public static ArrayList<ArrayList<Integer>> updateFeatureCounts(Chord trainChord, ArrayList<ArrayList<Integer>> featureCounts, int featureIndex) {
		int index = chordLabels.indexOf(trainChord.getChordLabel());
		if (index != -1) {
			int newVal = featureCounts.get(featureIndex).get(index) + 1;
			featureCounts.get(featureIndex).set(index, newVal);
		}
		return featureCounts;
	}	
}