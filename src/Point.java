
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
	
}
