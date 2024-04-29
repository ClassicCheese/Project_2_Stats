package Project_part1;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.text.DecimalFormat;


public class mainSystem {

	Scanner scan = new Scanner(System.in);

	// This create a file, in case needed
	public File createFile(String name) {
		name = fixName(name);
		File file = new File(name);
		return file;
	}
	
	//Ask user to enter bounds
	public Double[] getBounds() {
		Double[] bounds = new Double[3];
		
		System.out.println("Enter the lower bound of the range");
		double low = scan.nextDouble();
		System.out.println("Enter the upper bound of the range");
		double upper = scan.nextDouble();
		System.out.println("Enter the increment rate");
		double inc = scan.nextDouble();

		bounds[0] = low;
		bounds[1] = upper;
		bounds[2] = inc;
		
		return bounds;
	}

	// This create a file and write to it
	public BufferedWriter writeFile(String name) {
		try {
			File file = createFile(name);
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			return bufferedWriter;
		} catch (IOException e) {
			System.out.println("An error occured during the creation of salted file");
			return null;
		}
	}

	// This create a reader object to read the file
	public BufferedReader readFile(String name) {
		try {
			name = fixName(name);
			BufferedReader br = new BufferedReader(new FileReader(name));
			return br;
		} catch (IOException e) {
			System.out.println("File not found");
			return null;
		}
	}

	// This ask the user for the name of a file
	public String askName() {
		String fileName = scan.nextLine();
		fileName = fixName(fileName);
		return fileName;
	}

	// This insure the user inter the correct format csv files for now since it's
	// what we using
	public String fixName(String name) {
		if (!name.contains(".csv")) {
			name += ".csv";
		}
		return name;
	}

	// This generate the salting values
	// Randomly chose 0 or 1
	// + or - accordingly
	public double salting(double salt, double range) {
		Random rng = new Random();
		int num = rng.nextInt(2);

		switch (num) {
		case 0:
			salt += range;
			break;
		case 1:
			salt -= range;
			break;
		default:
			break;
		}
		return salt;
	}
	
	//The smoothing method
	public ArrayList<Double> smoothy(ArrayList<Double> sm){
		int count = 0;
		double avg;
		ArrayList<Double> average = new ArrayList<>();
		for (int i = 0; i < sm.size(); i++) {
			count = 1;
			avg = sm.get(i);
			
			for (int l = Math.max(0, i - 3); l < i; l++) {
				if (l <= (sm.size() - 1) && l >= 0) {
					avg += sm.get(l);
					count++;
				}
			}

			for (int u = i + 1; u <= Math.min(sm.size() - 1, i + 3); u++) {
				if (u <= 3 && u <= sm.size() - 1 || u < (sm.size() - 1)) {
					
					avg += sm.get(u);
					count++;
				}
			}

			average.add(format(avg/count));
		}
		return average;
	}


	// This count how many lines are in the file.
	public static int countLine(String name) {
		try (BufferedReader br = new BufferedReader(new FileReader(name))) {
			int lineCount = 0;
			while (br.readLine() != null) {
				lineCount++;
			}
			return lineCount;
		} catch (IOException e) {
			return -1;
		}
	}

	// This adjust the format
	public double format (double in) {
		
		DecimalFormat df = new DecimalFormat("0.###");
		
		String formatted = df.format(in);
		
		return Double.parseDouble(formatted);
	}

	// I tried to used recursion but then it breaks when the increment is 0.001 or
	// smaller
	// So I just for loop it
	public double getArea(double cur, double upper, boolean isRad) {
		double rad;

			if (isRad == false) {
				rad = Math.toRadians(cur);
			} else rad = cur;;
			double area = Math.cos(rad);
			area = format(area);

		return area;
	}

}
