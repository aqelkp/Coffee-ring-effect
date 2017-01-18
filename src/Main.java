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
		
		double[] xaxis = new double[n[1]];
		double[] yaxis = new double[n[1]];
		for ( int i=0; i< n[1]; i++) xaxis[i] = i;
		
		// Apply streaming function
		for (int i=0; i<=10000000; i++){
//			if (i % 100 == 0) printPoints(domain, n);
//			if (i % 10000 == 0) DataVisuals.plotBoundaryPhi(domain, n, xaxis, yaxis, i);
			if (i % 10000 == 0) System.out.println(domain.sigmaG());
			
//			LBSimulation(domain);
			methodOfLines(domain);
			
//			if (i%100 == 0 ) domain.savePhi("" + i);
//			Point.read("" + i);

			if (i%10000 == 0 ) Plot.define(domain, "Domain_at_t=" + i);
			
		}
		
	}

	private static void methodOfLines(Domain domain) {
		
		domain.findNu();
		domain.findPhiMethodOfLines();
		
	}

	private static void LBSimulation(Domain domain) {
		domain.findNu();
		domain.findGeq();
		domain.stream(cMatrix);
		domain.findPhiLBM();
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
