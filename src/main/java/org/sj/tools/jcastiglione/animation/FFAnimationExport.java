package org.sj.tools.jcastiglione.animation;

import java.io.IOException;

public class FFAnimationExport extends PNGAnimationExport {

	String ffmpegPath = "C:\\Users\\IHS\\Documents\\apps\\ffmpeg-20160921\\bin\\ffmpeg.exe";
	public FFAnimationExport(String filename) {
		super(filename);
		// TODO Auto-generated constructor stub
	}
	
	public String insertPattern(String path)
	{
		int j = path.lastIndexOf(".");
		String head = path.substring(0,j);
		String ext = path.substring(j);
		return head + "%03d" + ext;
	}
	
	public String getVideoName(String path)
	{
		int j = path.lastIndexOf(".");
		String head = path.substring(0,j);
		String ext = path.substring(j);
		return head + ".ogv";
	}


	public void close()
	{
		int fps = 12;
		String command =
				String.format("%s -i %s -r %d %s", 
						ffmpegPath, insertPattern(filename),
						fps, getVideoName(filename));
		//String params[] = command.split("\\s+")
		System.out.println(command);
		try { 
			Process p = Runtime.getRuntime().exec(command);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
}
