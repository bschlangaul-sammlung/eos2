package de.lathanda.eos.gui.diagram;

import java.awt.Color;
import java.awt.Font;

/**
 * Grundlegende Anzeigeeinheit
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class Unit {
	protected final static float INDENT = 1;
	protected final static float BORDER = 1;
	protected final static float SPACE = 5;
	protected final static float SPACEX = 5;
	protected static Font HEADER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
	protected static Font STANDARD_FONT = new Font(Font.SERIF, Font.PLAIN, 12);
	protected float width;
	protected float height;
	protected float offsetX = 0;
	protected float offsetY = 0;
	protected Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	protected Color color = Color.BLACK;

	public abstract void drawUnit(Drawing d);

	public abstract void layoutUnit(Drawing d);

	public final void draw(Drawing d) {
		d.pushTransform();
		d.translate(offsetX, offsetY);
		Font orgF = d.getFont();
		Color orgC = d.getColor();
		d.setFont(font);
		d.setColor(color);
		drawUnit(d);
		d.setFont(orgF);
		d.setColor(orgC);
		d.popTransform();
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public final void layout(Drawing d) {
		d.setFont(font);
		layoutUnit(d);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	public float getOffsetX() {
		return offsetX;
	}

	public float getBottom() {
		return height + offsetY;
	}

	public float getRight() {
		return width + offsetX;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void centerX(float width) {
		setOffsetX((width - this.width) / 2);
	}

	public void centerY(float height) {
		setOffsetY((height - this.height) / 2);
	}
}
