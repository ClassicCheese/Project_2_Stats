package final_project_part_3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import Project_part1.mainSystem;
import javax.swing.*;

import org.jfree.data.xy.XYSeries;

//Since this is just the same as part 1 but with graph, so I just copy and paste it
//I also don't know if we only need to graph or also store data
//So I did both
public class plot_graph {

	public static void main(String[] args) {
		mainSystem use = new mainSystem();
        XYSeries points = new XYSeries("Cos(x) graph");
        StringBuilder text = new StringBuilder();
        String fileName;
        boolean isRad = false;
        double insurance = 0.000001; // To cover the entire range

        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Cos(x) function calculation of integers\n");

            // Ask for file name and create BufferedWriter
            System.out.println("Enter file name to store data");
            fileName = in.nextLine();
            BufferedWriter write = use.writeFile(fileName);
            write.write("x, cos(x)\n");

            // Ask for radians or angle
            System.out.println("Are the values \n(1)radians \n(2)angle?");
            int val = in.nextInt();
            if (val == 1) {
                isRad = true;
            }

            // Get bounds
            Double[] bounds = use.getBounds();
            double low = bounds[0];
            double upper = bounds[1];
            double inc = bounds[2];

            // Loop through the range and calculate cos(x)
            while (low <= upper + insurance) {
                double x = use.format(low);
                double y = use.getArea(low, upper, isRad);
                text.append(x).append(", ").append(y).append("\n");
                points.add(x, y);
                low += inc;
            }
            write.write(text.toString());
            System.out.println("Data has been stored in: " + fileName);
            write.close();
            in.close();

            // Write the data to designated file
            new main_system("Line Graph Example", points);

        } catch (InputMismatchException e) {
            System.out.println("You have entered a non-number variable");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        main_system graph = new main_system("Cos(x) graph", points);
        graph.setSize(800, 600);
        graph.setLocationRelativeTo(null);
        graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        graph.setVisible(true);
    }
}