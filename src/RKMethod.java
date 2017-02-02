import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class RKMethod {
	
	public static void main(String[] args) {
		int max = 10000;
		
		int length = 100;
		double[] T = new double[length+1];
		
		double[] xaxis = new double[length+1];
		double[] yaxis = new double[length+1];
		T[0] = 50; T[length] = 0;
		xaxis[0] = 0; yaxis[0] = 50;
		xaxis[length] = length; yaxis[length] = 0;
		
		for (int i=0; i<max; i++){
			for (int j=1; j<length; j++){
				xaxis[j] = j;
				System.out.println("Before = " + T[j] + " after = " + RK4(T,j)) ;
				yaxis[j] = RK4(T,j);
				T[j] = yaxis[j];
				
			}
			if (i%100 == 0){
				XYChart chart = QuickChart.getChart("T(x) vs x", "x", "T(x)", "T", xaxis, yaxis);
				new SwingWrapper(chart).displayChart();
			}
			
		}
		
	}

	public static double fun(Domain domain, double phiPoint, int i, int j, int k){
		double[][][] nu = domain.nu;
		double delNu = 0;
		int[] n = domain.n;
		// Applying discretization
		delNu += 
				(nu[ (i + 1 < n[0]) ? i + 1 : (i + 1 - n[0]) ][j][k] 
						- 2 * domain.findNu(phiPoint, i, j, k) + 
						nu[ (i- 1 >= 0) ? i - 1 : (i - 1 + n[0])][j][k]);
		
		delNu +=  
				( nu[i][ (j + 1 < n[1]) ? j + 1 : (j + 1 - n[1]) ][k] 
						- 2 * domain.findNu(phiPoint, i, j, k) + 
						nu[i][ (j- 1 >= 0) ? j - 1 : (j - 1 + n[1])][k] );
		
		delNu += 
				(nu[i][j][(k + 1 < n[2]) ? k + 1 : (k + 1 - n[2]) ] 
						- 2 * domain.findNu(phiPoint, i, j, k) + 
						nu[i][j][ (k- 1 >= 0) ? k - 1 : (k - 1 + n[2])]);
		return Domain.M * delNu;
	}
	
	public static double RK4(Domain domain, int x, int y, int z){
		double phi = domain.phi[x][y][z];
		double k1 = fun(domain, phi, x,y,z);
		double k2 = fun(domain, phi + 0.5 * k1, x,y,z);
		double k3 = fun(domain, phi + 0.5 * k2, x,y,z);
		double k4 = fun(domain, phi + k3, x,y,z);
		return phi + ( k1 + 2*k2 + 2*k3 + k4)/6;
	}
	
	public static double RK4(double[] T, int i){
		double k1 = fun(T, T[i], i);
		double k2 = fun(T, T[i] + 0.5 * k1, i);
		double k3 = fun(T, T[i] + 0.5 * k2, i);
		double k4 = fun(T, T[i] + k3, i);
		System.out.println(k1);
		return T[i] + ( k1 + 2*k2 + 2*k3 + k4)/6;
	}
	
	public static double fun(double[] T, double x,  int i){
		
		return  (T[i-1] - 2 * x + T[i+1]);

	}

	
}
