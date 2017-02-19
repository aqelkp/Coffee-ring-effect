import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Domain {

	
	public int dimension;
	public int numPoints;
	int directions;
	
	public static final int D3Q15 = 3;
	public static final int D2Q9 = 2;
	
//	int model = dimension;
	
	
	// Points in the plane
	Point[][][] points;
	double[][][] phi, nu;
	int[][] c;
	
	
	// Simulation parameters from the paper
	// Lattice - Boltzmann simulation of droplet evaporation
	public static double a = -0.00305, b=-a, K = 0.0078;
	public static double epsilon = Math.sqrt(-K/(2*a));
	public static int rho = 1, delt = 1;
	public static double M = 5;
	double towG = 1;
	double Mbar = 2*M/(2*towG - 1);
	public static double h = 0.00;
//	double towG = 0.78868;
	public static double contactAngle =  Math.PI/3;
	
	// To exclude wall
	int start = 1;
	int[] n;
	public boolean isSolidWall = false;
	
	// Defining a domain
	public Domain(int dimension, int numPoints){
		
		this.dimension = dimension;
		this.numPoints = numPoints;
		
		int[] n = {numPoints, dimension > 1 ? numPoints : 1, dimension > 2 ? numPoints : 1};
		
//		int[] n = {150,50,1}; dimension = 2;
		
		this.n = n;
		points = new Point[n[0]][n[1]][n[2]];
		phi = new double[n[0]][n[1]][n[2]];
		nu = new double[n[0]][n[1]][n[2]];
		defineDimensions();
		defineGMatrices();
		
	}
	
	public void defineDimensions() {
		// TODO Auto-generated method stub
		
		
		if (dimension == 2 ){
			// D2Q9 Model
			int[][] cMatrix = { {0,0,0}, {1,0,0}, {0,1,0}, {-1,0,0}, {0,-1,0}, {1,1,0}, {-1,1,0}, {-1,-1,0}, {1,-1,0} };
			this.c = cMatrix;
		} else if (dimension == 3) {
			// D3Q15 Model
			int[][] cMatrix = { {0,0,0}, {1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0},{0,0,1},{0,0,-1}, {1,1,1}, {-1,1,1}, 
								{-1,-1,1}, {-1,1,-1}, {-1,-1,-1}, {1,-1,1}, {1,1,-1}, {1,-1,-1} };	
			this.c = cMatrix;
		} else if (dimension == 1){
			// D1Q3 Model
			int[][] cMatrix = { {0,0,0}, {1,0,0}, {-1,0,0} };
			this.c = cMatrix;
		}
		this.directions = c.length;

	}

	private void defineGMatrices() {
		// TODO Auto-generated method stub
		for (int i=0; i<n[0]; i++){
			for (int j = 0; j<n[1]; j++){
				for (int m=0; m<n[2]; m++){
					double[] g = new double[directions];
					for (int k =0; k< directions; k++){
						g[k] = 0.0;
//						g[k] = Math.random();
					}
					points[i][j][m] = new Point(directions, g);
				}
			}
		}
		
	}

	// Apply streaming function
	void stream(){
		
//		double[][][][] tempg = new double[directions][n[0]][n[1]][n[2]];
//		
//		for (int i=0; i<directions; i++){
//			// Take out the g value in each direction and make it into a 1-D array
//						// For temp saving values
//						for (int j=0; j<n[0]; j++){
//							for (int k=0; k<n[1]; k++) {
//								for (int m=0; m<n[2]; m++){
//									tempg[i][j][k][m] = points[j][k][m].g[i];
//								}
//							}
//						}
//		}
		
		for (int i=0; i<directions; i++){
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
						
//						points[x][y][z].g[i] = points[j][k][m].geq[i] ;
						
						// Top bounce-back BC
						if (isSolidWall && y == start && (i == 6 || i == 2 || i == 5 ) ){
//							points[x][y][z].g[i] = tempg[i+2][x][y][z];
							points[x][y][z].g[i] = points[x][y][z].geq[i+2];
						// Bottom bounce-back BC
						} else if (isSolidWall && y == n[1] - 1 - start && (i == 8 || i == 4 || i == 7 ) ){
//							points[x][y][z].g[i] = tempg[i-2][x][y][z];
							points[x][y][z].g[i] = points[x][y][z].geq[i-2];
						} else {
//							points[x][y][z].g[i] = tempg[i][j][k][m];
							points[x][y][z].g[i] = points[j][k][m].geq[i] ;
						}
						
						
								
					}
				}
				
				
			}
			
			
			
		}
		
		
		
	}

	// ΣΣgi
	double sigmaG(){
		double sum = 0;
		for (int i=0; i<n[0]; i++)
			for (int j=start; j<n[1]-start;j++)
				for (int k=0; k<n[2]; k++)
//					for (int m = 0; m<directions; m++){
//						sum += points[i][j][k].g[m];
//					}
					sum += phi[i][j][k];
		return sum;
	}

	public void findNu() {
		
		
		for (int i=0; i<n[0]; i++)
			for (int j= start; j<n[1]- start; j++)
				for (int k=0; k<n[2]; k++){
					
					nu[i][j][k] = findNu(phi[i][j][k], i,j,k);
					
				}
		if (isSolidWall)
		// del.nu = 0
				for (int i=0; i<n[0]; i++)
					for (int k =0; k<n[2]; k++){
						int j = n[1]-1;
						
						nu[i][j][k] = nu[i][j-1][k];
						
						nu[i][0][k] = nu[i][1][k];
					}
		
	}

	 double findNu(double phiPoint, int i, int j, int k) {
		// Applying Landau model and 2nd order central finite
		// difference discretization
		
		// Checking for boundaries 
		double nuPoint = a * phiPoint + b * phiPoint * phiPoint * phiPoint;
		
		
		// Applying discretization
		nuPoint -= K * (phi[ (i + 1 < n[0]) ? i + 1 : (i + 1 - n[0]) ][j][k] - 2 * phiPoint + 
						phi[ (i- 1 >= 0) ? i - 1 : (i - 1 + n[0])][j][k]);
		
		
//		if ( j + delY < n[1] && j - delY >= 0)
		nuPoint -= K * ( phi[i][ j + 1 < n[1] ? j + 1 : j + 1 - n[1]][k] 
						- 2 * phiPoint + 
						phi[i][ j - 1 >=0 ? j - 1 : j - 1 + n[1] ][k] );
		
		nuPoint -= K * 
				(phi[i][j][(k + 1 < n[2]) ? k + 1 : (k + 1 - n[2]) ] 
						- 2 * phiPoint + 
						phi[i][j][ (k- 1 >= 0) ? k - 1 : (k - 1 + n[2])]);
		return  nuPoint;
		
	}
	 
	 double findNu(int i, int j, int k) {
		 return nu[i][j][k];
//			// Applying Landau model and 2nd order central finite
//			// difference discretization
//			
//		 	double phiPoint = phi[i][j][k];
//			// Checking for boundaries 
//			double nuPoint = a * phiPoint + b * phiPoint * phiPoint * phiPoint;
//			
//			// Applying discretization
//			nuPoint -= K * (phi[ (i + 1 < n[0]) ? i + 1 : (i + 1 - n[0]) ][j][k] - 2 * phiPoint + 
//							phi[ (i- 1 >= 0) ? i - 1 : (i - 1 + n[0])][j][k]);
//			
////			if ( j + delY < n[1] && j - delY >= 0)
//			nuPoint -= K * ( phi[i][ j + 1 < n[1] ? j + 1 : j + 1 - n[1]][k] 
//							- 2 * phiPoint + 
//							phi[i][ j - 1 >=0 ? j - 1 : j - 1 + n[1] ][k] );
//			
//			nuPoint -= K * 
//					(phi[i][j][(k + 1 < n[2]) ? k + 1 : (k + 1 - n[2]) ] 
//							- 2 * phiPoint + 
//							phi[i][j][ (k- 1 >= 0) ? k - 1 : (k - 1 + n[2])]);
//			
//			return  nuPoint;
			
		}

	public void findGeq() {
		
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					
					Point point = points[i][j][k];
					
					if (dimension == 2){
						
						// D2Q9 Model weighing factors
						for (int m=0; m<directions; m++){
							if (m==0){
								point.geq[m] = phi[i][j][k] - (20.0/12) * Mbar * nu[i][j][k];
							}else if (m<5) {
								point.geq[m] = (4.0/12) * Mbar * nu[i][j][k];
							}else if (m<10){
								point.geq[m] = (1.0/12) * Mbar * nu[i][j][k];
							}
						}
						
////						 D2Q9 Model weighing factors
//						for (int m=0; m<directions; m++){
//							if (m==0){
//								point.geq[m] = phi[i][j][k] - 1.1547 * nu[i][j][k];
//							}else if (m<5) {
//								point.geq[m] = 0.23094 * nu[i][j][k];
//							}else if (m<10){
//								point.geq[m] = 0.057735 * nu[i][j][k];
//							}
//						}
					} else {
						//D3Q15 Model weighing factors
						// Assign A value according to directions
						double A = 0, omega =0;
						for (int h =0; h<directions; h++){
							if (h==0){
								A = 4.5 * phi[i][j][k] - (3.5 * 3 * Mbar * nu[i][j][k]);
								omega = 2.0/9;
							} else if (h < 7){
								A = (1/rho) * 3 * Mbar * nu[i][j][k];
								omega = 1.0/9;
							} else if (h < 16){
								A = (1/rho) * 3 * Mbar * nu[i][j][k];
								omega = 1.0/72;
							}
							point.geq[h] = rho *  omega * A;
						}	

					}
					
				}
		
	}

	public void findPhiLBM(int c) {
		double error = 0;
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
					
					Point point = points[i][j][k];	
					double phiPoint = 0;
					for (int h =0; h<directions; h++){
						phiPoint += point.g[h];
					}
					if (Math.abs(phi[i][j][k]) >= 0.9 )
						error += (phi[i][j][k]-phiPoint) * (phi[i][j][k]-phiPoint);
					phi[i][j][k] = phiPoint;
				}
