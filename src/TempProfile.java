
public class TempProfile {

	int[] n;
	int[][] c;
	int directions;
	Domain domain;
		
	TempProfile(){
		// Define a domain and set Temp to 0 at all point 
		int[] n = {100, 1, 1};
		int[][] c = { {0,0,0}, {1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0}};
		int directions = c.length;
		Domain domain = new Domain();

		this.n = n;
		this.c = c;
		this.directions = directions;
		this.domain = domain;
		
		for (int i=0; i<n[0]; i++){
			domain.points[i][0][0].T = 0;
		}
		domain.points[0][0][0].T = 1;
		domain.points[n[0]-1][0][0].T = 0;
		
		// Apply streaming function
		for (int i=0; i<20000; i++){
			findGeq();
			stream();
			findT();
			Main.printPoints(domain, n);
			
			if (i%200 == 0) TempPlot.define(domain);
		}

	}

	private void findT() {
		for (int i =0; i<n[0]; i++){
			
			double sum = 0;
			Point point = domain.points[i][0][0];
			for (int m=0; m<c.length; m++){
				sum += point.g[m];
			}
			point.T = sum;	
		}
		domain.points[0][0][0].T = 1;
		domain.points[n[0]-1][0][0].T = 0;

	}

	private void findGeq() {
		double omega;
		for (int i =0; i<n[0]; i++){
			
			Point point = domain.points[i][0][0];
			for (int m=0; m<c.length; m++){
				if (m==0) omega = 1.0/3;
				else omega = 1.0/6;
				point.geq[m] = point.T * omega;
			}
			
			
		}
	}
	
	// Apply streaming function
	void stream(){
			
			
			for (int i=0; i<directions; i++){
				
				// Take out the g value in each direction and make it into a 1-D array
				// For temp saving values
				double[][][] tempf = new double[n[0]][n[1]][n[2]];
				for (int j=0; j<n[0]; j++){
					for (int k=0; k<n[1]; k++) {
						for (int m=0; m<n[2]; m++){
							tempf[j][k][m] = domain.points[j][k][m].g[i];
						}
					}
				}
				
				// Apply streaming to each of the point
				for (int j=0; j<n[0]; j++){
					for (int k=0; k<n[1]; k++){
						for (int m=0; m<n[2]; m++){
							int x = j + c[i][0] * 1;
							int y = k + c[i][1] * 1;
							int z = m + c[i][2] * 1;
							
							// Periodic boundary condition 
							if (x >= n[0]) x =- n[0];
							if (x < 0 ) x+= n[0];
							
							if (y >= n[1]) y =- n[1];
							if (y < 0 ) y += n[1];
							
							if (z >= n[2]) z =- n[2];
							if (z < 0) z += n[2];
							
//							domain.points[x][y][z].g[i] = tempf[j][k][m];
							
							domain.points[x][y][z].g[i] = tempf[j][k][m] -  
									(tempf[j][k][m] - domain.points[j][k][m].geq[i]) ;
						}
					}
					
				}
				
			}
		}


	
	public  void intialisepoints() {
		
		for (int i=0; i<n[0]; i++){
			for (int j = 0; j<n[1]; j++){
				for (int m=0; m<n[2]; m++){
					double[] g = new double[directions];
					for (int k =0; k< directions; k++){
//						g[k] = Math.random()/1000.0;
						g[k] = 0.0;
					}
					domain.points[i][j][m] = new Point(directions, g);
				}
			}
		}
		
	}
	
}
