package bach;

import java.util.ArrayList;
import java.util.Collections;

public class NaiveBayes {
	ArrayList<Chord> train;
	ArrayList<Chord> test;
	public NaiveBayes(ArrayList<Chord> train, ArrayList<Chord> test) {
		this.train = train;
		this.test = test;
	}
	
	//method to create a naive bayes classifier
	public void classify() {
		//get all the chords that this could be
		ArrayList<String> chordLabels = getPossibleChords();
		ArrayList<Double> probabilities;
		ArrayList<Integer> chordCounts;
		int numCorrect = 0;
		for (Chord testChord: test) {
			//holds the probability that the chord has each label
			probabilities = new ArrayList<Double>();
			
			//just use this to count stuff
			chordCounts = new ArrayList<Integer>();
			
			//initialize the arrays so nothing breaks :)
			for (int i = 0; i < chordLabels.size(); i++) {
				chordCounts.add(0);
				probabilities.add(0.0);
			}
			
			//get the prior probabilities
			//first count up how many of each chord is in the train set
			for (Chord trainChord: train) {
				String label = trainChord.getChordLabel();
				int index = chordLabels.indexOf(label);
				int newCount = chordCounts.get(index) + 1;
				chordCounts.set(index, newCount);
			}
			//then divide it by the total number of chords in the train set
			for (int i = 0; i < chordLabels.size(); i++) {
				probabilities.set(i, (double)chordCounts.get(i)/train.size());
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
			
			//calculate accuracy
			int ind = probabilities.indexOf(Collections.max(probabilities));
			if (chordLabels.get(ind).contentEquals(testChord.getChordLabel())) {
				numCorrect++;
			}
		}
		System.out.println("Number Correct: " + numCorrect + "\nTotal: " + test.size());
		System.out.println("Accuracy: " + (double)numCorrect/test.size());
	}
	
	//get all the chords available
	public ArrayList<String> getPossibleChords() {
		ArrayList<String> chordOptions = new ArrayList<String>();
		for (Chord chord: train) {
			String label = chord.getChordLabel();
			if(!chordOptions.contains(label)) {
				chordOptions.add(label);
			}
		}
		return chordOptions;
	}
}
