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
		getPossibleChords();
		
		//get the number of occurrences of each chord in the train and test set
		getChordCounts();
		
		//do a bayes
		classify();
	}
	
	//do the naive bayes classification
	public static void classify() {
		
		
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
		
		//number of features
		int numFeatures = 15;
		
		for (Chord testChord: test) {
			//holds the probability that the chord has each label
			probabilities = new ArrayList<Double>();
				
			//get the prior probabilities by dividing the number of occurences of the chord by the size of the train set
			for (int i = 0; i < chordLabels.size(); i++) {
				probabilities.add(Math.log((double)chordCountsTrain.get(i)/train.size()));
			}
			
			//counts the number of each feature we have
			//each row holds a feature
			//each column holds a how many of a label have this feature
			ArrayList<int[]> featureCounts = new ArrayList<int[]>();			
			for (int j = 0; j < numFeatures; j++) {
				featureCounts.add(new int[chordLabels.size()]);
				for (int i = 0; i < chordLabels.size(); i++) {
					featureCounts.get(j)[i] = 0;
				}
			}
			
			//get the test notes
			boolean[] testNotes = testChord.getAllNotes();

			//first count how many of each type of chord have the same c value as this chord
			for (Chord trainChord: train) {
				//get the test notes
				boolean[] trainNotes = trainChord.getAllNotes();
				
				//features 1 - 13
				int i = 0;
				for (i = 0; i < 12; i++) {
					if (trainNotes[i] == testNotes[i]) {
						featureCounts = updateFeatureCounts(trainChord.getChordLabel(), featureCounts, i);
					}
				}
				
				//feature 14
				i++;
				if (trainChord.getBass().equals(testChord.getBass())) {
					featureCounts = updateFeatureCounts(trainChord.getChordLabel(), featureCounts, i);
				}
				
				//feature 15
				i++;
				if (trainChord.getMeter() == testChord.getMeter()) {
					featureCounts = updateFeatureCounts(trainChord.getChordLabel(), featureCounts, i);
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
	
	//update the count of a particular feature
	public static ArrayList<int[]> updateFeatureCounts(String label, ArrayList<int[]> featureCounts, int featureIndex) {
		int index = chordLabels.indexOf(label);
		if (index != -1) {
			featureCounts.get(featureIndex)[index] += 1;
		}
		return featureCounts;
	}
	
	//update the probabilities based on an added feature
	public static ArrayList<Double> updateProbabilities(ArrayList<Double> probabilities, ArrayList<int[]> newFeatureCounts) {
		//then divide it by the number of chords of that label in the train set
		for (int j = 0; j < newFeatureCounts.size(); j++) {
			for (int i = 0; i < chordLabels.size(); i++) {
				double newProb = Math.log((double)newFeatureCounts.get(j)[i]/chordCountsTrain.get(i));
				newProb += probabilities.get(i);
				probabilities.set(i, newProb);
			}
		}
		return probabilities;
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
			
		//read in the file line by line and get the features
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
			
			//find out which notes are in the chords
			if (chordScan.next().equals("YES")) {
				c = true;
			}
			else {
				c = false;
			}
			if (chordScan.next().equals("YES")) {
				cSharp = true;
			}
			else {
				cSharp = false;
			}
			if (chordScan.next().equals("YES")) {
				d = true;
			}
			else {
				d = false;
			}
			if (chordScan.next().equals("YES")) {
				dSharp = true;
			}
			else {
				dSharp = false;
			}
			if (chordScan.next().equals("YES")) {
				e = true;
			}
			else {
				e = false;
			}
			if (chordScan.next().equals("YES")) {
				f = true;
			}
			else {
				f = false;
			}
			if (chordScan.next().equals("YES")) {
				fSharp = true;
			}
			else {
				fSharp = false;
			}
			if (chordScan.next().equals("YES")) {
				g = true;
			}
			else {
				g = false;
			}
			if (chordScan.next().equals("YES")) {
				gSharp = true;
			}
			else {
				gSharp = false;
			}
			if (chordScan.next().equals("YES")) {
				a = true;
			}
			else {
				a = false;
			}
			if (chordScan.next().equals("YES")) {
				aSharp = true;
			}
			else {
				aSharp = false;
			}
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
	
	//get all the chords available in the train set
	public static void getPossibleChords() {
		chordLabels = new ArrayList<String>();
		for (Chord chord: train) {
			String label = chord.getChordLabel();
			if(!chordLabels.contains(label)) {
				chordLabels.add(label);
			}
		}
	}
	
	//get the number of occurrences of each chord in the train set and test set
	public static void getChordCounts() {
		chordCountsTrain = new ArrayList<Integer>();
		chordCountsTest = new ArrayList<Integer>();
		for (int i = 0; i < chordLabels.size(); i++)  {
			chordCountsTrain.add(0);
			chordCountsTest.add(0);
		}
		for (Chord trainChord: train) {
			String label = trainChord.getChordLabel();
			int index = chordLabels.indexOf(label);
			int newCount = chordCountsTrain.get(index) + 1;
			chordCountsTrain.set(index, newCount);
		}
		for (Chord testChord: test) {
			String label = testChord.getChordLabel();
			int index = chordLabels.indexOf(label);
			if (index != -1) {
				int newCount = chordCountsTest.get(index) + 1;
				chordCountsTest.set(index, newCount);
			}
		}
	}
}