package eos;

import de.lathanda.eos.base.MutableColor;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht dem EOS-Konstantenpool FARBE.
 * 
 * Im Gegensatz zu den Eos Farben sind Javafarben unveränderlich.
 * Wenn Sie andere Farben benötigen verwenden sie die Javaklasse.
 * {@code new Color(rot, gruen, blau)}
 * 
 * Um eine Farbe aus diesem Pool zu verwenden schreiben sie zum Beispiel
 * 
 * {@code Farbe.gruen}
 * 
 * Wenn sie {@code import static eos.Farbe.*;} am Anfang der Klasse ergänzen genügt
 * es {@code gruen} zu schreiben.
 * 
 */
public class Farbe {
	private MutableColor c;
    public static final Farbe gelb      = new Farbe(MutableColor.YELLOW);
    public static final Farbe rot       = new Farbe(MutableColor.RED);
    public static final Farbe gruen     = new Farbe(MutableColor.GREEN);
    public static final Farbe blau      = new Farbe(MutableColor.BLUE);
    public static final Farbe weiss     = new Farbe(MutableColor.WHITE);
    public static final Farbe schwarz   = new Farbe(MutableColor.BLACK);
    public static final Farbe braun     = new Farbe(118,80,8);
    public static final Farbe hellblau  = new Farbe(123,177,244);
    public static final Farbe hellgruen = new Farbe(87,225,4);
    public static final Farbe grau      = new Farbe(MutableColor.GRAY);
    public static final Farbe hellgrau  = new Farbe(MutableColor.LIGHT_GRAY);
    public static final Farbe pink      = new Farbe(245,0,250);
    public static final Farbe orange    = new Farbe(255,128,0);
    public static final Farbe weinrot   = new Farbe(128,0,0);
    public static final Farbe tuerkis   = new Farbe(0,255,255);
    public Farbe(int r, int g, int b) {
    	c = new MutableColor(r,g,b);
    }
    public Farbe(int r, int g, int b, int a) {
    	c = new MutableColor(r,g,b,a);
    }
    /*package*/ MutableColor getColor() {
    	return c;
    }
    /*package*/ Farbe(MutableColor c) {
    	this.c = c;
    }
}
