package de.lathanda.eos.config;

import java.awt.Color;
import java.lang.reflect.Modifier;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.util.Direction;
import de.lathanda.eos.vm.ObjectSource;

public class ObjectFactory {
	public ObjectSource getObjectSource(LangClass lc) throws ClassNotFoundException {
		switch (lc.id) {
		case "alignment":
			return () -> Alignment.CENTER;
		case "boolean":
			return () -> false;
		case "color":
			return () -> Color.BLACK;
		case "real":
			return () -> 0d;
		case "fillstyle":
			return () -> FillStyle.FILLED;
		case "integer":
			return () -> 0;
		case "linestyle":
			return () -> LineStyle.SOLID;
		case "direction":
			return () -> Direction.NORTH;
		default:
			if (lc.javaclass.equals("?")) {
				return null;
			} else {
				Class<?> cls = Class.forName(lc.javaclass);
				if (Modifier.isAbstract(cls.getModifiers())) {
					return null;
				} else {
					return new DefaultObjectSource(lc.javaclass);
				}
			}
		}
	}
}
