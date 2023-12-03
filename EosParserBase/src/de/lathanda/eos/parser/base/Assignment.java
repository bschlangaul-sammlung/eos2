package de.lathanda.eos.parser.base;

import java.util.ArrayList;

import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.parser.core.Node;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.DebugPoint;

/**
 * Speichert und behandelt eine Zuweisung.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Assignment extends Node {

	private final Expression right;
	private final PropertyWrite left;

	public Assignment(PropertyWrite left, Expression right) {
		this.left = left;
		this.right = right;
	}

	public Expression getRight() {
		return right;
	}

	public PropertyWrite getLeft() {
		return left;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ops.add(new DebugPoint(marker));
		right.compile(ops, autoWindow);
		left.compile(ops, autoWindow);
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		right.resolveNamesAndTypes(with, env);
		left.resolveNamesAndTypes(with, env);
		if (!left.getType().checkType(right.getType())) {
			env.addError(marker, "InvalidTypeCast", right.getType(), left.getType());
		}
	}

	@Override
	public String toString() {
		return left + " = " + right;
	}

	@Override
	public String getLabel() {
		return createText("Assignment.Label", left.getLabel(), right.getLabel());
	}

}
