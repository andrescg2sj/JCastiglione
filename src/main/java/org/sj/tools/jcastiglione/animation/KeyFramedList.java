package org.sj.tools.jcastiglione.animation;

import java.util.NoSuchElementException;
import java.util.Vector;



class KeyFramedList<T>
{
	private Vector<ValKeyFrame<T>> keyframes;

	

	public KeyFramedList()
	{	
		keyframes = new Vector<ValKeyFrame<T>>();
	}
	
	public T getEarliest() throws NoSuchElementException
	{
		return keyframes.firstElement().value;
	}

	public T getLatest() throws NoSuchElementException
	{
		return keyframes.lastElement().value;
	}
	
	public int getMinTime() throws NoSuchElementException
	{
		//if(keyframes.isEmpty()) throw new NoSuchElementException("No keyframes");
		return keyframes.firstElement().time;
	}
	
	public int getMaxTime() throws NoSuchElementException
	{
		//if(keyframes.isEmpty()) throw new NoSuchElementException("No keyframes");
		return keyframes.lastElement().time;
	}


	public T getValue(int time) 
	{	
		//TODO: binary search or linked list...
		if(time < getMinTime() || time > getMaxTime()) return null;
		for(int i=0; i<keyframes.size(); i++){
			ValKeyFrame<T> kf = keyframes.get(i);
			if(kf.time == time) {
				return kf.value;
			} else if(kf.time > time) {
				return null;
			}
		}
		return null;
	}
	
	public Vector<ValKeyFrame<T>> getNearest(int time) throws NoSuchElementException
	{	
		ValKeyFrame prev = null;
		//ValKeyFrame<T> nearest[];
		Vector<ValKeyFrame<T>> nearest;
		if(keyframes.isEmpty()) throw new NoSuchElementException("No keyframes");
		if(time < getMinTime() || time > getMaxTime()) return null;
		for(int i=0; i<keyframes.size(); i++){
			ValKeyFrame<T> kf = keyframes.get(i);
			if(kf.time == time) {
				nearest = new Vector<ValKeyFrame<T>>(1);
				//nearest[0] = kf;
				nearest.add(kf);
				return nearest;
			} else if(kf.time > time) {
				nearest = new  Vector<ValKeyFrame<T>>(2);
				//nearest[0] = prev;
				//nearest[1] = kf;
				nearest.add(prev);
				nearest.add(kf);
				return nearest;
			}
			prev = kf;
		}
		throw new RuntimeException("Should not reach this point");
	}


	public void setKeyFrame(ValKeyFrame<T> kf)
	{
		setKeyFrame_impl(kf.time, kf.value);
	}

	public void setKeyFrame(int time, T value)
	{
		setKeyFrame_impl(time, value);
	}
	
	public int size()
	{
		return keyframes.size();
	}

	public void setKeyFrame_impl(int time, T value)
	{
		System.out.println("set KF. num=" + keyframes.size());
		ValKeyFrame<T> kf = new ValKeyFrame<T>(time, value);
		if(keyframes.size() == 0) {
			/*if(time > 0) {
				ValKeyFrame<T> kf = new ValKeyFrame<T>(0, value);
			}*/
			keyframes.add(kf);
			
		} else if(keyframes.size() == 1) {
			ValKeyFrame<T> first =  keyframes.get(0);
			if(time < first.time) {
				keyframes.insertElementAt(kf, 0);
			} else if(time > first.time) {
				keyframes.add(kf);
			} else if(time == first.time) {
				keyframes.set(0, kf);
			}
		} else {
			// TODO: binary search or linked list
			for(int i=0; i<keyframes.size(); i++){
				if(keyframes.get(i).time > time) {
					keyframes.insertElementAt(kf, i);
					return;
				}
			}
			keyframes.add(kf);
		}
	}

	public int getTimeForKey(int i)
	{
		return keyframes.get(i).time;
	}

	public T getValueForKey(int i)
	{
		return keyframes.get(i).value;
	}

	
	public  void removeKeyFrame(int time)
	{
		// TODO:
		throw new UnsupportedOperationException("Remove KeyFrame");
	}
	
	public KeyFramedList<T> clone() {
		KeyFramedList<T> k = new KeyFramedList<T>();
		for(int i=0; i<this.size(); i++) {
			int time = this.getTimeForKey(i);
			T value = this.getValueForKey(i);
			k.setKeyFrame(time, value);
		}
		return k;
	}

}
