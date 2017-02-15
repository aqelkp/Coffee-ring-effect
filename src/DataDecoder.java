import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class DataDecoder {

	public static void main(String[] args) {
		
		Domain domain = new Domain();
		int max = 4800000;
		int stepSize = 100000;
		for (int i =0; i<=max; i=i+stepSize){
			domain.phi = read("" + i );
			Plot.define(domain, "Domain_at_t=" + i);
		}
		
//		XYChart chart = QuickChart.getChart("sigma phi vs t", "t", "sigma phi", "phi(x)", x, y);
//		new SwingWrapper(chart).displayChart();
		
	}
	
	public static double[][][] read(String name) {
		// read the object from file
        // save the object to file
        FileInputStream fis = null;
        ObjectInputStream in = null;
        double[][][] points;
        
        try {	
        		
                fis = new FileInputStream("dataphi/t" + name + ".tmp");
                in = new ObjectInputStream(fis);
                points = (double[][][]) in.readObject();
                in.close();
        } catch (Exception ex) {
        		points = null;
                ex.printStackTrace();
        }
        return points;
        
	}

}
