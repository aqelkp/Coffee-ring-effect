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
	
	
			
	public static void main(String[] args) {
		
		// Define a domain
		Domain domain = new Domain();
		int[] n = domain.n;
		// Initial Condition for the domain
//		domain.definePlanarDroplet();
		domain.defineCube(n[0]/2);
//		domain.defineSeperatedSystem();
//		domain.defineRandomSystem();
//		domain.testDomain();
		
		// For plotting tanh curve
		double[] xaxis = new double[n[1]];
		double[] yaxis = new double[n[1]];
		for ( int i=0; i< n[1]; i++) xaxis[i] = i;
		
		// Apply streaming function
		for (int i=0; i<=100000000; i++){
			if (i % 10000 == 0) displayResults(domain, i, xaxis, yaxis);	
			
			LBSimulation(domain, i);
//			methodOfLines(domain, i);
			
		}
			
	}

	private static void displayResults(Domain domain, int i, double[] xaxis, double[] yaxis) {
		// TODO Auto-generated method stub
//		printPoints(domain);
//		DataVisuals.plotBoundaryPhi(domain, xaxis, yaxis, i);
		System.out.println("at t = " + i + " sigma phi " +  domain.sigmaG());
		domain.savePhi("" + i);
		Plot.define(domain, "Domain_at_t=" + i);
//		
	}

	private static void methodOfLines(Domain domain, int i) {
		
		domain.solidWallBC();
		domain.findNu();
//		domain.findBoundaryNu();
		domain.findPhiMethodOfLines(i);
		 
	}

	private static void LBSimulation(Domain domain, int i) {
		domain.findNu();
//		domain.findBoundaryNu();
		domain.findGeq();
		domain.stream();
		domain.findPhiLBM(i);
		domain.solidWallBC();
//		domain.evaporate();
	}

	
	public static void printPoints(Domain domain){
		int[] n = domain.n;
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
