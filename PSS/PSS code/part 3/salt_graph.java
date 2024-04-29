package final_project_part_3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import Project_part1.mainSystem;
import javax.swing.*;

import org.jfree.data.xy.XYSeries;

//Since this is just the same as part 1 but with graph, so I just copy and paste it
//I also don't know if we only need to graph or also store data
//So I did both
public class salt_graph {

	public static void main(String[] args) {

		// Initialize variables and parameters as needed
		mainSystem use = new mainSystem();
        XYSeries points = new XYSeries("Cos(x) graph");
		String name;
		String line;
		StringBuilder build = new StringBuilder();
		String[] list = new String[1];
		Scanner in = new Scanner(System.in);
		double x, salt;

		// Enter name of file and correct the format
		System.out.println("Enter file name");
		
		name = use.askName();
		
		String nameSalt = name.replace(".csv", "_salted.csv");

		// Asking for the range of salting
		System.out.println("Enter a number for the salting range:");
		double range = Math.abs(in.nextDouble());

		// try and catch
		try {
			BufferedReader br = use.readFile(name);

			BufferedWriter write = use.writeFile(nameSalt);

			// Since I have a header, I need to skip first line
			// Probably could've been more efficient.
			boolean firstLine = true;

			while ((line = br.readLine()) != null) {

				list = line.split(",");

				if (firstLine) {
					build.append(line).append("\n");
					firstLine = false;
					continue;
				}
				salt = Double.parseDouble(list[1].trim());

				// Salting the result (y values)
				salt = use.salting(salt, range);
				x = Double.parseDouble(list[0].trim());
				x = use.format(x);
				
				salt = use.format(salt);
				build.append(x).append(", ").append(salt).append("\n");
				points.add(x, salt);

			}

			write.write(build.toString());
			System.out.println("Sucessfully stored salted data in " + nameSalt);
			write.close();
			br.close();
		}

		catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("An error occured during the creation of salted file");
		}
		in.close();

		main_system graph = new main_system("Cos(x) graph", points);
        graph.setSize(800, 600);
        graph.setLocationRelativeTo(null);
        graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        graph.setVisible(true);
	}
}
