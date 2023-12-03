package de.lathanda.eos.gui.structogram;

import java.awt.Color;
import java.util.ArrayList;

import de.lathanda.eos.baseparser.ProgramSequence;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.Unit;

/**
 * Befehlssequenz.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Sequence extends Unit {
	private ArrayList<Unit> units;

	Sequence(ProgramSequence programSequence) {
		units = new ArrayList<>();
		if (programSequence != null) {
			programSequence.getInstructions().stream().forEachOrdered((n) -> {
				units.add(Toolkit.create(n));
			});
		}
	}

	@Override
	public void drawUnit(Drawing d) {
		d.setColor(Color.BLACK);
		d.drawRect(0, 0, width, height);
		units.stream().forEachOrdered(p -> p.draw(d));
	}

	@Override
	public void layoutUnit(Drawing d) {
		units.forEach(p -> p.layout(d));
		float maxw = 0;
		float h = 0;
		for (Unit u : units) {
			if (maxw < u.getWidth()) {
				maxw = u.getWidth();
			}
			u.setOffsetY(h);
			h = h + u.getHeight();
		}
		height = h;
		width = maxw;
		for (Unit u : units) {
			u.setWidth(maxw);
		}
	}

	@Override
	public void setWidth(float width) {
		super.setWidth(width);
		units.stream().forEachOrdered(u -> u.setWidth(width));
	}
}
