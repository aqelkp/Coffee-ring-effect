
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
			while (angle < anglePassed + 10){
				domain.contactAngle = (angle/180.0) * Math.PI;
				Main.runSimulation(domain);
				angle = angle + 1;
			}
			
			
//			for (int i = (int) Math.round(angle); i<(int) Math.round(angle) + 9; i++){
////				System.out.println("\n============ " + i + "0" + " ============");
////				System.out.print("t Contact_Diameter\t Drop_Height\t\t Slope\t\t Desired_Angle");
//				domain.phi = DataDecoder.readFile("angles/data"+ i + ".tmp");
//				System.out.print("Area: "+ domain.findArea());
////				System.out.print("sigma phi " + domain.sigmaG());
//				domain.contactAngle = domain.findContactAngle(true);
//				Main.runSimulation(domain);
//			}
			
		}  
	}