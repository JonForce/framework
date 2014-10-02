package com.jbs.framework.util.interpolation;

public abstract class Interpolation<Type> {
	
	private boolean
		/* True if the normal should loop. */
		shouldLoop = false;
	
	private long
		/* The time that the Interpolation began interpolating. */
		startTime;
	
	private float
		/* The Interpolation's speed. */
		speed;
	
	private Type[]
		/* The default Objects to Interpolate. */
		objectsToInterpolate;
	
	public Interpolation(Type... objs) {
		setObjectsToInterpolate(objs);
	}
	
	public Interpolation() { }
	
	public abstract Type interpolate(float normal, Type ... objectsToInterpolate);
	
	/* @return the normalized value to use by default. */
	public final float normal() {
		return (System.currentTimeMillis() - startTime) * speed;
	}
	
	/* Set whether or not to loop the Interpolation's normal. */
	public final void setLooping(boolean flag) {
		this.shouldLoop = flag;
	}
	
	public final void setSpeed(float speed) {
		this.speed = speed;
	}
	
	/* Interpolate the specified Objects with the default normal. */
	public final Type interpolate(Type ... objectsToInterpolate) {
		float normal = normal();
		if (shouldLoop)
			normal %= 1;
		return interpolate(normal, objectsToInterpolate);
	}
	
	/* Interpolate the default Objects with the default normal. */
	public final Type interpolate() {
		return interpolate(objectsToInterpolate);
	}
	
	/* Set the default Objects to Interpolate. */
	public final void setObjectsToInterpolate(Type ... objs) {
		this.objectsToInterpolate = objs;
	}
	
	/* @return the default Objects to Interpolate. */
	public final Type[] objectsToInterpolate() {
		return this.objectsToInterpolate;
	}
	
	/* Start the Interpolation with the specified speed in Hertz. */
	public final void start(float speed) {
		this.startTime = System.currentTimeMillis();
		this.speed = speed;
	}
	
	/* Reset the Interpolation. */
	public final void reset() {
		this.startTime = System.currentTimeMillis();
	}
}