package eos;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.geo.FilledFigure;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse GEFÜLLTEFIGUR.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 * 
 */
public abstract class GefuellteFigur extends StrichFigur {
	private final FilledFigure filled;

	public GefuellteFigur(FilledFigure figure) {
		super(figure);
		filled = (FilledFigure)figure;
	}

	public void fuellartSetzen(FillStyle fuellart) {
		filled.setFillStyle(fuellart);
	}

	public FillStyle fuellartLesen() {
		return filled.getFillStyle();
	}

	public void fuellfarbeSetzen(Farbe farbe) {
		filled.setFillColor(farbe.getColor());
	}

	public Farbe fuellfarbeLesen() {
		return new Farbe(filled.getFillColor());
	}
}
