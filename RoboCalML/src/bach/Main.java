package bach;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Sarah Calvis and Micaela Robosky
 * This program performs Naive Bayes Classification on the Bach Choral Harmony Data Set
 * https://archive.ics.uci.edu/ml/datasets/Bach+Choral+Harmony
 */
public class Main {
	static ArrayList<Chord> data;				//holds all the data
	static ArrayList<Chord> train;				//holds the train set
	static ArrayList<Chord> test;				//holds the test set
	static ArrayList<String> chordLabels;		//holds the possible chords
	static ArrayList<Integer> chordCountsTrain;	//holds the number of occurrences of each chord in chordLabels
	static ArrayList<Integer> chordCountsTest;	//holds the number of occurrences of each chord in chordLabels
	
	public static void main(String[] args) {
		try {
			data = ReadData();		//read in the data
			cleanData();			//clean data array
			ShuffleAndSplit();		//shuffle the data, split it into a train set and a test set
			cleanTrainAndTest();	//clean train and test sets
			getPossibleChords();	//get all the chord options
			getChordCounts();		//get the number of occurrences of each chord in the train and test set
			classify();				//do a bayes
		} catch (FileNotFoundException f) {
			System.out.println(f.getMessage());
		}
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
			Scanner chordScan = new Scanner(chordStr);
			chordScan.useDelimiter(",");
				
			//get the chord attributes
			sequence = chordScan.next();
			eventNo = chordScan.nextInt();
			if (chordScan.next().equals("YES")) {c = true;} else {c = false;}
			if (chordScan.next().equals("YES")) {cSharp = true;} else {cSharp = false;}
			if (chordScan.next().equals("YES")) {d = true;} else {d = false;}
			if (chordScan.next().equals("YES")) {dSharp = true;} else {dSharp = false;}
			if (chordScan.next().equals("YES")) {e = true;} else {e = false;}
			if (chordScan.next().equals("YES")) {f = true;} else {f = false;}
			if (chordScan.next().equals("YES")) {fSharp = true;} else {fSharp = false;}
			if (chordScan.next().equals("YES")) {g = true;} else {g = false;}
			if (chordScan.next().equals("YES")) {gSharp = true;} else {gSharp = false;}
			if (chordScan.next().equals("YES")) {a = true;} else {a = false;}
			if (chordScan.next().equals("YES")) {aSharp = true;} else {aSharp = false;}
			if (chordScan.next().equals("YES")) {b = true;} else {b = false;}
			bass = chordScan.next();
			meter = chordScan.nextInt();
			chordLabel = chordScan.next();
				
			//close the chord scanner
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
		return data;
	}
	
