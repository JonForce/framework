package com.jbs.framework.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameLoop {
	
	private final long timeStep;
	private long
		/* The difference between the actual time and the amount of time simulated with
		 * updates. */
		lag,
		/* The last time that the tick() method was called. */
		lastTickTime;
	
	/*
	 * timeStep is measured in milliseconds and represents
	 * the amount of time to simulate in one update call.
	 */
	public GameLoop(long timeStep) {
		this.timeStep = timeStep;
		lastTickTime = getTime();
	}
	
	/*
	 * Simulate the amount of time defined in the variable
	 * 'timeStep' passed in through the constructor.
	 */
	abstract void update();
	
	/*
	 * Render the game to the specified SpriteBatch.
	 */
	abstract void renderTo(SpriteBatch batch);
	
	/*
	 * Signals the game loop to simulate enough
	 * time to catch up from the last 'tick' method call
	 * and then renders.
	 */
	public final void tick(SpriteBatch batch) {
		assert lag >= 0 && timeStep > 0;
		
		// Add (the amount of time that has passed since the last tick) to (the lag)
		lag += getTime() - lastTickTime;
		lastTickTime = getTime();
		
		// While we can simulate more time
		while (lag >= timeStep) {
			// Simulate (timeStep) amount of time
			update();
			// Subtract the amount of time simulated from our remaining lag
			lag -= timeStep;
		}
		
		// Finally, after updating the game as much as possible, render.
		renderTo(batch);
	}
	
	/* @return the amount of time to simulate per update call. */
	public final long timeStep() {
		return this.timeStep;
	}
	
	public static long getTime() {
		return System.currentTimeMillis();
	}
}