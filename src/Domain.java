import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Domain {

	int dimension, directions;
	
	// Number of points in each dimension
	int[] n;
	
	// Points in the plane
	Point[][][] points;
	double[][][] phi;
	int[][] c;
	
	int delX = 1, delY = 1, delZ = 1;
	
	// Simulation parameters from the paper
	// Lattice - Boltzmann simulation of droplet evaporation
	public static double a = -0.00305*0.25, b=-a, K = 0.0078;
	public static double epsilon = Math.sqrt(-K/(2*a));
	int M = 10, rho = 1, delt = 1;
	double towG = 1;
//	double towG = 0.78868;
	
	// Defining a domain
	public Domain(int[] n, int[][] c){
		this.dimension = n.length;
		this.n = n;
		this.c = c;
		this.directions = c.length;
		points = new Point[n[0]][n[1]][n[2]];
		phi = new double[n[0]][n[1]][n[2]];
		for (int i=0; i<n[0]; i++){
			for (int j = 0; j<n[1]; j++){
				for (int m=0; m<n[2]; m++){
					double[] g = new double[directions];
					for (int k =0; k< directions; k++){
						g[k] = 0.0;
					}
					points[i][j][m] = new Point(directions, g);
				}
			}
		}

	}
	
	// Apply streaming function
	void stream(int[][] c){
		
		for (int i=0; i<directions; i++){
			
			// Take out the g value in each direction and make it into a 1-D array
			// For temp saving values
			double[][][] tempf = new double[n[0]][n[1]][n[2]];
			for (int j=0; j<n[0]; j++){
				for (int k=0; k<n[1]; k++) {
					for (int m=0; m<n[2]; m++){
						tempf[j][k][m] = points[j][k][m].g[i];
					}
				}
			}
			
			// Apply streaming to each of the point
			for (int j=0; j<n[0]; j++){
				for (int k=0; k<n[1]; k++){
					for (int m=0; m<n[2]; m++){
						int x = j + c[i][0] * delt;
						int y = k + c[i][1] * delt;
						int z = m + c[i][2] * delt;
						
						// Periodic boundary condition 
						if (x >= n[0]) x =- n[0];
						if (x < 0 ) x+= n[0];
						
						if (y >= n[1]) y =- n[1];
						if (y < 0 ) y += n[1];
						
						if (z >= n[2]) z =- n[2];
						if (z < 0) z += n[2];
						
//						points[x][y][z].g[i] = tempf[j][k][m];
						
						points[x][y][z].g[i] = tempf[j][k][m] - (delt/towG) * 1.0 *
								(tempf[j][k][m] - points[j][k][m].geq[i]) ;
					}
				}
				
			}
			
		}
	}

	// ΣΣgi
	double sigmaG(){
		double sum = 0;
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1];j++)
				for (int m=0; m<n[2]; m++)
					sum += phi[i][j][m];
		return sum;
	}

	public void findNu() {
		
		
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					
					// Applying Landau model and 2nd order central finite
					// difference discretization
					
					// Checking for boundaries 
					double phiPoint =  phi[i][j][k];
					double nuPoint = a * phiPoint + b * phiPoint * phiPoint * phiPoint;
					
					// Applying discretization
					nuPoint -= (K / (delX * delX)) * 
							(phi[ (i + delX < n[0]) ? i + delX : (i + delX - n[0]) ][j][k] 
									- 2 * phiPoint + 
									phi[ (i- delX >= 0) ? i - delX : (i - delX + n[0])][j][k]);
					
					nuPoint -= (K / (delY * delY)) * 
							( phi[i][ (j + delY < n[1]) ? j + delY : (j + delY - n[1]) ][k] 
									- 2 * phi[i][j][k] + 
									phi[i][ (j- delY >= 0) ? j - delY : (j - delY + n[1])][k] );
					
					nuPoint -= (K / (delZ * delZ)) * 
							(phi[i][j][(k + delZ < n[2]) ? k + delZ : (k + delZ - n[2]) ] 
									- 2 * phi[i][j][k] + 
									phi[i][j][ (k- delZ >= 0) ? k - delZ : (k - delZ + n[2])]);
					
					points[i][j][k].nu = nuPoint;

					
				}
		
	}

	public void findGeq() {
		
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					
					Point point = points[i][j][k];
					
					// D2Q9 Model weighing factors
					for (int m=0; m<directions; m++){
						if (m==0){
							point.geq[m] = phi[i][j][k] - 1.1547 * point.nu;
						}else if (m<5) {
							point.geq[m] = 0.23094 * point.nu;
						}else if (m<10){
							point.geq[m] = 0.057735 * point.nu;
						}
					}
					
//					//D3Q15 Model weighing factors
//					// Assign A value according to directions
//					for (int h =0; h<directions; h++){
//						if (h==0){
//							A = 4.5 * point.phi - (3.5 * 3 * M * point.nu);
//							omega = 2.0/9;
//						} else if (h < 7){
//							A = (1/rho) * 3 * M * point.nu;
//							omega = 1.0/9;
//						} else if (h < 16){
//							A = (1/rho) * 3 * M * point.nu;
//							omega = 1.0/72;
//						}
//						point.geq[h] = rho *  omega * A;
//						
//					}	
					
				}
		
	}

	public void findPhi() {
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					
					Point point = points[i][j][k];	
					double phiPoint = 0;
					for (int h =0; h<directions; h++){
						phiPoint += point.g[h];
					}
					phi[i][j][k] = phiPoint;
				}
	}

	public void makeAverage() {
		double sum = 0;
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					sum += Math.abs(phi[i][j][k]);
				}
		double avg = sum / (n[0] * n[1] * n[2]);
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++)
					phi[i][j][k] = phi[i][j][k]/avg;
		// TODO Auto-generated method stub
		
	}

	public void findParams() {
		
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					
					// Applying Landau model and 2nd order central finite
					// difference discretization
					
					// Checking for boundaries 
					double phiPoint =  phi[i][j][k];
					double nuPoint = a * phiPoint + b * phiPoint * phiPoint * phiPoint;
					
					// Applying discretization
					nuPoint -= (K / (delX * delX)) * 
							(phi[ (i + delX < n[0]) ? i + delX : (i + delX - n[0]) ][j][k] 
									- 2 * phiPoint + 
									phi[ (i- delX >= 0) ? i - delX : (i - delX + n[0])][j][k]);
					
					nuPoint -= (K / (delY * delY)) * 
							( phi[i][ (j + delY < n[1]) ? j + delY : (j + delY - n[1]) ][k] 
									- 2 * phi[i][j][k] + 
									phi[i][ (j- delY >= 0) ? j - delY : (j - delY + n[1])][k] );
					
					nuPoint -= (K / (delZ * delZ)) * 
							(phi[i][j][(k + delZ < n[2]) ? k + delZ : (k + delZ - n[2]) ] 
									- 2 * phi[i][j][k] + 
									phi[i][j][ (k- delZ >= 0) ? k - delZ : (k - delZ + n[2])]);
					
					points[i][j][k].nu = nuPoint;

					// Geqb
					Point point = points[i][j][k];
					double A = 0, omega =0;
					
					// Assign A value accoring to directions
					for (int h =0; h<directions; h++){
						if (h==0){
							A = 4.5 - (3.5 * 3 * M * point.nu);
							omega = 2.0/9;
						} else if (h < 7){
							A = (1/rho) * 3 * M * point.nu;
							omega = 1.0/9;
						} else if (h < 16){
							A = (1/rho) * 3 * M * point.nu;
							omega = 1.0/72;
						}
						point.geq[h] = phi[i][j][k] * omega * A;
						
					}	
					
					double phiPoint1 = 0;
					for (int h =0; h<directions; h++){
						phiPoint1 += point.g[h];
					}
					phi[i][j][k] = phiPoint1;
					
				}
	}

	public void defineSeperatedSystem() {
		
		// Initial condition of phi = 0 at all points
				for (int i=0; i<n[0]; i++)
					for (int j=0; j<n[1]; j++)
						for (int k=0; k<n[2]; k++){
							// For already seperated system
							if (i<n[0]/2) phi[i][j][k] = 1.0;
							else phi[i][j][k] = -1;
							
						}
		
	}
	
	public void defineRandomSystem() {
			
			// Initial condition of phi = 0 at all points
					for (int i=0; i<n[0]; i++)
						for (int j=0; j<n[1]; j++)
							for (int k=0; k<n[2]; k++){
								// For randomized system
								double sign = 1.0;
								if (Math.random() < 0.5 ) sign = -1.0;
								phi[i][j][k]  = Math.random() * sign / 100.0;
															
							}
			
	}
	
	void defineCube(int width ) {
			
			for (int i=0; i<n[0]; i++)
				for (int j=0; j<n[1]; j++)
					for (int k=0; k<n[2]; k++){
						if ( (i > n[0]/2 - width/2) && (i < n[0]/2 + width/2) && 
								(j > n[1]/2 - width/2) && (j < n[1]/2 + width/2) 
								&& (k > n[2]/2 - width/2) && (k < n[2]/2 + width/2) 
								){
							phi[i][j][k] = 1;
						}
						else phi[i][j][k] = -1;
					}
			
	}

	public void savePhi(String name) {
		try {
			FileOutputStream fos = new FileOutputStream("dataphi/t" + name +".tmp");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(phi);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}

	