	//remove everything from the data that only appears once
	public static void cleanData() {
		for (int i = 0; i < data.size(); i++) {
			boolean remove = true;
			for(int j = 0; j < data.size(); j++) {
				if(data.get(i).getChordLabel().equals(data.get(j).getChordLabel()) && j != i) {
					remove = false;
				}
			}
			if (remove) {
				data.remove(i);
			}
		}
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
	
	//removes every chord in the train set that doesn't appear in the test set
	public static void cleanTrainAndTest() {
		for (int i = 0; i < test.size(); i++) {
			boolean remove = true;
			for(int j = 0; j < train.size(); j++) {
				if(train.get(j).getChordLabel().equals(test.get(i).getChordLabel())) {
					remove = false;
				}
			}
			if (remove) {
				test.remove(i);
			}
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
	
	//do the naive bayes classification
	public static void classify() {
		//count number of correct guesses
		int numCorrect = 0;
		int seventhAcc = 0;
		int sevenths = 0;
		
		//count number of correct guesses for each chord label
		ArrayList<Integer> correctByChord = new ArrayList<Integer>();
		for (int i = 0; i < chordLabels.size(); i++) {
			correctByChord.add(0);
		}
		
		for (Chord testChord: test) {
			//holds the probability that the chord has each label
			ArrayList<Double> probabilities = getPriors();
			
			//one row for each chord, one column for each feature
			//give that the chord is i, what is the probability that it would have feature j?
			ArrayList<ArrayList<Integer>> featureCounts = test(testChord);
			
			//update probabilities based on an added feature
			probabilities = updateProbabilities(probabilities, featureCounts);
				
			//update number of correct answers
			int ind = probabilities.indexOf(Collections.max(probabilities));
			if (chordLabels.get(ind).contains("7")) {
				sevenths++;
			}
			if (chordLabels.get(ind).equals(testChord.getChordLabel())) {
				numCorrect++;
				int newNum = correctByChord.get(ind) + 1;
				correctByChord.set(ind, newNum);
				if (chordLabels.get(ind).contains("7")) {
					seventhAcc++;
				}
			}
		}
		//print accuracy
		System.out.println("Total Number Correct: " + numCorrect + "\nTotal Number of Chords: " + test.size());
		System.out.println("Overall Accuracy: " + (double)numCorrect/test.size());
		
		//print seventh accuracy
		System.out.println("\nNumber of Sevenths Correct: " + seventhAcc + "\nTotal: " + sevenths);
		System.out.println("Accuracy: " + (double)seventhAcc/sevenths);
		
		//print accuracy by chord
		for (int i = 0; i < correctByChord.size(); i++) {
			System.out.println("\nChord: " + chordLabels.get(i) + "\nNumber Correct: " +correctByChord.get(i)+ "\nTotal: " + chordCountsTest.get(i));
			System.out.println("Accuracy: " + (double)correctByChord.get(i)/chordCountsTest.get(i));
		}
	}
	
	//get the prior probability of each chord label
	static ArrayList<Double> getPriors() {
		ArrayList<Double> probabilities = new ArrayList<Double>();
		for (int i = 0; i < chordLabels.size(); i++) {
			probabilities.add(0.0);
		}
		
		//get the prior probabilities by dividing the number of occurences of the chord by the size of the train set
		for (int i = 0; i < chordLabels.size(); i++) {
			probabilities.set(i, Math.log((double)chordCountsTrain.get(i)/train.size()));
		}
		return probabilities;
	}
	
	//count how many of each chord in the train set share features with our chord in the test set
	static ArrayList<ArrayList<Integer>> test(Chord testChord) {
		int numFeatures = 15;
		
		ArrayList<ArrayList<Integer>> featureCounts = new ArrayList<ArrayList<Integer>>();	
		for (int j = 0; j < numFeatures; j++) {
			featureCounts.add(new ArrayList<Integer>());
			for (int i = 0; i < chordLabels.size(); i++) {
				featureCounts.get(j).add(0);
			}
		}
		
		for (Chord trainChord: train) {
			int i = 0;
			if (trainChord.getC() == testChord.getC()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getCSharp() == testChord.getCSharp()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getD() == testChord.getD()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getDSharp() == testChord.getDSharp()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getE() == testChord.getE()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getF() == testChord.getF()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getFSharp() == testChord.getFSharp()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getG() == testChord.getG()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getGSharp() == testChord.getGSharp()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getA() == testChord.getA()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getASharp() == testChord.getASharp()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getB() == testChord.getB()) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.getBass().equals(testChord.getBass())) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.sameSong(testChord)) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			
			//Working synthetic feature: predicting sevenths
			i++;
			if (trainChord.seventh(testChord)) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			
			//FAILED Synthetic Features- here because they are in the report
	/*		i++;
			if (trainChord.firstThirdFifthMBass(testChord)) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.firstThirdFifthmBass(testChord)) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.firstThirdFifthM(testChord)) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }
			i++;
			if (trainChord.firstThirdFifthm(testChord)) { featureCounts = updateFeatureCounts(trainChord, featureCounts, i); }*/	
			
			
		}
		
		return featureCounts;
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
	
	//update the probabilities based on an added feature
	public static ArrayList<Double> updateProbabilities(ArrayList<Double> probabilities, ArrayList<ArrayList<Integer>> newFeatureCounts) {
		//then divide it by the number of chords of that label in the train set
		for (int j = 0; j < newFeatureCounts.size(); j++) {
			for (int i = 0; i < chordLabels.size(); i++) {
				if (newFeatureCounts.get(j).get(i) >= 0) {
					double newProb = Math.log((double)newFeatureCounts.get(j).get(i)/chordCountsTrain.get(i));
					newProb += probabilities.get(i);
					probabilities.set(i, newProb);
				}
			}
		}
		return probabilities;
	}
}