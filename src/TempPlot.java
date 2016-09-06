import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.HSLColor;

public class TempPlot extends JPanel{
	
	public static void define(Domain domain){
		JFrame frame = new JFrame("Plot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TempPlot plot = new TempPlot(domain);
		frame.add(plot);
		frame.setSize(domain.n[0], 125);
		frame.setVisible(true);
	}
	
	Domain domain;
	TempPlot (Domain domain){
		this.domain = domain;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		for (int i=0; i<domain.n[0]; i++)
			for (int j =0; j<domain.n[1]; j++){
				g.fillRect(i, (100 - (int) (domain.points[i][0][0].T* 100)), 1,1);
			}
		
		
	}
}
