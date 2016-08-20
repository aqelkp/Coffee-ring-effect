
public class Domain {

	int dimension;
	int delt = 1;
	
	// Number of points in each dimension
	int[] n;
	
	// Points in the plane
	Point[][][] points;
	
	// Defining a domain
	public Domain(int[] n){
		this.dimension = n.length;
		this.n = n;
		points = new Point[n[0]][n[1]][n[2]];
	}
	
	// Apply streaming function
	void stream(int directions, int[][] c){
		
		for (int i=0; i<directions; i++){
			
			// Take out the f value in each direction and make it into a 1-D array
			// For temp saving values
			int[][][] tempf = new int[n[0]][n[1]][n[2]];
			for (int j=0; j<n[0]; j++){
				for (int k=0; k<n[1]; k++) {
					for (int m=0; m<n[2]; m++){
						tempf[j][k][m] = points[j][k][m].f[i];
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
						
						points[y][x][z].f[i] = tempf[k][j][m];
					}
				}
				
			}
			
		}
	}

	// ΣΣgi
	int sigmaG(int directions){
		int sum = 0;
		for (int i=0; i<n[0]; i++)
			for (int j=0; j<n[1];j++)
				for (int m=0; m<n[2]; m++)
					for (int k=0; k<directions; k++)
						sum += points[i][j][m].f[k];
		return sum;
	}
}
