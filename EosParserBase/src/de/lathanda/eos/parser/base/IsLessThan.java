package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.commands.LT;

import java.util.ArrayList;

/**
 * Speichert und behandelt einen weniger als Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class IsLessThan extends Expression {

	private final Expression left;
	private final Expression right;

	public IsLessThan(Expression left, Expression right) {
		this.left = left;
		this.right = right;
		type = Type.getBoolean();
		prio = 3;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		left.compile(ops, autoWindow);
		right.compile(ops, autoWindow);
		ops.add(new LT());
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
		return "(" + left + " < " + right + ")";
	}

	@Override
	public String getLabel() {
		return createText("LessThan.Label", getLabelLeft(left), getLabelRight(right));
	}
}
