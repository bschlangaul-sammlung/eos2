package de.lathanda.eos.geo;

import de.lathanda.eos.base.MutableColor;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.event.CleanupListener;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.util.ConcurrentLinkedList;
import de.lathanda.eos.geo.gui.ViewFrame;
import de.lathanda.eos.geo.gui.event.CursorListener;
import de.lathanda.eos.geo.gui.event.FigureListener;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.LinkedList;

/**
 * Das Fenster stellt alle Figuren dar.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Window implements FigureGroup, CleanupListener, CursorListener, ComponentListener  {
    ViewFrame vf;
    ChangeMultiCaster cmc;
    protected ConcurrentLinkedList<Figure> members;
    protected Point cursor = new Point(0,0);
    protected boolean cursorDown = false;
    protected boolean cursorClick = false;

    public Window() {
        members = new ConcurrentLinkedList<Figure>();
        cmc = new ChangeMultiCaster();
        vf = new ViewFrame(this);
        vf.addCursorListener(this);
        vf.addComponentListener(this);
        vf.setVisible(true);
    }                          

    @Override
    public void addFigure(Figure figure) {
        figure.replaceGroup(this);
        members.add(figure);
        cmc.fireDataChanged();
    }

    @Override
    public void removeFigure(Figure figure) {
        members.remove(figure);
    }

    @Override
    public Group getGroup() {
        return null;
    }
    @Override
    public FigureGroup getParentGroup() {
        return null;
    }  
    public void draw(Picture g) {
    	for (Figure m : members) {
    		m.draw(g);
    	}
    }
    public void addFigureListener(FigureListener gol) {
        cmc.add(gol);
    }
    public void removeFigureListener(FigureListener gol) {
        cmc.remove(gol);
    }

    @Override
    public void fireDataChanged() {
        cmc.fireDataChanged();
    }

    public void setGridColor(MutableColor color) {
        vf.setGridColor(color);
        fireDataChanged();
    }

    public MutableColor getGridColor() {
        return vf.getGridColor();
    }

    public void setBackgroundColor(MutableColor color) {
        vf.setBackgroundColor(color);
    }

    public MutableColor getBackgroundColor() {
        return vf.getBackgroundColor();
    }

    public void setHeight(double height) {
        vf.setHeightMM(height);
    }

    public double getHeight() {
        return vf.getHeightMM();
    }

    public void setWidth(double width) {
        vf.setWidthMM(width);
    }

    public double getWidth() {
        return vf.getWidthMM();
    }

    public void setTop(double top) {
        vf.setTop(top);
    }

    public double getTop() {
        return vf.getTop();
    }

    public void setLeft(double left) {
        vf.setLeft(left);
    }

    public double getLeft() {
        return vf.getLeft();
    }
    
    public void setCenter(double x, double y) {
    	vf.setCenter(x, y);
    }

    public void setZoom(double zoom) {
    	vf.setZoom(zoom);
    }
    public void setGridWidth(double gridwidth) {
        vf.setGridWidth(gridwidth);
    }

    public double getGridWidth() {
        return vf.getGridWidth();
    }

    public void move(double dx, double dy) {
        vf.move(dx, dy);
    }

    public void setGridVisible(boolean b) {
        vf.setGridVisible(b);
    }
    public boolean getGridVisible() {
        return vf.getGridVisible();
    }
    public void turnGridOn() {
        vf.setGridVisible(true);
    }
    public void turnGridOff() {
        vf.setGridVisible(false);
    }
    public void setTitle(String title) {
        vf.setTitle(title);
    }

    public String getTitle() {
        return vf.getTitle();
    }
    private class ChangeMultiCaster {
        private final LinkedList<FigureListener> figureListener;
        protected ChangeMultiCaster() {
            figureListener = new LinkedList<>();
        }
        void add(FigureListener cl) {
            figureListener.add(cl);
        }
        void remove(FigureListener cl) {
            figureListener.remove(cl);
        }
        void fireDataChanged() {
            figureListener.forEach((cl) -> {
                cl.dataChanged();
            });
        }        
    }
    @Override
    public void terminate() {
        if (vf != null) {
            vf.dispose();
            vf = null;
        }
        cmc = null;
        members.clear();
        members = null;
    }

	public boolean isCursorClick() {
		if (cursorClick) {
			cursorClick = false;
			return cursorClick;
		}
		return false;
	}
	public boolean isCursorDown() {
		return cursorDown;
	}
	public double getCursorX() {
		return cursor.getX();
	}
	public double getCursorY() {
		return cursor.getY();
	}

	@Override
	public void cursorMoved(Point p) {
		cursor = p;
	}

	@Override
	public void cursorUp(Point p) {
		cursorDown = false;
		cursor = p;
	}

	@Override
	public void cursorDown(Point p) {
		cursorDown = true;
		cursorClick = true;
		cursor = p;
	}

	@Override
	public void componentHidden(ComponentEvent ce) {}

	@Override
	public void componentMoved(ComponentEvent ce) {
		cmc.fireDataChanged();		
	}

	@Override
	public void componentResized(ComponentEvent ce) {
		cmc.fireDataChanged();		
	}

	@Override
	public void componentShown(ComponentEvent ce) {}

}
