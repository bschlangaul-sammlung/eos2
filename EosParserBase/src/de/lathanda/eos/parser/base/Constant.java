package de.lathanda.eos.parser.base;


import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.parser.core.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.commands.LoadConstant;

import java.util.ArrayList;

/**
 * Speichert und behandelt einen konstanten Wert.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class Constant extends Expression {

	private final Object value;
	private final String image;

	public Constant(Object value, Type type) {
		this.value = value;
		this.type = type;
		this.image = value.toString();
		prio = 1000;
	}

	public Constant(Object value, Type type, String image) {
		this.value = value;
		this.type = type;
		this.image = image;
		prio = 1000;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ops.add(new LoadConstant(value));
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		// nothing to do
	}

	@Override
	public String getLabel() {
		return image;
	}

}
