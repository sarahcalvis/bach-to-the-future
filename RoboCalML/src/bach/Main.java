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
		
		//perform classification with a decision tree
		DecisionTree decisionTree = new DecisionTree(train, test);
		
		//perform classification with naive bayes
		NaiveBayes naiveBayes = new NaiveBayes(train, test);
		naiveBayes.classify();
	}
	
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
}
