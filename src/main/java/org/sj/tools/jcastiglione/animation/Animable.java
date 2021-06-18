package org.sj.tools.jcastiglione.animation;

//import java.util.Vector;

public abstract class Animable<Z>
{


	private Z defValue;
	
	//TODO: da error
	/*public Animable(Y def)
	{
		this.defValue = def;
	}*/
	
	public abstract Z interpolate(Z a, Z b, float factor);
	
	public Z getDefaultValue()
	{
		return defValue;
	}
	
	/* Warning! we should fix the type of Interpollable */
	public void  setDefaultValue(Z d)
	{
		defValue = d;
	}
	
	public abstract void setKeyFrame(int time, Z value);
	public abstract void removeKeyFrame(int time);
	
	public abstract Z getValue(int time);
	
	public abstract int numKeyFrames();
	
}