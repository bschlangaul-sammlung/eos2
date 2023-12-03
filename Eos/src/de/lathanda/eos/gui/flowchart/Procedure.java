package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.baseparser.ProgramSequence;
import de.lathanda.eos.gui.diagram.Drawing;

/**
 * Unterprogramm.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Procedure extends ConnectedUnit {
	private final Sequence sequence;
	private final String title;
	private float textx;
	private float texty;
	private float arrowxE;
	private float arrowyE;
	private float arrowxB;
	private float arrowyB;

	public Procedure(String title, ProgramSequence programSequence) {
		this.title = title;
		sequence = new Sequence(programSequence);
		font = HEADER_FONT;
	}

	@Override
	public void layoutUnit(Drawing d) {
		sequence.layout(d);
		float textwidth = d.stringWidth(title);
		float textheight = d.getHeight();
		if (textwidth + 2 * BORDER > sequence.getWidth()) {
			width = textwidth + 2 * BORDER;
		} else {
			width = sequence.getWidth();
		}
		texty = d.getAscent() + BORDER;
		textx = (width - textwidth) / 2;
		height = sequence.getHeight() + textheight + 2 * BORDER + 2 * SPACE;
		arrowyE = sequence.getHeight() + textheight + 2 * BORDER + SPACE;
		arrowxE = width / 2;
		arrowxB = arrowxE;
		arrowyB = textheight + 2 * BORDER;
		sequence.setOffsetX((width - sequence.getWidth()) / 2);
		sequence.setOffsetY(textheight + 2 * BORDER + SPACE);
	}

	@Override
	public void drawUnit(Drawing d) {
		d.drawString(title, textx, texty);
		sequence.draw(d);

		if (!sequence.neverEnds) {
			d.drawArrow(arrowxE, arrowyE, arrowxE, arrowyE + SPACE, 2);
		}

		if (sequence.needsIncomingArrow) {
			d.drawArrow(arrowxB, arrowyB, arrowxB, arrowyB + SPACE, 2);
		} else {
			d.drawLine(arrowxB, arrowyB, arrowxB, arrowyB + SPACE);
		}
	}
}
