import java.io.IOException;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;

public class DataVisuals {

	public static void plotBoundaryPhi(Domain domain,  int i) {
		// TODO Auto-generated method stub
		// For plotting tanh curve
		double[] xaxis = new double[domain.n[0]];
		double[] yaxis = new double[domain.n[0]];
		for ( int it=0; it< domain.n[0]; it++) xaxis[it] = it;
				
		findYAxis(domain, domain.n, yaxis);
		XYChart chart = QuickChart.getChart("Phi v/s x at t = " + i, "x", "phi", "phi(x)", xaxis, yaxis);
		
		findTanh(domain, domain.n, yaxis);
		chart.addSeries("tanh(r/epsilon)", xaxis, yaxis);
		
		new SwingWrapper(chart).displayChart();
		
		try {
			BitmapEncoder.saveBitmap(chart, "images/phi_vs_x_at_t=" + i, BitmapFormat.PNG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void findTanh(Domain domain, int[] n, double[] yaxis) {
//		double error = 0;
		int center = n[0]/2;
		int r = center;
		for (int i =0; i<n[0]; i++){
			
			if (i<center){
				r = (center - i);
			}else {
				r = (center - i - 1);
			}
//			if ( i == domain.n[0]/2-5){
//				error += Math.abs( yaxis[i] - Math.tanh(r/(2 * Domain.epsilon))  );
//				System.out.print( "phi at x= " + (domain.n[0]/2-5) + " "  + yaxis[i] + " tanh " +  Math.tanh(r/(2 * Domain.epsilon)));
//			}
			yaxis[i] =   Math.tanh(r/(2 * domain.epsilon) );
		}
//		System.out.print(" error " + error);
	}

	public static void findYAxis(Domain domain, int[] n, double[] yaxis) {
		for (int i =0; i<n[0]; i++){
			yaxis[i] = domain.phi[i][0][0];
		}
	}

}