//		if (c % 1000 == 0 ) 
//			System.out.println("error at t = " + c + " = " + Math.pow(error/(n[0]*n[1]*n[2]), 0.5));
	}
	
	public void findPhiMethodOfLines(int i) {
		Solver.RK4(this, i);
//		Solver.euler(this);
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
	
	public void testDomain(){
		double[] arr = { 0.000405 , 0.007054 , 0.00772 , 0.000586 , -0.004323 , 0.009548 , 0.004614 , -0.008622 , 0.007425 , 0.004446 };
		for (int i=0; i< arr.length; i++) phi[0][i][0] = arr[i];
	}
	
	void definePlanarDroplet() {
		
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1]; j++)
				for (int k=0; k<n[2]; k++){
//					if ( j > 50){
					if ( j > n[1]/2){
						phi[i][j][k] = 1;
					}
					else phi[i][j][k] = -1;
				}
		
	}
	
	void defineCube(int width ) {
			
			for (int i=0; i<n[0]; i++)
				for (int j=0; j<n[1]; j++)
					for (int k=0; k<n[2]; k++){
						if ( (i > n[0]/2 - width/1.5) && (i < n[0]/2 + width/1.5) && 
//								(j > n[1]/2 + width/4) 
								(j > 30)
//								(j < n[1]/2 + width/2) && (j > n[1]/2 - width/2)
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

	public void savePhi(String name, String string) {
		try {
			FileOutputStream fos = new FileOutputStream("dataphi/stringfile" + name +".tmp");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(string);
			oos.writeUTF(string);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void evaporate() {
		double phiH = -1.3;
		
		for (int i=0; i<n[0]; i++)
			for (int k =0; k<n[2]; k++){
				phi[i][1][k] = phiH;
			}
		
	}
	
	public void solidWallBC() {
		
//		// BC at the bottom and top
//				for (int i=0; i<n[0]; i++)
//					for (int k =0; k<n[2]; k++){
//						int j = n[1]-2;
//						
//						phi[i][j][k] = phi[i][j-1][k];
//						phi[i][j+1][k] = phi[i][j][k];
//						
//						phi[i][1][k] = phi[i][2][k];
//						phi[i][0][k] = phi[i][1][k];
//					}
				
		// BC at the bottom and top
				for (int i=0; i<n[0]; i++)
					for (int k =0; k<n[2]; k++){
						int j = n[1]-1;
						
						phi[i][j][k] = phi[i][j-2][k] + Math.tan(Math.PI/2 - contactAngle) 
										* Math.abs(phi[(i+1<n[0]) ? i+1 : (i+1-n[0])][j-1][k] - 
												phi[(i-1>= 0) ? i-1 : (i-1+n[0])][j-1][k]);
						
						phi[i][0][k] = phi[i][2][k] + Math.tan(Math.PI/2 - contactAngle) 
						* Math.abs(phi[(i+1<n[0]) ? i+1 : (i+1-n[0])][1][k] - 
								phi[(i-1>= 0) ? i-1 : (i-1+n[0])][1][k]);
						
					}
	
//		// BC at the bottom and top
//		for (int i=0; i<n[0]; i++)
//			for (int k =0; k<n[2]; k++){
//				int j = n[1]-2;
//				
//				phi[i][j][k] = phi[i][j-2][k] + Math.tan(Math.PI/2 - contactAngle) 
//								* Math.abs(phi[(i+1<n[0]) ? i+1 : (i+1-n[0])][j-1][k] - 
//										phi[(i-1>= 0) ? i-1 : (i-1+n[0])][j-1][k]);
//				phi[i][j+1][k] = phi[i][j][k];
//				
//				phi[i][1][k] = phi[i][3][k] + Math.tan(Math.PI/2 - contactAngle) 
//				* Math.abs(phi[(i+1<n[0]) ? i+1 : (i+1-n[0])][2][k] - 
//						phi[(i-1>= 0) ? i-1 : (i-1+n[0])][2][k]);
//				phi[i][0][k] = phi[i][1][k];
//			}
		
	}

	public void findBoundaryNu() {
		for (int i=0; i<n[0]; i++)
			for (int j =0; j<n[2]; j++){
				nu[i][0][j] = findBoundaryNu(phi[i][0][j], i, 0, j);
				nu[i][n[1]-1][j] = findBoundaryNu(phi[i][n[1]-1][j], i, n[1]-1, j);
			}
	}
	
	double findBoundaryNu(double phiPoint, int i, int j, int k) {
		// Applying Landau model and 2nd order central finite
		// difference discretization
		
		// Checking for boundaries 
		double nuPoint = a * phiPoint + b * phiPoint * phiPoint * phiPoint;
		
		// Applying discretization
		nuPoint -= K * (phi[ (i + 1 < n[0]) ? i + 1 : (i + 1 - n[0]) ][j][k] - 2 * phiPoint + 
						phi[ (i- 1 >= 0) ? i - 1 : (i - 1 + n[0])][j][k]);

		nuPoint -= K * (phi[i][j][(k + 1 < n[2]) ? k + 1 : (k + 1 - n[2]) ] - 2 * phiPoint + 
						phi[i][j][ (k- 1 >= 0) ? k - 1 : (k - 1 + n[2])]);
		
		return  nuPoint;
		
	}

	public double phi(int x, int y, int z){
		// Periodic boundary condition 
		if (x >= n[0]) x =- n[0];
		if (x < 0 ) x+= n[0];
		
		if (y >= n[1]) y =- n[1];
		if (y < 0 ) y += n[1];
		
		if (z >= n[2]) z =- n[2];
		if (z < 0) z += n[2];
		
		return phi[x][y][z];
		
	}

	public void findContactAngle() {
		
		/*
		 * Ref : Displacement of a two-dimensional immiscible droplet in a channel
		 * https://pdfs.semanticscholar.org/b851/1e184101fc44f6d68e8757c6f4f998c063da.pdf
		 */
		
		double b0, a0 = 0; 
		int start = n[0]-1;
		int end = 0;
		for (int i = 0; i<n[0]; i++){
			if (phi[i][n[1]-1][0] > 0 ){
				start = i;
				break;
			}
		}
		for (int i = n[0]-1; i>= 0; i--){
			if (phi[i][n[1]-1][0] > 0 ){
				end = i;
				break;
			}
		}
		b0 =  (end - start + 1);
		
		
		for (int j=0; j<n[1]; j++)
			for (int i= 0; i<n[0]; i++){
				if (phi[i][j][0] > 0 ){
					a0 = n[1] - j;
					j = n[1]; break; 
				}
			}
//		System.out.println("b0 = " + b0 + " a0 = " + a0 );
		double R = a0/2 + (b0 * b0)/(8*a0);
		System.out.println( Math.atan(b0/(2*(R-a0)))*(180/Math.PI) );
		
		
//		for (int m=0; m<20; m = m + 1){
//			for (int i=0; i<n[0]; i++){
//				if (phi[i][n[1]-m-1][0] >= 0 ){
//					System.out.println("Point on the interface "  + i + " " + m);
//					i = n[0];
//				}
//			}	
//		}
		
	}

	
	
	


	

}

	
