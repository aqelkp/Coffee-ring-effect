

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphics.HSLColor;

public class Image {
	
	private static int pointSize = 5;
	
	public static BufferedImage createImage(Domain domain, int t)
    {
          int width = domain.n[0] * pointSize;
          int height = domain.n[1] * pointSize;
          int imageType = BufferedImage.TYPE_4BYTE_ABGR;
         
          BufferedImage imageBuffer = new BufferedImage(width,height,imageType);
          Graphics2D graphics = imageBuffer.createGraphics();
         
          graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                      RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
         
          // true means to repeat pattern
          GradientPaint gradientBG = new GradientPaint(0, 0, Color.red, 175,
                      175, Color.yellow,true);     
         
//          graphics.setPaint(gradientBG);
//          graphics.fillRect(0, 0, imageBuffer.getWidth(), imageBuffer.getHeight());
//          graphics.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
//         
//         
//          GradientPaint gradientText = new GradientPaint(0, 0, Color.BLUE,
//                      100, 20, Color.CYAN,true);   
//          graphics.setPaint(gradientText);
//         
//           graphics.rotate(0.25);
//          graphics.drawString("Sarf 2D",55,40);
         
          HSLColor liquid = new HSLColor( Color.RED );
  		HSLColor gas = new HSLColor( Color.BLUE );
  		HSLColor boundary = new HSLColor( Color.GRAY );
		
  		for (int i=0; i<domain.n[0]; i++)
  			for (int j =0; j<domain.n[1]; j++){
  				double ph = domain.phi[i][j][0];
  				float intensity = (float) (Math.abs(ph) * 1.0f);
  				if (ph > 0){
//  					System.out.println((int) intensity);
  					graphics.setColor(liquid.adjustTone(intensity));
//  					g.setColor(new Color( (int)intensity  * 25, 0, 0));
  				} else if (ph < domain.phiH + 0.005 && ph < -1) {
					graphics.setColor(boundary.adjustTone(intensity));
//					g.setColor(Color.getHSBColor(0.0f, 1.0f, 1- intensity));;
				} else if (ph < 0) {
  					graphics.setColor(gas.adjustTone(intensity));
//  					g.setColor(Color.getHSBColor(0.0f, 1.0f, 1- intensity));;
  				} else if (ph ==0 ){
  					graphics.setColor(Color.WHITE);
  				}
  				graphics.fillRect(i * pointSize,  j * pointSize, pointSize, pointSize);
  			}
  	
          
           File file = new File("images/domain_at_t=" + t + "_angle="+ (int)(domain.contactAngle*(180/Math.PI)) + ".png");
           try {
        	   ImageIO.write(imageBuffer, "png", file);
           } catch (IOException e) {
				e.printStackTrace();
           }
       
          return imageBuffer;
}

}
