import java.util.*;

public class Main {
	public static void main(String[] args) {
		
		int[] n = {10, 10, 2};
		int[][] c = { {0,0,0},  {1,0,0}, {1, 0, 1}};
		int directions = c.length;

		
		// Define a domain
		Domain domain = new Domain(n);
		
		// Assign gi values in all the k directions
		Point.assignRandomPoints(domain, n, directions);
		printPoints(domain, n);
		
		// Apply streaming function
		for (int i=0; i<10; i++){
			domain.stream(directions, c);
			printPoints(domain, n);
			// Testing  sigmasigmagi = constant
			System.out.println("sigmasigma gi = " + domain.sigmaG(directions) + "\n");
		}
		
	}
	
	public static void printPoints(Domain domain, int[] n){
		Point[][][] points = domain.points;
		for (int j=0; j<n[0]; j++){
			for (int i=0; i< n[1]; i++){
				System.out.print(points[j][i][0].f[2] + " ");
			}
			System.out.println("");
		}
		System.out.print("\n");
	}
	
	
}
