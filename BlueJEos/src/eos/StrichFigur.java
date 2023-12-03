package eos;

import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.geo.LineFigure;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht keiner EOS Klasse. Sie ist die Oberklasse
 * aller Figuren die einen Rand haben.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 * 
 */
public abstract class StrichFigur extends Figur {
	private final LineFigure line;
    protected StrichFigur(LineFigure figure) {
    	super(figure);
    	line = (LineFigure)figure;
    }
    
    public void randfarbeSetzen(Farbe farbe) {
    	line.setLineColor(farbe.getColor());
    }
    public Farbe randfarbeLesen() {
        return new Farbe(line.getLineColor());
    }
    public void randartSetzen(LineStyle randart) {
    	line.setLineStyle(randart);
    }
    public LineStyle randartLesen() {
        return line.getLineStyle();
    }
    public void randstaerkeSetzen(double randstaerke) {
    	line.setLineWidth(randstaerke);
    }
    public double randstaerkeLesen() {
        return line.getLineWidth();
    }
}
