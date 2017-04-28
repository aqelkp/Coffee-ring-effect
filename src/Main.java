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
import util.BlankSlate;

public class Main {
	
	static int numPoints;
	static String file = "data96.tmp";
	
			
	public static void main(String[] args) {
		numPoints = DataDecoder.readFile(file).length;
		// Define a domain
		Domain domain = new Domain(2,numPoints);
		domain.addSolidWall();
		
		initializeDroplet(domain);
		
		domain.phi = DataDecoder.readFile(file);
		
		System.out.print("t Contact_Diameter\t Drop_Height\t\t Slope\t\t Desired_Angle");
		runSimulation(domain);
		
		domain.phi = DataDecoder.readFile(file);
		domain.a = domain.a/2; domain.K = domain.K * 2; domain.b = -domain.a;
		runSimulation(domain);
		
		domain.phi = DataDecoder.readFile(file);
		domain.a = domain.a/2; domain.K = domain.K * 2; domain.b = -domain.a;
		runSimulation(domain);
		
		domain.phi = DataDecoder.readFile(file);
		domain.a = domain.a/2; domain.K = domain.K * 2; domain.b = -domain.a;
		runSimulation(domain);
		
		domain.phi = DataDecoder.readFile(file);
		domain.a = domain.a*8; domain.K = domain.K /8; domain.b = -domain.a;
		domain.a = domain.a*2; domain.K = domain.K / 2; domain.b = -domain.a;
		runSimulation(domain);
		
		domain.phi = DataDecoder.readFile(file);
		domain.a = domain.a*2; domain.K = domain.K / 2; domain.b = -domain.a;
		runSimulation(domain);
		
		
		
//		for (int i=0; i<7; i++){
//			NewThread thread = new NewThread(numPoints, 80 + i * 3);
//			thread.start();
//		}
				
		
	}

	

	public static void runSimulation(Domain domain) {
		
		double angle = 0.123445678;
		for (int i=0; i<=5000000; i++){
			if (i % 5000000 == 0) displayResults(domain, i);	
			if (i % 10000 == 0) {
				double tempAngle = Math.floor(domain.findContactAngle(false) * 10000000) / 10000000;
				if (angle == tempAngle ) {
					displayResults(domain, i);
					break;
				}
				else angle = tempAngle;
			}
			
			
			
			LBSimulation(domain, i);
//			methodOfLines(domain, i);
		
//			if ( domain.findContactAngle(false) <= 0 ){
//				System.out.println("\n" + i + " 0 0 0 0 " );
//				break;
//			}
		}
		System.out.print("a " + domain.a +" b " + domain.b + " k " + domain.K + " ");
		
	}

	public static void LBSimulation(Domain domain, int i) {
		
		domain.findNu();
		domain.findBoundaryNu();
		domain.findGeq();
		domain.stream();
		domain.findPhiLBM(i);
		if (domain.isSolidWall) domain.solidWallBC(i);
//		domain.contactAngleHysterisis();
//		domain.evaporate();
//		domain.condensate();
	}

	private static void displayResults(Domain domain, int i) {
		System.out.print("\n" + i + " ");
		double angle = domain.findContactAngle(true);
//		printPoints(domain);
//		DataVisuals.plotBoundaryPhi(domain, i);
//		System.out.print("sigma phi = " +  domain.sigmaG() + " ");
//		domain.savePhi(((int) (angle * 180 / Math.PI)) + "contactangle" + i);
//		Plot.define(domain, "Domain_at_t=" + i);
//		findHeight(domain);
//		Image.createImage(domain,i);
//		domain.findContactAngleBC(true);
		
	}

	private static void methodOfLines(Domain domain, int i) {
		
//		domain.findCenter();
		domain.findNu();
//		domain.findBoundaryNu();
		domain.findPhiMethodOfLines(i);
		if (domain.isSolidWall) domain.solidWallBC(i);
		domain.contactAngleHysterisis();
		domain.evaporate();
//		domain.condensate();
		
	}
		
	
	
	private static void findHeight(Domain domain) {
		// TODO Auto-generated method stub
		for (int i=0; i<domain.numPoints; i++){
			if (domain.phi[0][i][0] >= 0 ){
				System.out.println("Height of the interface "  + i);
				return;
			}
		}
	}

	public static void printPoints(Domain domain){
		int[] n = domain.n;
		Point[][][] points = domain.points;
//		for (int j=0; j<n[1]; j++){
		for (int j=n[1]-1; j>=0; j--){
			for (int i=0; i< n[0]; i++){
				System.out.print( new DecimalFormat("#.######").format(domain.phi[i][j][0])  + " ");
				//System.out.print("(" + i + "," + j + ")");
			}
			System.out.println("");
		}
		System.out.print("\n");
		
	}
	
	public static void initializeDroplet(Domain domain){
		 // Initial Condition for the domain
			domain.definePlanarDroplet();
//			domain.defineCube(numPoints/2);	
//			domain.defineOneDimentionalDrop(numPoints/2);
//			domain.defineCube(numPoints/2);
//			domain.defineSeperatedSystem();
//			domain.defineRandomSystem();
//			domain.testDomain();
		}
	
	

}
