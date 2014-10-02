package com.jbs.framework.util.interpolation;

import com.badlogic.gdx.math.Vector2;

public class NurbsCurve extends Interpolation<Vector2> {
	
	public NurbsCurve(Vector2... vecs) {
		super(vecs);
	}
	
	@Override
	public Vector2 interpolate(float normal, Vector2 ... vecs) {
		while (vecs.length != 1) {
			Vector2[] array = new Vector2[vecs.length - 1];
			for (int i = 0; i != vecs.length - 1; i ++)
				array[i] = LinearInterpolation.interpolate(normal, vecs[i], vecs[i + 1]);
			vecs = array;
		}
		return vecs[0];
	}
}