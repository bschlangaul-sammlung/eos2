package de.lathanda.eos.geo.gui.event;

import de.lathanda.eos.base.math.Point;

/**
 * Die Maus hat sich innerhalb des Fensters bewegt.
 *
 * @author Peter (Lathanda) Schneider
 */

public interface CursorListener {
    void cursorMoved(Point p);

	void cursorUp(Point p);

	void cursorDown(Point p);
}
