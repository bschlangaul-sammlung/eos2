package de.lathanda.eos.gui.objectchart;

import java.awt.Color;
import java.text.MessageFormat;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.Scaling;
import de.lathanda.eos.base.util.ConcurrentLinkedList;
import de.lathanda.eos.baseparser.Program;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.config.Language;
import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.gui.diagram.TextUnit;
import de.lathanda.eos.vm.MemoryEntry;
import de.lathanda.eos.vm.ReservedVariables;

public class Toolkit {

	public static Unit formatValue(Object value) {
		if (value == null) {
			return new TextUnit(Language.def.getLabel("1null"));
		}
		if (value instanceof Boolean) {
			return new TextUnit(Language.def.getLabel(value.toString()));
		} else if (value instanceof Integer) {
			return new TextUnit(value.toString());
		} else if (value instanceof Number) {
			return new TextUnit(MessageFormat.format("{0,number,#.####}", value));
		} else if (value instanceof ConcurrentLinkedList<?>) {
			return new TextUnit(buildValue((ConcurrentLinkedList<?>) value));
		} else if (value instanceof Color) {
			return new ColorValue((Color) value);
		} else if (value instanceof LineStyle) {
			return new LineStyleValue((LineStyle) value);
		} else if (value instanceof FillStyle) {
			return new FillStyleValue((FillStyle) value);
		} else if (value instanceof Alignment) {
			return new AlignmentValue((Alignment) value);
		} else if (value instanceof Scaling) {
			return new ScalingValue((Scaling) value);
		} else {
			return new TextUnit(value.toString());
		}
	}

	public static Unit create(String name, String label, Object data, Type type) {
		if (name.equals(ReservedVariables.RESULT)) {
			return new Property(Language.def.getLabel(ReservedVariables.RESULT), data);
		} else if (name.equals(ReservedVariables.WINDOW)) {
			return new ObjectCard(Language.def.getLabel(ReservedVariables.WINDOW), label, data, type);
		} else if (name.startsWith(ReservedVariables.REPEAT_TIMES_INDEX)) {
			return new Property(Language.def.getLabel(ReservedVariables.REPEAT_TIMES_INDEX), data);
		} else if (data instanceof Number || data instanceof String || data instanceof Boolean) {
			return new Property(name, data);
		} else {
			return new ObjectCard(name, label, data, type);
		}
	}

	private static String buildValue(ConcurrentLinkedList<?> data) {
		return MessageFormat.format(Language.def.getLabel(ReservedVariables.COUNT), data.getLength());
	}

	public static Unit create(MemoryEntry v, Program program) {
		// TODO Auto-generated method stub
		return null;
	}
}
