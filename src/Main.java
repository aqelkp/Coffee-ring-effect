import java.io.IOException;
import java.text.DecimalFormat;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class Main {
	public static int[] n = {750, 750, 10};
	public static int c = 1;
	
	// D2Q9 Model
	public static int[][] cMatrix = { {0,0,0}, {c,0,0}, {0,c,0}, {-c,0,0}, {0,-c,0}, {c,c,0}, {-c,c,0}, {-c,-c,0}, {c,-c,0} };
			
//	// D3Q15 Model
	// Order to be changed with index
//	public static int[][] cMatrix = { {0,0,0}, {1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0},{0,0,1},{0,0,-1}, {1,1,1}, {-1,1,1}, 
//						{-1,-1,1}, {-1,1,-1}, {-1,-1,-1}, {1,-1,1}, {1,1,-1}, {1,-1,-1} };
			
	public static void main(String[] args) {
		
		Runtime rt = Runtime.getRuntime();
		long total = rt.totalMemory();
        long free = rt.freeMemory();
		System.out.println("Total memory " + total + ", Free memory " + free);
		
		// Define a domain
		Domain domain = new Domain(n, cMatrix);
		domain.defineCube(n[0]/2);
//		domain.defineSeperatedSystem();
//		domain.defineRandomSystem();
		
		double[] xaxis = new double[n[1]];
		double[] yaxis = new double[n[1]];
		for ( int i=0; i< n[1]; i++) xaxis[i] = i;
		
//		for (int i=0; i<=0; i++){
//			if (i % 100000 == 0) displayResults(domain, i, xaxis, yaxis);	
//			
//			LBSimulation(domain);
////			methodOfLines(domain);
//			
//		}
		
		System.out.println("Total memory " + total + ", Free memory " + free);
		
	}

	private static void displayResults(Domain domain, int i, double[] xaxis, double[] yaxis) {
		// TODO Auto-generated method stub
//		printPoints(domain, n);
//		DataVisuals.plotBoundaryPhi(domain, n, xaxis, yaxis, i);
		System.out.println(domain.sigmaG());
//		domain.savePhi("" + i);
		Plot.define(domain, "Domain_at_t=" + i);
//		
	}

	private static void methodOfLines(Domain domain) {
		
		domain.findNu();
		domain.findBoundaryNu();
		domain.findPhiMethodOfLines();
//		domain.applySolidWallBC();
		
		
	}

	private static void LBSimulation(Domain domain) {
		domain.findNu();
		domain.findBoundaryNu();
		domain.findGeq();
		domain.stream();
		domain.findPhiLBM();
		domain.applySolidWallBC();
	}

	
	public static void printPoints(Domain domain, int[] n){
		Point[][][] points = domain.points;
		for (int j=0; j<n[1]; j++){
			for (int i=0; i< n[0]; i++){
				System.out.print( new DecimalFormat("#.######").format(domain.points[i][j][0].g[5])  + " ");
				//System.out.print("(" + i + "," + j + ")");
			}
			System.out.println("");
		}
		System.out.print("\n");
		
	}
	
	
}
