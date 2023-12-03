package de.lathanda.eos.geo;

import de.lathanda.eos.base.LineDescriptor;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.MutableColor;
import de.lathanda.eos.base.Picture;

/**
 * Basisklasse für alle Figuren mit Rand.
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class LineFigure extends Figure {
    protected LineDescriptor line;
    public LineFigure() {
        line = new LineDescriptor();
    }

    @Override
    public Figure copy() {
        LineFigure linefigure = (LineFigure)super.copy();
        linefigure.line = new LineDescriptor(line);
        return linefigure;
    }

    @Override
    protected void beforeDrawing(Picture p) {
        p.setLine(line);
    }

    public void setLineColor(MutableColor color) {
        line.setColor(color);
        fireDataChanged();
    }

    public MutableColor getLineColor() {
        return line.getColor();
    }

    public void setLineStyle(LineStyle linestyle) {
        line.setLineStyle(linestyle);
        fireDataChanged();
    }

    public LineStyle getLineStyle() {
        return line.getLineStyle();
    }

    public void setLineWidth(double width) {
        line.setDrawWidth(width);
        fireDataChanged();
    }

    public double getLineWidth() {
        return line.getDrawWidth();
    }
    public LineDescriptor getLineDescriptor() {
        return line;
    }
}
