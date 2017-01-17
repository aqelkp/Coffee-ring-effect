import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Point implements Serializable{

	int directions;
	double[] g;
	double[] geq;
	
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
