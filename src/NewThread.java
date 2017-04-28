
public class NewThread extends Thread{  
		
		Domain domain;
		double anglePassed;
		
		NewThread(int numPoint, double anglePassed){
			domain = new Domain(2,numPoint);
			domain.addSolidWall();
			Main.initializeDroplet(domain);
			domain.phi = DataDecoder.readFile(Main.file);
			this.anglePassed = anglePassed;
		}
		
		public void run(){  
			double angle = anglePassed;
			while (angle < anglePassed + 3){
				domain.contactAngle = (angle/180.0) * Math.PI;
				Main.runSimulation(domain);
				angle = angle + 0.25;
			}
		}  
	}