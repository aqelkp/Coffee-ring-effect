package graphics;

import java.io.IOException;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class DataGraph {
	
	XYChart chart;
	DataGraph(String title, String xTitle, String yTitle, String seriesName, double[] xData, double[] yData){
		chart = QuickChart.getChart(title, xTitle, yTitle, seriesName, xData, yData);
	}
	
	public DataGraph(String title, String xTitle, String yTitle, String seriesName, List<Double> xData, List<Double> yData){
		chart = QuickChart.getChart(title, xTitle, yTitle, seriesName, xData, yData);
	}

	
	public void display(){
		new SwingWrapper(chart).displayChart();
	}
	
	public void saveToFile(String fileName){
		try {
			BitmapEncoder.saveBitmap(chart, fileName, BitmapFormat.PNG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
