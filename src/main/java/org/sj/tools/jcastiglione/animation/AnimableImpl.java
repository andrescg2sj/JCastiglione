package org.sj.tools.jcastiglione.animation;

//import math.geom.Vector2D;
import java.util.NoSuchElementException;

import java.util.Vector;

public abstract class AnimableImpl<T> extends Animable<T>
{

	//private Vector<T> keyFrames;
	//private T defValue;
	
	private KeyFramedList<T> keyFrames;
	
	

	public AnimableImpl(T def)
	{
		keyFrames  = new KeyFramedList<T>();
		setDefaultValue(def);
	}
	
	/**
		a if factor = 0; b if factor = 1
	*/
	public static float interFloat(float a, float b, float factor)
	{
		return b*factor + (1-factor)*a;
	}

	public static double interDouble(double a, double b, double factor)
	{
		return b*factor + (1-factor)*a;
	}
	
	public void copyAnim(AnimableImpl<T> at) {
		//System.out.println();
		this.keyFrames = at.keyFrames.clone();
	}
	
	public T getValue(int time)
	{
		try {
			if(time < keyFrames.getMinTime()) {
				return keyFrames.getEarliest();
			} else if(time > keyFrames.getMaxTime()){
				return keyFrames.getLatest();
			} else { 
				//Â¿Pair mejor?
				Vector<ValKeyFrame<T>> viv = keyFrames.getNearest(time);
				if(viv.size() == 1) {
					return viv.firstElement().value;
				} else if(viv.size() == 2){
					ValKeyFrame<T> kfa = viv.firstElement();
					ValKeyFrame<T> kfb = viv.lastElement();
					T a = kfa.value;
					T b = kfb.value;
					float time_a = kfa.time;
					float time_b = kfb.time;
					float factor = ((float) time - time_a)/(time_b-time_a);
					
					return interpolate((T) a, (T) b,factor);
				} else {
					// ?
					return getDefaultValue();
				}
			}
		} 
		catch(NoSuchElementException nse)
		{
			return getDefaultValue();
		}
	}
	
	public void setKeyFrame(ValKeyFrame<T> kf) {
		
	}
	
	public void setKeyFrame(int time, T value)
	{
		if(time > 0 && keyFrames.size() == 0) {
			keyFrames.setKeyFrame(0,getDefaultValue());
		} 
		keyFrames.setKeyFrame(time, value);
	}
	
	public void removeKeyFrame(int time)
	{
		keyFrames.removeKeyFrame(time);
	}
	
	public int numKeyFrames()
	{
		return keyFrames.size();
	}
	
	public int getTimeForKey(int i)
	{
		return keyFrames.getTimeForKey(i);
	}

	public T getValueForKey(int i)
	{
		return keyFrames.getValueForKey(i);
	}
	
	
	public boolean equals(AnimableImpl<T> ai) {
		if(ai.numKeyFrames() != numKeyFrames())
			return false;
		for(int i=0;i<numKeyFrames();i++) {
			T a = this.getValueForKey(i);
			T b = ai.getValueForKey(i);
			if(!a.equals(b))
				return false;
		}
		return true;
	}
	
	

}