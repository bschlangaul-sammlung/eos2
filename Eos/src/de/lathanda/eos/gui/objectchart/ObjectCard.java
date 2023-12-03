package de.lathanda.eos.gui.objectchart;

import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import de.lathanda.eos.baseparser.PropertyViewer;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.TextUnit;

/**
 * Objektkarte
 *
 * @author Peter (Lathanda) Schneider
 */
public class ObjectCard extends Unit {
	private TextUnit header;
	private LinkedList<Property> properties;
	private float yLine;

	public ObjectCard(String name, String label, Object target, Type type) {
		Map<TextAttribute, Object> map = new Hashtable<>();
		map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		header = new TextUnit(name + ":" +label);
		header.setFont(HEADER_FONT.deriveFont(map));
		properties = new LinkedList<>();
		LinkedList<PropertyViewer> attr = type.getPropertyViewers(target);
		for (PropertyViewer a : attr) {
			properties.add(new Property(a.name, a.getValue()));
		}
	}

	@Override
	public void drawUnit(Drawing d) {
		d.drawRoundRect(0, 0, width, height, 5);
		d.drawLine(0, yLine, width, yLine);
		header.draw(d);
		for (Unit property : properties) {
			property.draw(d);
		}
	}

	@Override
	public void layoutUnit(Drawing d) {
		header.layout(d);
		for (Unit property : properties) {
			property.layout(d);
		}
		header.setOffsetY(INDENT);
		header.setOffsetX(0);
		yLine = header.getHeight() + 2 * INDENT;
		if (properties.isEmpty()) {
			width = header.getWidth() + 2 * INDENT;
			height = yLine;
		} else {
			float alignedWidth = Property.align(d, properties);
			width = Math.max(header.getWidth(), alignedWidth) + 2 * INDENT;
			float y = yLine + INDENT;
			for (Unit property : properties) {
				property.setOffsetX(INDENT);
				property.setOffsetY(y);
				y += property.getHeight() + INDENT;
			}
			height = y;
		}
		header.setWidth(width);
		header.alignCentralX();
	}
}
