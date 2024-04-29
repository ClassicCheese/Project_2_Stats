package final_project_part_3;

import java.awt.Dimension;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class main_system extends JFrame {
	
	//Prop to https://www.baeldung.com/jfreechart-visualize-data
	//This construtor get evrythin ready, ready to take in values and graph it
	public main_system(String title, XYSeries points) {
        super(title);

        XYSeriesCollection data_set = new XYSeriesCollection(points);
        JFreeChart chart = ChartFactory.createXYLineChart("Cos(x) graph", "X", "Cos(x)", data_set);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        setContentPane(chartPanel);
    }
	
    }