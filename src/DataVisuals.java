import java.io.IOException;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;

public class DataVisuals {

	public static void plotBoundaryPhi(Domain domain, int[] n, double[] xaxis, double[] yaxis, int i) {
		// TODO Auto-generated method stub
		findYAxis(domain, n, yaxis);
		XYChart chart = QuickChart.getChart("Phi v/s r at t = " + i, "r", "phi", "phi(x)", xaxis, yaxis);
		
		findTanh(domain, n, yaxis);
		chart.addSeries("tanh(r/epsilon)", xaxis, yaxis);
		
		new SwingWrapper(chart).displayChart();
		
		try {
			BitmapEncoder.saveBitmap(chart, "images/Phi_vs_r_at_t=" + i, BitmapFormat.PNG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void findTanh(Domain domain, int[] n, double[] yaxis) {
		int center = n[1]/2;
		int r = center;
		for (int i =0; i<n[1]; i++){
			
			if (i<center){
				r = (center - i);
			}else {
				r = (center - i - 1);
			}
			yaxis[i] =   Math.tanh(r/(2 * Domain.epsilon) );
		}
	}

	public static void findYAxis(Domain domain, int[] n, double[] yaxis) {
		int center = n[1]/2;
		for (int i =0; i<n[0]; i++){
			yaxis[i] = domain.phi[i][center][0];
		}
	}

}
