package org.sj.tools.jcastiglione.animation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PNGAnimationExport implements AnimationExport {
	
	int frame = 0;
	String filename;
	
	public PNGAnimationExport(String filename)
	{
		this.filename = filename;
	}

	public String insertNumber(String path, int n)
	{
		int j = path.lastIndexOf(".");
		String head = path.substring(0,j);
		String ext = path.substring(j);
		return head + String.format("%03d", n) + ext;
		
	}
	
	public void push(BufferedImage img) {
		File file = new File(insertNumber(filename,frame));
		try {
			javax.imageio.ImageIO.write(img, "png", file);
		} catch(IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		
		frame++;

	}

}
