package com.jbs.framework.physics;

import com.badlogic.gdx.math.Vector2;

public class Util {
	public static final float
		WORLD_TO_BOX = 0.01f,
		BOX_TO_WORLD = 100f;
	
	/*
	 * @return the value scaled to the Box2D coordinate system.
	 */
	public static float toBox(float worldValue) {
		return worldValue * WORLD_TO_BOX;
	}
	
	/*
	 * @return the value scaled to the world coordinate system.
	 */
	public static float toWorld(float box2DValue) {
		return box2DValue * WORLD_TO_BOX;
	}
	
	/*
	 * @return the world vector scaled to the Box2D coordinate system.
	 */
	public static Vector2 toBox(Vector2 worldVector) {
		return new Vector2(toBox(worldVector.x), toBox(worldVector.y));
	}
	
	/*
	 * @return the world vector scaled to the Box2D coordinate system.
	 */
	public static Vector2 toWorld(Vector2 box2DVector) {
		return new Vector2(toWorld(box2DVector.x), toWorld(box2DVector.y));
	}
	
	/*
	 * Convert milliseconds per period to Hertz.
	 */
	public static float toHertz(float milliseconds) {
		return milliseconds / 1000f;
	}
	
	/*
	 * Convert Hertz to milliseconds per period.
	 */
	public static float toMilliseconds(float hertz) {
		return hertz * 1000f;
	}
}