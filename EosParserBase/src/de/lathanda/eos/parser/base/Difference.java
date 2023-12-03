package de.lathanda.eos.parser.base;

import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.commands.SubtractD;
import de.lathanda.eos.vm.commands.SubtractI;

import java.util.ArrayList;

/**
 * Speichert und behandelt einen Differenz-Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class Difference extends Expression {

	private final Expression left;
	private final Expression right;

	public Difference(Expression left, Expression right) {
		this.left = left;
		this.right = right;
		prio = 4;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		left.compile(ops, autoWindow);
		right.compile(ops, autoWindow);
		if (type.isInteger()) {
			ops.add(new SubtractI());
		} else {
			ops.add(new SubtractD());
		}
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		left.resolveNamesAndTypes(with, env);
		right.resolveNamesAndTypes(with, env);
		if (!left.getType().isNumber() || !right.getType().isNumber()) {
			env.addError(marker, "NumberType", left.getType(), right.getType());
		}
		type = left.getType().mergeTypes(right.getType());
	}

	@Override
	public String toString() {
		return "(" + left + "-" + right + ")";
	}

	@Override
	public String getLabel() {
		return createText("Difference.Label", getLabelLeft(left), getLabelRight(right));
	}

}
