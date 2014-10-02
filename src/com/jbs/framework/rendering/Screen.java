package com.jbs.framework.rendering;

import com.badlogic.gdx.math.Vector2;

/*
 * Utility for storing the size of your virtual and actual screen size. Can
 * translate vectors between the two coordinate systems.
 */
public class Screen {
	
	private final Vector2 actualSize, virtualSize;
	
	public Screen(int actualWidth, int actualHeight, int virtualWidth, int virtualHeight) {
		actualSize = new Vector2(actualWidth, actualHeight);
		virtualSize = new Vector2(virtualWidth, virtualHeight);
	}
	
	/*
	 * @return the screen's actual aspect ratio.
	 */
	public float actualAspectRatio() {
		// AspectRatio = height / width
		return actualHeight() / actualWidth();
	}
	
	/*
	 * @return the screen's virtual aspect ratio.
	 */
	public float virtualAspectRatio() {
		// AspectRatio = height / width
		return (float) virtualHeight() / (float) virtualWidth();
	}
	
	/*
	 * @return the width of the device.
	 */
	public int actualWidth() {
		return (int)actualSize.x;
	}
	
	/*
	 * @return the height of the device.
	 */
	public int actualHeight() {
		return (int)actualSize.y;
	}
	
	/*
	 * @return the width of the virtual screen.
	 */
	public int virtualWidth() {
		return (int)virtualSize.x;
	}
	
	/*
	 * @return the height of the virtual screen.
	 */
	public int virtualHeight() {
		return (int)virtualSize.y;
	}
	
	/*
	 * Creates a Vector2 from the actualX and actualY and
	 * translates it to the screen's virtual coordinate system.
	 */
	public Vector2 virtualVec(int actualX, int actualY) {
		return new Vector2(toVirtualX(actualX), toVirtualY(actualY));
	}
	
	/*
	 * Creates a Vector2 from the virtualX and virtualY and
	 * translates it to the screen's actual coordinate system.
	 */
	public Vector2 actualVec(int virtualX, int virtualY) {
		return new Vector2(toActualX(virtualX), toActualY(virtualY));
	}
	
	/*
	 * Translate the Vector2 vec to the screen's virtual coordinate system.
	 */
	public void toVirtual(Vector2 vec) {
		vec.set(toVirtualX(vec.x), toVirtualY(vec.y));
	}
	
	/*
	 * Translate the Vector2 vec to the screen's actual coordinate system.
	 */
	public void toActual(Vector2 vec) {
		vec.set(toActualX(vec.x), toActualY(vec.y));
	}
	
	/*
	 * @return the screen x-coordinate actualX translated to virtual space.
	 */
	public int toVirtualX(float actualX) {
		return (int) (actualX * ((float) virtualWidth()/actualWidth()));
	}
	
	/*
	 * @return the screen y-coordinate actualY translated to virtual space.
	 */
	public int toVirtualY(float actualY) {
		return (int) (actualY * ((float) virtualHeight()/actualHeight()));
	}
	
	/*
	 * @return the virtual x-coordinate virtualX translated to actual screen space.
	 */
	public int toActualX(float virtualX) {
		return (int) (virtualX * ((float) actualWidth()/virtualWidth()));
	}
	
	/*
	 * @return the virtual y-coordinate virtualY translated to actual screen space.
	 */
	public int toActualY(float virtualY) {
		return (int) (virtualY * ((float) actualWidth()/virtualWidth()));
	}
}