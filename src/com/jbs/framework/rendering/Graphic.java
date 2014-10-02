package com.jbs.framework.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Graphic implements Renderable {
	
	/* The Texture to be rendered */
	private Texture texture;
	/* The center of the graphic */
	private Vector2 position;
	/* The (width, height) of the graphic */
	private Vector2 size;
	/* The Graphic's rotation in degrees */
	private float rotation;
	
	/*
	 * Create a new Graphic with it's center set to (x, y), with a width and height,
	 * the Texture 'texture'.
	 */
	public Graphic(int x, int y, int width, int height, Texture texture) {
		this.texture = texture;
		this.position = new Vector2(x, y);
		this.size = new Vector2(width, height);
	}
	
	public Graphic(Vector2 center, Vector2 size, Texture texture) {
		this((int)center.x, (int)center.y, (int)size.x, (int)size.y, texture);
	}
	
	public Graphic(Vector2 center, Texture texture) {
		this((int)center.x, (int)center.y, texture.getWidth(), texture.getHeight(), texture);
	}
	
	/*
	 * Create a new Graphic with it's center set to (x, y) and the Texture 'texture'.
	 */
	public Graphic(int x, int y, Texture texture) {
		this(x, y, texture.getWidth(), texture.getHeight(), texture);
	}
	
	/*
	 * @return the x-coordinate of the center of the graphic.
	 */
	public int x() {
		return (int) position.x;
	}
	
	/*
	 * @return the y-coordinate of the center of the graphic.
	 */
	public int y() {
		return (int) position.y;
	}
	
	/*
	 * Set the graphic's center to (x,y).
	 */
	public void setPosition(int x, int y) {
		position.set(x, y);
	}
	
	/*
	 * Set the graphic's center to newPosition.
	 */
	public void setPosition(Vector2 newPosition) {
		position.set(newPosition);
	}
	
	/*
	 * Translate the center of the graphic by (amountX, amountY).
	 */
	public void translate(int amountX, int amountY) {
		// Set the position to the current position plus (amountX, amountY).
		setPosition(x() + amountX, y() + amountY);
	}
	
	/*
	 * Translate the center of the graphic by (amount.x, amount.y).
	 */
	public void translate(Vector2 amount) {
		translate((int) amount.x, (int) amount.y);
	}
	
	/*
	 * @return the width of the graphic.
	 */
	public int width() {
		return (int) size.x;
	}
	
	/*
	 * @return the height of the graphic.
	 */
	public int height() {
		return (int) size.y;
	}
	
	/*
	 * Set the width of the graphic to 'newWidth'.
	 */
	public void setWidth(int newWidth) {
		size.x = newWidth;
	}
	
	/*
	 * Set the height of the graphic to 'newHeight'.
	 */
	public void setHeight(int newHeight) {
		size.y = newHeight;
	}
	
	/*
	 * Set the width and height of the graphic to 'newWidth' and 'newHeight' respectively.
	 */
	public void setSize(int newWidth, int newHeight) {
		setWidth(newWidth);
		setHeight(newHeight);
	}
	
	/*
	 * Set the width and height of the graphic to 'newSize.x' and 'newSize.y' respectively.
	 */
	public void setSize(Vector2 newSize) {
		setSize((int) newSize.x, (int) newSize.y);
	}
	
	/*
	 * Scale the graphic's size by (amountX, amountY).
	 */
	public void scale(float amountX, float amountY) {
		// Multiply the size of the graphic by the scales.
		size.x *= amountX;
		size.y *= amountY;
	}
	
	/*
	 * Scale the graphic's size by (amount.x, amount.y).
	 */
	public void scale(Vector2 amount) {
		scale(amount.x, amount.y);
	}
	
	/*
	 * Scale the graphic's size by the scalar.
	 */
	public void scale(float scalar) {
		scale(scalar, scalar);
	}
	
	/*
	 * Set the Graphic's rotation to 'degrees'.
	 */
	public void setRotation(float degrees) {
		rotation = degrees;
	}
	
	/*
	 * Rotate the Graphic by 'degrees'.
	 */
	public void rotate(float degrees) {
		rotation += degrees;
	}
	
	/*
	 * @return the graphic's texture.
	 */
	public Texture texture() {
		return texture;
	}
	
	/*
	 * Set the graphic's texture to newTexture.
	 */
	public void setTexture(Texture newTexture) {
		texture = newTexture;
	}
	
	/*
	 * Draw the graphic's texture to the batch with the graphic's size
	 * assuming that graphic's position is it's center.
	 */
	public void renderTo(SpriteBatch batch) {
		batch.draw(
				texture(), // Draw the Graphic's texture.
				x() - width()/2, y() - height()/2, // The position to render at.
				width()/2, height()/2, // The offset relative to the position to rotate around.
				width(), height(), // The size to stretch the texture to.
				1, 1, // The x and y scale of the rendered texture.
				rotation, // The rotation of the rendered texture.
				0, 0, // The source position of the rendered texture within the Graphic's texture.
				texture().getWidth(), texture().getHeight(), // The source size of the rendered texture within the Graphic's texture.
				false, false // If the rendered texture is flipped over the x and y axis respectively.
			);
	}
	
	/*
	 * Draw the graphic's texture to the batch with the graphic's size
	 * with the transparency of 'alpha'.
	 * Assumes that the position of the graphic is it's center.
	 */
	public void renderTo(SpriteBatch batch, float alpha) {
		// Set the identity color to the batch's current color.
		Color identity = batch.getColor();
		
		// The color 'temporaryColor' represents the identity color but with 'alpha' transparency.
		Color temporaryColor = new Color(identity.r, identity.g, identity.b, alpha);
		
		// Set the sprite batch's color to the temporary color.
		batch.setColor(temporaryColor);
		
		// Draw the texture centered around the point 'position'.
		renderTo(batch);
		
		// Reset the batch's color back to it's color before we changed it.
		batch.setColor(identity);
	}
	
	/*
	 * Draw the graphic's texture to the batch with the graphic's size
	 * with the color 'tint'.
	 * Assumes that the position of the graphic is it's center.
	 */
	public void renderTo(SpriteBatch batch, Color tint) {
		// Set the identity color to the batch's current color.
		Color identity = batch.getColor();
		
		// Set the sprite batch's color to the specified tint.
		batch.setColor(tint);
		
		// Draw the texture centered around the point 'position'.
		renderTo(batch);
		
		// Reset the batch's color back to it's color before we changed it.
		batch.setColor(identity);
	}
}