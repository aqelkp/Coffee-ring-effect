
public class Point {

	int directions;
	int[] f;
	
	Point (int directions, int[] f){
		this.directions = directions;
		this.f = f;
	}

	public static void assignRandomPoints(Domain domain, int[] n, int directions) {
		
		int value = 0;
		for (int i=0; i<n[0]; i++){
			for (int j = 0; j<n[1]; j++){
				for (int m=0; m<n[2]; m++){
					int[] g = new int[directions];
					for (int k =0; k< directions; k++){
						g[k] = value;
						value++;
					}
					domain.points[i][j][m] = new Point(directions, g);
				}
			}
		}
		
	}
}
