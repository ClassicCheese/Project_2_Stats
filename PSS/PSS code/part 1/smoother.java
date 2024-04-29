package Project_part1;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class smoother {

	public static void main(String[] args) {
		// Initialize variables and parameters
		mainSystem use = new mainSystem();
		String fileName, smoothed;
		String line;
		StringBuilder build = new StringBuilder();
		String[] splitter = new String[2];
		ArrayList<Double> x = new ArrayList<>();
		ArrayList<Double> y = new ArrayList<>();
		
		Scanner scan = new Scanner(System.in);

		System.out.println("Enter the file name");
		fileName = scan.nextLine();
		fileName = use.fixName(fileName);
		smoothed = fileName.replace(".csv", "_smoothed.csv");
		try {

			BufferedReader br = use.readFile(fileName);

			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				splitter = line.split(",");

				if (firstLine) {
					build.append(line).append("\n");
					firstLine = false;
					continue;
				}
				x.add(Double.parseDouble(splitter[0].trim()));
				y.add(Double.parseDouble(splitter[1].trim()));
			}
			System.out.println("X size: " + x.size());
			System.out.println("Y size: " + y.size());

			ArrayList<Double> average = use.smoothy(y);
			
			
			//Get ready to write data into a file
			for (int num = 0; num < x.size(); num++) {
				build.append(x.get(num)).append(", ").append(average.get(num)).append("\n");
			}
			BufferedWriter write = use.writeFile(smoothed);
			write.write(build.toString());
			System.out.println("Successfully smooth the data to " + smoothed);
			scan.close();
			write.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found " + fileName);
		} catch (IOException e) {
			System.out.println("An error occured somewhere.");
		}

	}
	
	

}
