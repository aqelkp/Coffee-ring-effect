
public class Point {

	int directions;
	double[] g;
	double[] geq;
	
	// Phi - Probabilitic distribution - sigma gi
	double phi;
		
	// nu - Chemical potential
	double nu;
	
	// TODO Delete this if temp profile is not required
	// For temperature profile
	double T;
	
	Point (int directions, double[] g){
		this.directions = directions;
		this.g = g;
		geq = new double[directions];
	}

	public static void assignRandomPoints(Domain domain, int[] n, int directions) {
		
		int value = 0;
		for (int i=0; i<n[0]; i++){
			for (int j = 0; j<n[1]; j++){
				for (int m=0; m<n[2]; m++){
					double[] g = new double[directions];
					for (int k =0; k< directions; k++){
//						g[k] = Math.random()/1000.0;
						g[k] = 0.0;
						value++;
					}
					domain.points[i][j][m] = new Point(directions, g);
				}
			}
		}
		
	}

	public static void defineCube(Domain domain, int[] n, int width ) {
		
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					double[] g = new double[domain.directions];
					domain.points[i][j][k] = new Point(domain.directions, g);
					
					if ( (i > n[0]/2 - width/2) && (i < n[0]/2 + width/2) && 
							(j > n[1]/2 - width/2) && (j < n[1]/2 + width/2) 
							&& (k > n[2]/2 - width/2) && (k < n[2]/2 + width/2) 
							){
						domain.points[i][j][k].phi = 1;
					}
					else domain.points[i][j][k].phi = -1;
				}
		
	}
}
