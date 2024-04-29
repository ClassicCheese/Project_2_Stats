package final_project_part_3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import Project_part1.mainSystem;
import javax.swing.*;

import org.jfree.data.xy.XYSeries;


//Since this is just the same as part 1 but with graph, so I just copy and paste it
//I also don't know if we only need to graph or also store data
//So I did both
public class smooth_graph {

	public static void main(String[] args) {
		// Initialize variables and parameters
		mainSystem use = new mainSystem();
		XYSeries points = new XYSeries("Smoothed Cos(x) graph");
		String fileName, smoothed;
		String line;
		int count;
		int limit = 3;
		double avg;

		StringBuilder build = new StringBuilder();
		String[] splitter = new String[2];
		ArrayList<Double> x = new ArrayList<>();
		ArrayList<Double> y = new ArrayList<>();
		
		Scanner scan = new Scanner(System.in);

		System.out.println("Enter file name: ");
		fileName = use.askName();
		smoothed = fileName.replace(".csv", "_smoothed.csv");
		try {

			BufferedReader br = use.readFile(fileName);
			BufferedWriter write = use.writeFile(smoothed);
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

			double[] average = new double[y.size()];
			for (int i = 0; i < y.size(); i++) {
				count = 1;
				avg = y.get(i);
				
				//This add the values of the left
				for (int l = Math.max(0, i - limit); l < i; l++) {
					if (l <= (y.size() - 1) && l >= 0) {
						avg += y.get(l);
						count++;
					}
				}

				//this add the values of the right
				for (int u = i + 1; u <= Math.min(y.size() - 1, i + limit); u++) {
					if (u <= 3 && u <= y.size() - 1 || u < (y.size() - 1)) {
						
						avg += y.get(u);
						count++;
					}
				}

				//sotre the averages
				average[i] = use.format(avg / count);
			}
			for (int num = 0; num < x.size(); num++) {
				double x_val = x.get(num);
				double y_val = average[num];
				build.append(x.get(num)).append(", ").append(average[num]).append("\n");
				points.add(x_val, y_val);
			}

			write.write(build.toString());
			System.out.println("Successfully smooth the data to " + smoothed);
			scan.close();
			write.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found " + fileName);
		} catch (IOException e) {
			System.out.println("An error occured somewhere.");
		}

		main_system graph = new main_system("Cos(x) graph", points);
        graph.setSize(800, 600);
        graph.setLocationRelativeTo(null);
        graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        graph.setVisible(true);
	}
}
