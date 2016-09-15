import java.text.DecimalFormat;

public class Main {
	public static void main(String[] args) {
		
		evaporation();
		
	}

	private static void evaporation() {
		
		int[] n = {2, 2, 2};
		
//		// D2Q9 Model
//		int[][] c = { {0,0,0}, {1,0,0}, {0,1,0}, {-1,0,0}, {0,-1,0}, {1,1,0}, {-1,1,0}, {-1,-1,0}, {1,-1,0} };
//		
//		D3Q15 Model
		int[][] c = { {0,0,0}, {1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0},{0,0,1},{0,0,-1}, {1,1,1}, {-1,1,1}, 
					{-1,-1,1}, {-1,1,-1}, {-1,-1,-1}, {1,-1,1}, {1,1,-1}, {1,-1,-1} };
		
		int directions = c.length;

		// Define a domain
		Domain domain = new Domain(n, c);
		
		// Assign gi values in all the k directions
		Point.assignRandomPoints(domain, n, directions);
//		Point.defineCube(domain, n, 20);
		
		// Initial condition of phi = 0 at all points
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					double sign = 1.0;
					if (Math.random() < 0.5 ) sign = -1.0;
					domain.points[i][j][k].phi  = -1;
					
					domain.points[i][j][k].phi  = Math.random() * sign / 100.0;
//					domain.points[i][j][k].phi = Double.parseDouble(String.format("%.8f",Math.random() * sign / 100.0 ));
//					if (i<n[0]/2) domain.points[i][j][k].phi = 0.9999;
//					else domain.points[i][j][k].phi = -0.9999;
				}
			
		
		
		// Apply streaming function
		for (int i=0; i<15; i++){
			
			printPoints(domain, n);
			domain.findNu();
			domain.findGeq();
			domain.stream(directions, c);
			domain.findPhi();
			
			if (i%10 == 0 ) Plot.define(domain);
//			System.out.println("sigma g = " + domain.sigmaG());
//			System.out.println(i);
			
		}
		
	}

	public static void printPoints(Domain domain, int[] n){
		Point[][][] points = domain.points;
		for (int j=0; j<n[1]; j++){
			for (int i=0; i< n[0]; i++){
				System.out.print( new DecimalFormat("#.######").format(points[i][j][0].phi)  + " ");
//				System.out.print( points[i][j][0].phi  + " ");
			}
			System.out.println("");
		}
		System.out.print("\n");
		
	}
	
	
}
