package com.jbs.framework.io;

import com.badlogic.gdx.Gdx;
import com.jbs.framework.rendering.Screen;

/*
 * Translates input from com.badlogic.gdx.Gdx.input to
 * the virtual coordinate system of the specified screen.
 */
public class InputProxy {
	
	private final Screen screen;
	
	public InputProxy(Screen screen) {
		this.screen = screen;
	}
	
	public boolean justTouched() {
		return Gdx.input.justTouched();
	}
	
	/** @return the amount that the touch moved. */
	public float getDeltaX(int touchIndex) {
		return Gdx.input.getDeltaX(touchIndex);
	}
	/** @return the amount that the touch moved. */
	public final float getDeltaX() { return getDeltaX(0); }
	
	/** @return the amount that the touch moved. */
	public float getDeltaY(int touchIndex) {
		return -Gdx.input.getDeltaY(touchIndex);
	}
	/** @return the amount that the touch moved. */
	public final float getDeltaY() { return getDeltaY(0); }
	
	/*
	 * Get the x-coordinate of the input with the ID of inputID.
	 * Input can be from touch or mouse.
	 */
	public int getX(int inputID) {
		return screen.toVirtualX(Gdx.input.getX(inputID));
	}
	
	/*
	 * Get the y-coordinate of the input with the ID of inputID.
	 * Input can be from touch or mouse.
	 */
	public int getY(int inputID) {
		return screen.toVirtualY(screen.actualHeight() - Gdx.input.getY(inputID));
	}
	
	/*
	 * Get the x-coordinate of the input with the ID of 0.
	 * Input can be from touch or mouse.
	 */
	public int getX() {
		return getX(0);
	}
	
	/*
	 * Get the y-coordinate of the input with the ID of 0.
	 * Input can be from touch or mouse.
	 */
	public int getY() {
		return getY(0);
	}
	
	/*
	 * @return true if the input with the ID of touchID is touched.
	 */
	public boolean isTouched(int touchID) {
		return Gdx.input.isTouched(touchID);
	}
	
	/*
	 * @returns true if the screen is currently touched.
	 */
	public boolean isTouched() {
		return Gdx.input.isTouched();
	}
	
}