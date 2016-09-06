import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.HSLColor;

public class Plot extends JPanel{
	
	public static void define(Domain domain){
		JFrame frame = new JFrame("Plot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Plot plot = new Plot(domain);
		frame.add(plot);
		frame.setSize(domain.n[0] , domain.n[1] );
		frame.setVisible(true);
	}
	
	Domain domain;
	Plot (Domain domain){
		this.domain = domain;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		HSLColor liquid = new HSLColor( Color.RED );
		HSLColor gas = new HSLColor( Color.BLUE );
		
		for (int i=0; i<domain.n[0]; i++)
			for (int j =0; j<domain.n[1]; j++){
				double ph = domain.points[i][j][0].phi;
				float intensity = (float) (Math.abs(ph) * 1.0f);
				if (ph > 0){
					g.setColor(liquid.adjustTone(intensity));
				} else if (ph < 0) {
					g.setColor(gas.adjustTone(intensity));
				} else if (ph ==0 ){
					g.setColor(Color.WHITE);
				}
				g.fillRect(i, j, 1,1);
			}
		
		
	}
}
