package org.sj.tools.jcastiglione.animation;

public interface Interpolable<type>

{

	public type interpolate(type a, type b, float factor);
}