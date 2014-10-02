package com.jbs.framework.util.interpolation;

import com.badlogic.gdx.math.Vector2;

public class LinearInterpolation extends Interpolation<Vector2> {
	
	public LinearInterpolation(Vector2... vecs) {
		super(vecs);
	}
	
	@Override
	public Vector2 interpolate(float normal, Vector2... vecs) {
		if (vecs.length > 2)
			throw new RuntimeException("Cannot perform a linear interpolation of more than 2 vectors");
		return LinearInterpolation.interpolate(normal, vecs[0], vecs[1]);
	}
	
	public static Vector2 interpolate(float normal, Vector2 vecA, Vector2 vecB) {
		return new Vector2(vecA.x + (vecB.x - vecA.x)*normal, vecA.y + (vecB.y - vecA.y)*normal);
	}
}