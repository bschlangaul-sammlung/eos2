package de.lathanda.eos.base;

public interface Constants {
    LineStyle 
    	DASHED        = LineStyle.DASHED,
    	SOLID         = LineStyle.SOLID,
    	DOTTED        = LineStyle.DOTTED,
    	DASHED_DOTTED = LineStyle.DASHED_DOTTED,
    	INVISIBLE     = LineStyle.INVISIBLE;
    FillStyle
    	FILLED        = FillStyle.FILLED,
    	SHADED        = FillStyle.RULED,
    	DARK_SHADED   = FillStyle.CHECKED,
    	TRANSPARENT   = FillStyle.TRANSPARENT;
    Alignment
    	CENTER        = Alignment.CENTER,
    	LEFT          = Alignment.LEFT,
    	RIGHT         = Alignment.RIGHT,
    	TOP           = Alignment.TOP,
    	BOTTOM        = Alignment.BOTTOM;
	MutableColor 
		BLACK         = MutableColor.BLACK,
		WHITE         = MutableColor.WHITE,
		GRID          = MutableColor.GRID,
		BLUE          = MutableColor.BLUE,
		GREEN         = MutableColor.GREEN,
		RED           = MutableColor.RED,
		GRAY          = MutableColor.GRAY,
		CYAN          = MutableColor.CYAN,
		MAGENTA       = MutableColor.MAGENTA,
		YELLOW        = MutableColor.YELLOW,
		ORANGE        = MutableColor.ORANGE,
		PINK          = MutableColor.PINK,
		DARK_GRAY     = MutableColor.DARK_GRAY,
		LIGHT_GRAY    = MutableColor.LIGHT_GRAY,
		BROWN         = MutableColor.BROWN,
		DIRTY_BROWN   = MutableColor.DIRTY_BROWN,
		LIGHT_BLUE    = MutableColor.LIGHT_BLUE,
		LIGHT_GREEN   = MutableColor.LIGHT_GREEN;   
	boolean
		TRUE          = true,
		FALSE         = false;
}
