package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.commands.LE;

import java.util.ArrayList;

/**
 * Speichert und behandelt einen wengier oder gleich Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class IsLessEquals extends Expression {

	private final Expression left;
	private final Expression right;

	public IsLessEquals(Expression left, Expression right) {
		this.left = left;
		this.right = right;
		type = Type.getBoolean();
		prio = 3;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		left.compile(ops, autoWindow);
		right.compile(ops, autoWindow);
		ops.add(new LE());
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		left.resolveNamesAndTypes(with, env);
		right.resolveNamesAndTypes(with, env);
		if (!left.getType().isNumber() || !right.getType().isNumber()) {
			env.addError(marker, "CompareType", left.getType(), right.getType());
		}
	}

	@Override
	public String toString() {
		return "(" + left + " <= " + right + ")";
	}

	@Override
	public String getLabel() {
		return createText("LessEquals.Label", getLabelLeft(left), getLabelRight(right));
	}
}
