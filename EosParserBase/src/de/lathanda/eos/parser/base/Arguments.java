package de.lathanda.eos.parser.base;

import java.util.ArrayList;
import java.util.LinkedList;

import de.lathanda.eos.vm.DebugPoint;
import de.lathanda.eos.parser.core.AutoCompleteType;
import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.parser.core.Node;
import de.lathanda.eos.parser.core.Type;
import de.lathanda.eos.vm.Command;
/**
 * Speichert und behandelt eine Argumentliste.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class Arguments extends Node {

	private final LinkedList<Expression> arguments;
	private Type[] types;

	public Arguments() {
		arguments = new LinkedList<>();
	}

	public void append(Expression expression) {
		arguments.add(expression);
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		for (int i = arguments.size(); i-- > 0;) {
			ops.add(new DebugPoint(arguments.get(i).getMarker()));
			arguments.get(i).compile(ops, autoWindow);
		}
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		types = new Type[arguments.size()];
		for (int i = 0; i < arguments.size(); i++) {
			arguments.get(i).resolveNamesAndTypes(with, env);
			types[i] = arguments.get(i).getType();
		}
	}

	public Type[] getTypes() {
		return types;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		boolean first = true;
		res.append("(");
		for (AutoCompleteType t : types) {
			if (first) {
				res.append(t);
			} else {
				res.append(",").append(t);
				first = false;
			}
		}
		res.append(")");
		return res.toString();
	}

	@Override
	public String getLabel() {
		StringBuilder text = new StringBuilder();
		text.append("(");
		boolean first = true;
		for (Expression e : arguments) {
			if (first) {
				text.append(e.getLabel());
				first = false;
			} else {
				text.append(", ").append(e.getLabel());
			}
		}
		text.append(")");
		return text.toString();
	}
}
