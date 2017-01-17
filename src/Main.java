import java.io.IOException;
import java.text.DecimalFormat;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class Main {
	public static int[] n = {50, 50, 1};
	public static int c = 1;
	
	// D2Q9 Model
	public static int[][] cMatrix = { {0,0,0}, {c,0,0}, {0,c,0}, {-c,0,0}, {0,-c,0}, {c,c,0}, {-c,c,0}, {-c,-c,0}, {c,-c,0} };
			
//	// D3Q15 Model
//	public static int[][] cMatrix = { {0,0,0}, {1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0},{0,0,1},{0,0,-1}, {1,1,1}, {-1,1,1}, 
//						{-1,-1,1}, {-1,1,-1}, {-1,-1,-1}, {1,-1,1}, {1,1,-1}, {1,-1,-1} };
			
	public static void main(String[] args) {
		
		// Define a domain
		Domain domain = new Domain(n, cMatrix);
		domain.defineCube(15);
//		domain.defineSeperatedSystem();
//		domain.defineRandomSystem();
		
		LBSimulation(domain, n, cMatrix);
		
	}

	private static void LBSimulation(Domain domain, int[] n, int[][] cMatrix) {
		
			
		double[] xaxis = new double[n[1]];
		double[] yaxis = new double[n[1]];
		for ( int i=0; i< n[1]; i++) xaxis[i] = i;
		
		// Apply streaming function
		for (int i=0; i<=10000000; i++){
			if (i % 10000 == 0) System.out.println(domain.sigmaG());
//			if (i % 100 == 0) printPoints(domain, n);
//			if (i % 10000 == 0) plotBoundaryPhi(domain, n, xaxis, yaxis, i);
		
			domain.findNu();
			domain.findGeq();
			domain.stream(cMatrix);
			domain.findPhi();
			
			if (i%10000 == 0 ) domain.savePhi("" + i);
//			Point.read("" + i);

			if (i%10000 == 0 ) Plot.define(domain, "Domain_at_t=" + i);
			
		}
		
	}

	private static void plotBoundaryPhi(Domain domain, int[] n, double[] xaxis, double[] yaxis, int i) {
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

	private static void findTanh(Domain domain, int[] n, double[] yaxis) {
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

	private static void findYAxis(Domain domain, int[] n, double[] yaxis) {
		int center = n[1]/2;
		for (int i =0; i<n[0]; i++){
			yaxis[i] = domain.phi[i][center][0];
		}
	}

	public static void printPoints(Domain domain, int[] n){
		Point[][][] points = domain.points;
		for (int j=0; j<n[1]; j++){
			for (int i=0; i< n[0]; i++){
				System.out.print( new DecimalFormat("#.######").format(domain.phi[i][j][0])  + " ");
			}
			System.out.println("");
		}
		System.out.print("\n");
		
	}
	
	
}
