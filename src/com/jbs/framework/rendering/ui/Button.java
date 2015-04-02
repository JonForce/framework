package com.jbs.framework.rendering.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jbs.framework.io.InputProxy;
import com.jbs.framework.rendering.Graphic;
import com.jbs.framework.util.Updatable;

public class Button extends Graphic implements Updatable {
	
	/* The texture to be rendered if the button's state is !pressed */
	private TextureRegion unpressedTexture;
	
	/* The texture to be rendered if the button's state is pressed */
	private TextureRegion pressedTexture;
	
	/* The button's state */
	private boolean pressed;
	
	/* If the input's touch has yet been released */
	private boolean hasReleasedTouch;
	
	public Button(float x, float y, TextureRegion unpressedTexture, TextureRegion pressedTexture) {
		super(x, y, unpressedTexture);
		this.unpressedTexture = unpressedTexture;
		this.pressedTexture = pressedTexture;
	}
	
	public Button(Vector2 center, TextureRegion unpressedTexture, TextureRegion pressedTexture) {
		this(center.x, center.y, unpressedTexture, pressedTexture);
	}
	
	public Button(Vector2 center, TextureRegion texture) {
		this(center.x, center.y, texture, texture);
	}
	
	/** Set the both the Button's Textures. */
	@Override
	public void setTexture(TextureRegion newTexture) {
		setPressedTexture(newTexture);
		setUnpressedTexture(newTexture);
	}
	
	/** Set the Texture to render when the Button is pressed. */
	public void setPressedTexture(TextureRegion newTexture) {
		this.pressedTexture = newTexture;
	}
	
	/** Set the Texture to render when the Button is not pressed. */
	public void setUnpressedTexture(TextureRegion newTexture) {
		this.unpressedTexture = newTexture;
	}
	
	/**
	 * @return the button's state of depression.
	 */
	public boolean isPressed() {
		return pressed;
	}
	
	/**
	 * @return the texture to be drawn, depends on the button's state.
	 */
	@Override
	public TextureRegion texture() {
		if (isPressed())
			return pressedTexture;
		else
			return unpressedTexture;
	}
	
	/**
	 * React to the button being released.
	 */
	protected void onRelease() {}
	
	/**
	 * React to the button being pressed.
	 */
	protected void onPress() {}
	
	/**
	 * Updates the state of the button, checking for input with
	 * the specified InputProxy and subsequently calling the
	 * onRelease() and onPress() if the state changed.
	 */
	@Override
	public void updateWith(InputProxy input) {
		if (input.isTouched()) {
			// If the input is touched, we were previously not touching, and
			// the touch is within the button then press the button.
			if (hasReleasedTouch && checkInput(input.getX(), input.getY()))
				press();
			// Because the input is touched, we have definitely released the button.
			hasReleasedTouch = false;
		} else {
			// If the input is not touched but was previously touched and
			// the button is pressed, release the button.
			if (!hasReleasedTouch && pressed)
				release();
			// Because the input is not touched, we have definitely released the button.
			hasReleasedTouch = true;
		}
	}
	
	/**
	 * Press the button, throws an error if the button is
	 * already in it's pressed state.
	 */
	public void press() {
		// Assert that our button is not already in it's pressed state.
		assert !this.isPressed();
		// Set the button to it's pressed state.
		pressed = true;
		// React abstractly to the change in state.
		onPress();
	}
	
	/**
	 * Releases the button from it's pressed state,
	 * throws an error if the button is not in it's pressed
	 * state. Calls the onRelease method.
	 */
	public void release() {
		// Assert that our button is in it's pressed state.
		assert this.isPressed();
		// Set the button to it's released state.
		pressed = false;
		// React abstractly to the change in state.
		onRelease();
	}
	
	/**
	 * Returns true if the input's position is within the
	 * button.
	 */
	public final boolean checkInput(float inputX, float inputY) {
		// Return true if :
		// inputX is within [positionX, positionX + textureWidth] and
		// inputY is within [positionY, positionY + textureHeight]
		return ((inputX >= x() - width()/2) &&
				(inputX <= x() + width()/2) &&
				(inputY >= y() - height()/2) &&
				(inputY <= y() + height()/2));
	}
	
	
	/**
	 * Dispose of the textures 'pressedTexture' and
	 * 'unpressedTexture' passed in through the button's constructor.
	 */
	public void dispose() {
		/* not needed with asset manager / this game is too small to worry about
		pressedTexture.dispose();
		unpressedTexture.dispose();
		*/
	}
}