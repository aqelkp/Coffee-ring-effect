import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import graphics.DataGraph;

public class Main {
	public static int[] n = {8, 8, 1};
	public static int c = 1;
	
//	// D2Q9 Model
//	public static int[][] cMatrix = { {0,0,0}, {c,0,0}, {0,c,0}, {-c,0,0}, {0,-c,0}, {c,c,0}, {-c,c,0}, {-c,-c,0}, {c,-c,0} };
			
	// D3Q15 Model
	public static int[][] cMatrix = { {0,0,0}, {1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0},{0,0,1},{0,0,-1}, {1,1,1}, {-1,1,1}, 
						{-1,-1,1}, {-1,1,-1}, {-1,-1,-1}, {1,-1,1}, {1,1,-1}, {1,-1,-1} };
			
	public static void main(String[] args) {
		
		for (int num = 0; num < 250; num++){
			
			n[0] = n[0] + 10; 
			n[1] = n[1] + 10;
			
			// Define a domain
			Domain domain = new Domain(n, cMatrix);
			
			// Initial Condition for the domain
//			domain.definePlanarDroplet();
//			domain.defineCube(n[0]/2);
//			domain.defineSeperatedSystem();
			domain.defineRandomSystem();
			
			// For plotting tanh curve
			double[] xaxis = new double[n[1]];
			double[] yaxis = new double[n[1]];
			for ( int i=0; i< n[1]; i++) xaxis[i] = i;
			
			long startTime = System.currentTimeMillis();
			
			// Apply streaming function
			for (int i=0; i<=99; i++){
//				if (i % 1000 == 0) displayResults(domain, i, xaxis, yaxis);	
				
				LBSimulation(domain);
//				methodOfLines(domain);
				
			}
			
			System.out.println( num + ": Time taken for " + (n[0]*n[1]) + " points = " + (System.currentTimeMillis() - startTime));
		}
		
		System.out.println("\n \n Method of lines \n \n");
		
for (int num = 0; num < 250; num++){
			
			n[0] = n[0] + 10; 
			n[1] = n[1] + 10;
			
			// Define a domain
			Domain domain = new Domain(n, cMatrix);
			
			// Initial Condition for the domain
//			domain.definePlanarDroplet();
//			domain.defineCube(n[0]/2);
//			domain.defineSeperatedSystem();
			domain.defineRandomSystem();
			
			// For plotting tanh curve
			double[] xaxis = new double[n[1]];
			double[] yaxis = new double[n[1]];
			for ( int i=0; i< n[1]; i++) xaxis[i] = i;
			
			long startTime = System.currentTimeMillis();
			
			// Apply streaming function
			for (int i=0; i<=99; i++){
//				if (i % 1000 == 0) displayResults(domain, i, xaxis, yaxis);	
				
//				LBSimulation(domain);
				methodOfLines(domain);
				
			}
			
			System.out.println( num + ": Time taken for " + (n[0]*n[1]) + " points = " + (System.currentTimeMillis() - startTime));
		}
		
		
		
	}

	private static void displayResults(Domain domain, int i, double[] xaxis, double[] yaxis) {
		// TODO Auto-generated method stub
		printPoints(domain, n);
//		DataVisuals.plotBoundaryPhi(domain, n, xaxis, yaxis, i);
//		System.out.println(domain.sigmaG());
//		domain.savePhi("" + i);
//		Plot.define(domain, "Domain_at_t=" + i);
//		
	}

	private static void methodOfLines(Domain domain) {
		
		domain.findNu();
		domain.findBoundaryNu();
		domain.findPhiMethodOfLines();
		domain.applySolidWallBC();
		
		
	}

	private static void LBSimulation(Domain domain) {
		domain.findNu();
		domain.findBoundaryNu();
		domain.findGeq();
		domain.stream();
		domain.findPhiLBM();
		domain.applySolidWallBC();
//		domain.evaporate();
	}

	
	public static void printPoints(Domain domain, int[] n){
		Point[][][] points = domain.points;
		for (int j=0; j<n[1]; j++){
			for (int i=0; i< n[0]; i++){
				System.out.print( new DecimalFormat("#.######").format(domain.phi[i][j][n[2]/2])  + " ");
				//System.out.print("(" + i + "," + j + ")");
			}
			System.out.println("");
		}
		System.out.print("\n");
		
	}
	
	
}
