package com.jbs.framework.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	
	private Texture texture;
	
	/* The the number of sprites horizontally in the spritesheet and
	 * the width and height of each sprite */
	private final int spritesPerRow, spriteWidth, spriteHeight;
	
	/* The total number of sprites in the spritesheet */
	private int sprites;
	
	/* The time in milliseconds when the animation last began */
	private double startTime;
	
	/* The time in milliseconds when the animation last stopped */
	private double stopTime;
	
	private double totalTimeStopped;
	
	/* The number of frames to switch between in 1 second */
	private float framesPerSecond;
	
	public Animation(Texture texture, int spritesPerRow, int rows, int sprites, float framesPerSecond) {
		this.texture = texture;
		this.spritesPerRow = spritesPerRow;
		this.sprites = sprites;
		this.framesPerSecond = framesPerSecond;
		
		this.spriteWidth = texture.getWidth() / spritesPerRow;
		this.spriteHeight = texture.getHeight() / rows;
	}
	
	/*
	 * Get the n'th sprite of the spritesheet.
	 */
	public TextureRegion getSprite(int spriteID) {
		// Check the domain of our function.
		if (spriteID < 0)
			throw new RuntimeException("Cannot getSprite("+spriteID+") : spriteID must be >= 0");
		if (spriteID >= sprites)
			throw new RuntimeException("Cannot getSprite("+spriteID+") : spriteID must be < the animation's number of sprites");
		
		// Adjust our spriteID to work with the following equations.
		//spriteID ++;
		
		// x is equal to ((the sprite's ID) * (the sprite's width)) % (the spriteSheet's width)
		int x = (spriteID * spriteWidth) % texture.getWidth();
		// y is equal to (((the sprite's ID) / (spritesheet's number of horizontal sprites)) rounded down) * (the sprite's height)
		int y = (int) (spriteID / spritesPerRow) * spriteHeight;
		
		// Create a TextureRegion from our established data.
		TextureRegion region = new TextureRegion(texture, x, y, spriteWidth, spriteHeight);
		
		return region;
	}
	
	/*
	 * @return the current animation's current sprite.
	 */
	public TextureRegion currentSprite() {
		return getSprite(currentSpriteID());
	}
	
	/*
	 * @return the ID of the animation's current sprite.
	 */
	public int currentSpriteID() {
		// The delta time is the total time the animation has been playing for.
		// If not paused, it is equal to (the current time) - (total time the animation has been paused)
		// If paused, it is equal to (the last time we stopped) - (total time the animation has been paused)
		double deltaTime = isAnimating()? (getTime() - totalTimeStopped) : (stopTime - totalTimeStopped);
		
		// deltaFrames is difference in frames since the animation began.
		double deltaFrames = deltaTime * (framesPerSecond/1000);
		
		// The current frame is the deltaFrames wrapped around the total number of
		// sprites in the animation.
		double currentFrame = deltaFrames % sprites;
		
		// Return the currentFrame casted to an integer.
		return (int) currentFrame;
	}
	
	/*
	 * Reset the animation to it's first frame.
	 */
	public void restart() {
		// Set the total amount of time the animation has been stopped to the current time,
		// which effectively resets the animation to frame 0.
		totalTimeStopped = getTime();
	}
	
	/*
	 * Stop the animation, animation must be started.
	 */
	public void stop() {
		if (!isAnimating())
			throw new RuntimeException("Cannot stop animating an animation that is not animating!");
		stopTime = getTime();
	}
	
	/*
	 * Begin the animation, animation must be stopped.
	 */
	public void start() {
		if (isAnimating())
			throw new RuntimeException("Cannot start animating an animation that is already animating!");
		// Set the most recent time we started the animation to now.
		startTime = getTime();
		// Add the time we were paused for to the totalTimeStopped.
		totalTimeStopped += getTime() - stopTime;
	}
	
	/*
	 * @return the state of the animation.
	 */
	public boolean isAnimating() {
		// Check whether we have stopped or begun the animation more recently.
		return startTime >= stopTime;
	}
	
	/*
	 * @return the animation's texture.
	 */
	public Texture texture() {
		return texture;
	}
	
	/*
	 * @return the animation's frame rate;
	 */
	public float framesPerSecond() {
		return framesPerSecond;
	}
	
	/*
	 * Set the playing speed of the animation, measured in frames
	 * per second.
	 */
	public void setSpeed(float framesPerSecond) {
		this.framesPerSecond = framesPerSecond;
	}
	
	/*
	 * @return the system's time in milliseconds.
	 */
	protected double getTime() {
		// 1,000,000 nanoseconds == 1 millisecond
		return System.nanoTime() * 1E-6;
	}
}