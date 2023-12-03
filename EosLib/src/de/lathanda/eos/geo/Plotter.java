package de.lathanda.eos.geo;

import java.util.Iterator;
import de.lathanda.eos.base.FillDescriptor;
import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineDescriptor;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.MutableColor;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;

/**
 * Der Plotter (Turtle) dient dazu Polygone zu zeichnen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Plotter extends Group {

	Pen pen;

	private boolean isPlotting = true;
	private Polygon polygon;

	public Plotter() {
		pen = new Pen();
		addFigure(pen);
		startPlot();
	}

	@Override
	protected void drawObject(Picture p) {
		Iterator<Figure> fit = members.iterator();
		fit.next(); //pen
   		while(fit.hasNext()) {
   			fit.next().draw(p);
   		}
		pen.draw(p);
	}

	public void turnLeft(double angle) {
		pen.rotate(angle);
	}

	public void turnRight(double angle) {
		pen.rotate(-angle);
	}

	public void turnTo(double angle) {
		pen.setRotation(angle);
	}

	private synchronized void appendPosition() {
		if (isPlotting) {
			polygon.addPoint(pen.getPoint());
		}
	}

	private void finishPlot() {
		isPlotting = false;
		if (!polygon.isValid()) {
			this.removeFigure(polygon);
		}
		fireDataChanged();
	}

	private void startPlot() {
		if (polygon == null) {
			polygon = new Polygon();
			polygon.setLineColor(MutableColor.BLACK);
			polygon.setFillStyle(FillStyle.TRANSPARENT);
		} else {
			Polygon newPolygon = new Polygon();
			newPolygon.fill = new FillDescriptor(polygon.fill);
			newPolygon.line = new LineDescriptor(polygon.line);
			if (!polygon.isValid()) {
				//check if polygon is valid before switching to new one
				this.removeFigure(polygon);
			}
			polygon = newPolygon;
		}
		addFigure(polygon);
		isPlotting = true;
		appendPosition();
	}
	public double getPenX() {
		return pen.getAbsolutePosition().getX();
	}
	public double getPenY() {
		return pen.getAbsolutePosition().getY();
	}
	public void setPenX(double x) {
		Point loc = pen.getRelativePosition(new Point(x, pen.getAbsolutePosition().getY()));
		pen.moveTo(loc.getX(), loc.getY());
		appendPosition();
	}
	public void setPenY(double y) {
		Point loc = pen.getRelativePosition(new Point(pen.getAbsolutePosition().getX(), y));
		pen.moveTo(loc.getX(), loc.getY());
		appendPosition();
	}

	public void movePenTo(double x, double y) {
		Point loc = pen.getRelativePosition(new Point(x, y));
		pen.moveTo(loc.getX(), loc.getY());
		appendPosition();
	}

	public double getAngle() {
		return pen.getRotation();
	}

	public void setAngle(double angle) {
		pen.setRotation(angle);
		fireDataChanged();
	}

	public void moveForward(double length) {
		pen.move(Math.cos(pen.getRotationInternal()) * length, Math.sin(pen.getRotationInternal()) * length);
		appendPosition();
	}

	public void moveBackward(double length) {
		moveForward(-length);
	}

	public void startPlotting() {
		if (!isPlotting) {
			startPlot();
		}
	}

	public void stopPlotting() {
		if (isPlotting) {
			finishPlot();
		}
	}

	public void setPenVisible(boolean b) {
		pen.setVisible(b);
		fireDataChanged();
	}

	public boolean getPenVisible() {
		return pen.getVisible();
	}

	public void setPenColor(MutableColor c) {
		pen.setLineColor(c);
	}
	public boolean isPlotting() {
		return isPlotting;
	}
	public MutableColor getPenColor() {
		return polygon.getLineColor();
	}
    @Override
    public Figure copy() {
        Plotter g = (Plotter)super.copy();
        g.polygon = (Polygon)polygon.copy();
        g.pen = (Pen)pen.copy();
        return g;
    }
	@Override
	public void setLineColor(MutableColor color) {
		startPlot();
		polygon.setLineColor(color);
	}

	public MutableColor getLineColor() {
		return polygon.getLineColor();
	}

	@Override
	public void setLineStyle(LineStyle linestyle) {
		startPlot();
		polygon.setLineStyle(linestyle);
	}

	@Override
	public void setLineWidth(double linewidth) {
		startPlot();
		polygon.setLineWidth(linewidth);
	}
	public double getLineWidth() {
		return polygon.getLineWidth();
	}
	@Override
	public void setFillStyle(FillStyle fillstyle) {
		startPlot();
		polygon.setFillStyle(fillstyle);
	}

	@Override
	public void setFillColor(MutableColor color) {
		startPlot();
		polygon.setFillColor(color);
	}   
	/**
	 * Löscht die Zeichnung
	 */
	public void clearAll() {
		super.removeAll();
		pen = new Pen();
		addFigure(pen);
		startPlot();
	}
}
