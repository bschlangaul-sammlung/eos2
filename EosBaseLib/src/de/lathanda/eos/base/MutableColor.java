package de.lathanda.eos.base;

import java.awt.Color;

public class MutableColor {
	public static final MutableColor BLACK       = new MutableColor(Color.BLACK);
	public static final MutableColor WHITE       = new MutableColor(Color.WHITE);
	public static final MutableColor GRID        = new MutableColor(128,128,128);
	public static final MutableColor BLUE        = new MutableColor(Color.BLUE);
	public static final MutableColor GREEN       = new MutableColor(Color.GREEN);
	public static final MutableColor RED         = new MutableColor(Color.RED);
	public static final MutableColor GRAY        = new MutableColor(Color.GRAY);
	public static final MutableColor CYAN        = new MutableColor(Color.CYAN);
	public static final MutableColor MAGENTA     = new MutableColor(Color.MAGENTA);
	public static final MutableColor YELLOW      = new MutableColor(Color.YELLOW);
	public static final MutableColor ORANGE      = new MutableColor(Color.ORANGE);
	public static final MutableColor PINK        = new MutableColor(Color.PINK);
	public static final MutableColor DARK_GRAY   = new MutableColor(Color.DARK_GRAY);
	public static final MutableColor LIGHT_GRAY  = new MutableColor(Color.LIGHT_GRAY);
	public static final MutableColor BROWN       = new MutableColor(118,80,8);
	public static final MutableColor DIRTY_BROWN       = new MutableColor(118,64,0);
	public static final MutableColor LIGHT_BLUE  = new MutableColor(123,177,244);
	public static final MutableColor LIGHT_GREEN = new MutableColor(87,225,4);	
	private Color c;
	
	public MutableColor(int r, int g, int b, int a) {
		c = new Color(r,g,b,a);
	}
	public MutableColor(int r, int g, int b) {
		c = new Color(r,g,b);
	}
	public MutableColor(Color c) {
		this.c = c;
	}
	public MutableColor(float r, float g, float b, float a) {
		c = new Color(r,g,b,a);
	}
	public MutableColor(int code) {
		c = new Color(code);
	}
	public int getRed() {
		return c.getRed();
	}

	public int getGreen() {
		return c.getGreen();
	}

	public int getBlue() {
		return c.getBlue();
	}

	public int getAlpha() {
		return c.getAlpha();
	}

	public Color brighter() {
		return c.brighter();
	}

	public Color darker() {
		return c.darker();
	}
	
	public void setAlpha(int a) {
		c = new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
	}
	
	public void setBlue(int b) {
		c = new Color(c.getRed(), c.getGreen(), b, c.getAlpha());
	}
	
	public void setRed(int r) {
		c = new Color(r, c.getGreen(), c.getBlue(), c.getAlpha());
	}
	
	public void setGreen(int g) {
		c = new Color(c.getRed(), g, c.getBlue(), c.getAlpha());
	}
	public Color getColor() {
		return c;
	}
	public int getRGB() {
		return c.getRGB();
	}
}
