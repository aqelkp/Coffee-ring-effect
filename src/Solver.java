import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class Solver {

	public static double fun(Domain domain, double phiPoint, int i, int j, int k){
		
//		double a = Domain.a; double b = Domain.b; double K = Domain.K;
//		
//		double nui = a * phiPoint + b * Math.pow(phiPoint, 3) - 
//				K * ( domain.phi(i+1, j, k) + domain.phi(i-1, j, k) - 2 * phiPoint );
//		double nuiplus1 = a * domain.phi(i+1, j, k) + b * Math.pow(domain.phi(i+1, j, k), 3) - 
//				K * ( domain.phi(i+2, j, k) + phiPoint - 2 * domain.phi(i+1, j, k) );
//		double nuiminus1 = a * domain.phi(i-1, j, k) + b * Math.pow(domain.phi(i-1, j, k), 3) - 
//				K * ( phiPoint + domain.phi(i-2, j, k) - 2 * domain.phi(i-1, j, k) );
//		return domain.M * (nuiplus1 + nuiminus1 - 2 * nui);
		
		
		
		double tempPhi = domain.phi(i, j, k);
		domain.phi[i][j][k] = phiPoint;
		
		double delNu = 0;
		int[] n = domain.n;
		// Applying discretization
		
		delNu += 
				( domain.findNu ( (i + 1 < n[0]) ? i + 1 : (i + 1 - n[0]), j, k ) 
						- (  2 * domain.findNu(i, j, k) ) + 
						domain.findNu( (i- 1 >= 0) ? i - 1 : (i - 1 + n[0]),j, k) );
		
		
		delNu +=  
				( domain.findNu(i, (j + 1 < n[1]) ? j + 1 : (j + 1 - n[1]), k)  
						- 2 * domain.findNu(i, j, k) + 
						domain.findNu(i, (j- 1 >= 0) ? j - 1 : (j - 1 + n[1]), k) );
		
		delNu += 
				( domain.findNu(i, j, (k + 1 < n[2]) ? k + 1 : (k + 1 - n[2]))  
						- 2 * domain.findNu(i, j, k) + 
						domain.findNu(i, j, (k- 1 >= 0) ? k - 1 : (k - 1 + n[2])) );
		
		domain.phi[i][j][k] = tempPhi;
		return Domain.M * delNu;
	}
	
	public static void euler(Domain domain){
		
		int[] n = domain.n;
		for (int i=0; i<n[0]; i++)
			for (int j=domain.start; j<n[1]-domain.start; j++)
				for (int k=0; k<n[2]; k++){
					
//					double distance = (i-domain.centerX)*(i-domain.centerX) + (j-domain.centerY)*(j-domain.centerY);
//					if(Math.pow(distance, 0.5) > domain.rH){
//						domain.phi[i][j][k] = domain.phiH;
//						continue;						
//					}
					
					domain.phi[i][j][k] = fun(domain, domain.phi[i][j][k], i,j,k) + domain.phi[i][j][k];
				}
	}
	
	public static void RK4(Domain domain, int num){
		double error = 0;
		int[] n = domain.n;
		double[][][] k1 = new double[n[0]][n[1]][n[2]];
		double[][][] phiTemp = new double[n[0]][n[1]][n[2]];
		
		for (int i=0; i<n[0]; i++)
			for (int j=domain.start; j<n[1]-domain.start; j++)
				for (int k=0; k<n[2]; k++){
					
					phiTemp[i][j][k] = domain.phi[i][j][k];
					k1[i][j][k] = fun(domain, domain.phi[i][j][k], i,j,k);
					domain.phi[i][j][k] = 0.5 * k1[i][j][k] + domain.phi[i][j][k];
				}
		
		if (domain.isSolidWall) domain.solidWallBC();
		domain.findNu();
		
		double[][][] k2 = new double[n[0]][n[1]][n[2]];
		
		for (int i=0; i<n[0]; i++)
			for (int j=domain.start; j<n[1]-domain.start; j++)
				for (int k=0; k<n[2]; k++){
					
					phiTemp[i][j][k] = domain.phi[i][j][k];
					k1[i][j][k] = fun(domain, domain.phi[i][j][k], i,j,k);
					domain.phi[i][j][k] = 0.5 * k1[i][j][k] + domain.phi[i][j][k];
				}
		
		if (domain.isSolidWall) domain.solidWallBC();
		domain.findNu();
		
		double[][][] k3 = new double[n[0]][n[1]][n[2]];
		
		for (int i=0; i<n[0]; i++)
			for (int j=domain.start; j<n[1]-domain.start; j++)
				for (int k=0; k<n[2]; k++){
					
					phiTemp[i][j][k] = domain.phi[i][j][k];
					k1[i][j][k] = fun(domain, domain.phi[i][j][k], i,j,k);
					domain.phi[i][j][k] = 0.5 * k1[i][j][k] + domain.phi[i][j][k];
				}
		
		if (domain.isSolidWall) domain.solidWallBC();
		domain.findNu();

		double[][][] k4 = new double[n[0]][n[1]][n[2]];
		for (int i=0; i<n[0]; i++)
			for (int j=domain.start; j<n[1]-domain.start; j++)
				for (int k=0; k<n[2]; k++){
					
					phiTemp[i][j][k] = domain.phi[i][j][k];
					k1[i][j][k] = fun(domain, domain.phi[i][j][k], i,j,k);
					domain.phi[i][j][k] = 0.5 * k1[i][j][k] + domain.phi[i][j][k];
				}		
	}
	
	public static double RK4(Domain domain, int x, int y, int z){
		double phi = domain.phi[x][y][z];
		double k1 = fun(domain, phi, x,y,z);
//		if (x == 1) 
//			System.out.println(x + " : k1 " + k1);
		double k2 = fun(domain, phi + 0.5 * k1, x,y,z);
//		double k3 = fun(domain, phi + 0.5 * k2, x,y,z);
//		double k4 = fun(domain, phi + k3, x,y,z);
//		return phi + ( k1 + 2*k2 + 2*k3 + k4)/6;
//		return phi + k1;
		return phi + k2;
	}	
}
