package eos;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.geo.Group;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse GRUPPE.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 * 
 */
public class Gruppe extends Figur {
	private final Group group;

	public Gruppe() {
		super(new Group());
		group = (Group)figure;
	}

	public void randfarbeSetzen(Farbe farbe) {
		group.setLineColor(farbe.getColor());
	}

	public void randartSetzen(LineStyle randart) {
		group.setLineStyle(randart);
	}

	public void randstaerkeSetzen(double randstaerke) {
		group.setLineWidth(randstaerke);
	}

	public void fuellartSetzen(FillStyle fuellart) {
		group.setFillStyle(fuellart);
	}

	public void fuellfarbeSetzen(Farbe farbe) {
		group.setFillColor(farbe.getColor());
	}

	public void schlucke(Figur figur) {
		group.addFigure(figur.figure);
	}

	public void hinzufuegen(Figur figur) {
		group.addFigure(figur.figure);
	}
	
	public void kopiere(Figur figur) {
		group.copyFigure(figur.figure);
	}

	public void spiegleX() {
		group.mirrorX();
	}

	public void spiegleY() {
		group.mirrorY();
	}

	public void zentrumSetzen(double x, double y) {
		group.setCenter(x, y);
	}
}
