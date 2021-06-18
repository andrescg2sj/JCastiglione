package org.sj.tools.jcastiglione;


import java.util.LinkedList;
import java.util.ListIterator;

class KeyFrame<T>
{
	public int time;
	public T frame;
	
	public KeyFrame(int t, T f)
	{
		time = t;
		frame = f;
	}
}

public class OrderedList<T> {
	
	// slow; TODO: use Vector
	LinkedList<KeyFrame> frames;
	
	public void insert(int time, T frame)
	{
		ListIterator<KeyFrame> it = frames.listIterator();
		while(it.hasNext())
		{
			KeyFrame kf = it.next();
			if(time < kf.time) {
				it.add(new KeyFrame(time,frame));
				return;
			}
		}
		frames.offerLast(new KeyFrame(time, frame));
	}
	
	public  void remove(int time)
	{
		// TODO
	}
}