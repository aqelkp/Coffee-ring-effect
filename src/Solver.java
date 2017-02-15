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
		
		domain.solidWallBC();
		domain.findNu();
		
		double[][][] k2 = new double[n[0]][n[1]][n[2]];
		for (int i=0; i<n[0]; i++)
			for (int j=domain.start; j<n[1]-domain.start; j++)
				for (int k=0; k<n[2]; k++){
					k2[i][j][k] = fun(domain, domain.phi[i][j][k], i,j,k);
					domain.phi[i][j][k] = phiTemp[i][j][k] +  k2[i][j][k]/2;
				}
		
		domain.solidWallBC();
		domain.findNu();
		
		double[][][] k3 = new double[n[0]][n[1]][n[2]];
		for (int i=0; i<n[0]; i++)
			for (int j=domain.start; j<n[1]-domain.start; j++)
				for (int k=0; k<n[2]; k++){
					k3[i][j][k] = fun(domain, domain.phi[i][j][k], i,j,k);
					domain.phi[i][j][k] = phiTemp[i][j][k] +  k3[i][j][k];
				}
		
		domain.solidWallBC();
		domain.findNu();

		double[][][] k4 = new double[n[0]][n[1]][n[2]];
		for (int i=0; i<n[0]; i++)
			for (int j=domain.start; j<n[1]-domain.start; j++)
				for (int k=0; k<n[2]; k++){
					k4[i][j][k] = fun(domain, domain.phi[i][j][k], i,j,k);
					domain.phi[i][j][k] = phiTemp[i][j][k] +  ( k1[i][j][k] + 2 * k2[i][j][k] + 
							2 * k3[i][j][k] + k4[i][j][k] )/6;
					if (Math.abs(domain.phi[i][j][k]) >= 0.9 )
					error += (domain.phi[i][j][k] - phiTemp[i][j][k] ) * (domain.phi[i][j][k] - phiTemp[i][j][k] );
				}
//		if (num % 1000 == 0) System.out.println("Error at t = " + num + " is " + Math.pow(max/(n[0]*n[1]*n[2]), 0.5));
		
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
