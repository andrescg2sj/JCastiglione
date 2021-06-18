package org.sj.tools.jcastiglione.animation;

public class ValKeyFrame<T>
{
	public int time;
	public T value;

	public ValKeyFrame(int time, T value)
	{
		this.time = time;
		this.value = value;
	}
}