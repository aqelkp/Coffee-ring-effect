import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.HSLColor;

public class Plot extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private static int pointSize = 1;
	public static void define(Domain domain, String fileName){
		int xLength = domain.n[0] * pointSize;
		int yLength = domain.n[1] * pointSize + 30;
		JFrame frame = new JFrame("Plot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Plot plot = new Plot(domain);
		frame.add(plot);
		frame.setSize(xLength , yLength );
		frame.setVisible(true);
		
		try
        {
            BufferedImage image = new BufferedImage(xLength , yLength, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();
            frame.paint(graphics2D);
            ImageIO.write(image,"jpeg", new File("plots/" + fileName));
        }
        catch(Exception exception)
        {
            //code
        }
		
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
		HSLColor boundary = new HSLColor( Color.GRAY );
		
		for (int i=0; i<domain.n[0]; i++)
			for (int j =0; j<domain.n[1]; j++){
				double ph = domain.phi[i][j][0];
				float intensity = (float) (Math.abs(ph) * 1.0f);
				if (ph > 0){
//					System.out.println((int) intensity);
					g.setColor(liquid.adjustTone(intensity));
//					g.setColor(new Color( (int)intensity  * 25, 0, 0));
				} else if (ph < domain.phiH + 0.005 && ph < -1) {
					g.setColor(boundary.adjustTone(intensity));
//					g.setColor(Color.getHSBColor(0.0f, 1.0f, 1- intensity));;
				}else if (ph < 0) {
					g.setColor(gas.adjustTone(intensity));
//					g.setColor(Color.getHSBColor(0.0f, 1.0f, 1- intensity));;
				} else if (ph ==0 ){
					g.setColor(Color.WHITE);
				}
				g.fillRect(i * pointSize,  j * pointSize, pointSize, pointSize);
			}
		
		 
		
	}
}
