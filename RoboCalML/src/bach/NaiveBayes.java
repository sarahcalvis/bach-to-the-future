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
//			/*ArrayList<Integer> sequenceCount = new ArrayList<Integer>();
//			for (int i = 0; i < chordLabels.size(); i++) {
//				sequenceCount.add(0);
//			}
//			//first count how many of each type of chord have the same c value as this chord
//			for (Chord trainChord: train) {
//				if (trainChord.getSequence().equals(testChord.getSequence())) {
//					int index = chordLabels.indexOf(trainChord.getChordLabel());
//					if (index != -1) {
//						int newVal = sequenceCount.get(index) + 1;
//						sequenceCount.set(index, newVal);
//					}
//				}
//			}
//			//then divide it by the number of chords of that label in the train set
//			for (int i = 0; i < chordLabels.size(); i++) {
//				double newProb = Math.log((double)sequenceCount.get(i)/chordCounts.get(i));
//				newProb += probabilities.get(i);
//				probabilities.set(i, newProb);
//			}*/
//			//this one appears to be unhelpful
//			/*
//			//*probability of eventNo
//			ArrayList<Integer> eventCount = new ArrayList<Integer>();
//			for (int i = 0; i < chordLabels.size(); i++) {
//				eventCount.add(0);
//			}
//			//first count how many of each type of chord have the same c value as this chord
//			for (Chord trainChord: train) {
//				if (trainChord.getEventNo() == testChord.getEventNo()) {
//					int index = chordLabels.indexOf(trainChord.getChordLabel());
//					if (index != -1) {
//						int newVal = eventCount.get(index) + 1;
//						eventCount.set(index, newVal);
//					}
//				}
//			}
//			//then divide it by the number of chords of that label in the train set
//			for (int i = 0; i < chordLabels.size(); i++) {
//				double newProb = Math.log((double)eventCount.get(i)/chordCounts.get(i));
//				newProb += probabilities.get(i);
//				probabilities.set(i, newProb);
//			}
			
			//*probability of eventNo
//			ArrayList<Integer> eventCount = new ArrayList<Integer>();
//			for (int i = 0; i < chordLabels.size(); i++) {
//				eventCount.add(0);
//			}
//			//first count how many of each type of chord have the same c value as this chord
//			for (Chord trainChord: train) {
//				if (trainChord.getEventNo() == testChord.getEventNo()) {
//					int index = chordLabels.indexOf(trainChord.getChordLabel());
//					if (index != -1) {
//						int newVal = eventCount.get(index) + 1;
//						eventCount.set(index, newVal);
//					}
//				}
//			}
//			//then divide it by the number of chords of that label in the train set
//			for (int i = 0; i < chordLabels.size(); i++) {
//				double newProb = Math.log((double)eventCount.get(i)/chordCounts.get(i));
//				newProb += probabilities.get(i);
//				probabilities.set(i, newProb);
//			}
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
			
			//divide by total
			//or maybe that;s irrelevant
			
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
