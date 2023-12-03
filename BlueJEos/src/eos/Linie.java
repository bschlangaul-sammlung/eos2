package eos;

import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.geo.Line;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse Linie.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Linie extends StrichFigur {
	private final Line line;

	public Linie() {
		super(new Line());
		line = (Line)figure;
	}

	public void linienStaerkeSetzen(double staerke) {
		randstaerkeSetzen(staerke);
	}

	public double linienStaerkeLesen() {
		return randstaerkeLesen();
	}

	public void linienartSetzen(LineStyle linienart) {
		line.setLineStyle(linienart);
	}
	public LineStyle linienartLesen() {
		return line.getLineStyle();
	}
	public void farbeSetzen(Farbe farbe) {
		line.setLineColor(farbe.getColor());
	}

	public Farbe farbeLesen() {
		return new Farbe(line.getLineColor());
	}

	public void x1Setzen(double x) {
		line.setX1(x);
	}

	public double x1Lesen() {
		return line.getX1();
	}

	public void x2Setzen(double x) {
		line.setX2(x);
	}

	public double x2Lesen() {
		return line.getX2();
	}
	public void y1Setzen(double y) {
		line.setY1(y);
	}

	public double y1Lesen() {
		return line.getY1();
	}

	public void y2Setzen(double y) {
		line.setY2(y);
	}

	public double y2Lesen() {
		return line.getY2();
	}

	public void punkt1Setzen(double x, double y) {
		line.setPoint1(x, y);
	}

	public void punkt2Setzen(double x, double y) {
		line.setPoint2(x, y);
	}

	public void endpunkteSetzen(double x1, double y1, double x2, double y2) {
		line.setPoints(x1, y1, x2, y2);
	}
}
