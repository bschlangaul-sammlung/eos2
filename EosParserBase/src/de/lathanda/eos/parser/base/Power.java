package de.lathanda.eos.parser.base;

import java.util.ArrayList;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.commands.PowD;
import de.lathanda.eos.vm.commands.PowI;

/**
 * Speichert und behandelt eine Hoch Operation.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class Power extends Expression {
	private final Expression left;
	private final Expression right;

	public Power(Expression left, Expression right) {
		this.left = left;
		this.right = right;
		prio = 7;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		left.compile(ops, autoWindow);
		right.compile(ops, autoWindow);
		if (type.isInteger()) {
			ops.add(new PowI());
		} else {
			ops.add(new PowD());
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
		return "(" + left + "^" + right + ")";
	}

	@Override
	public String getLabel() {
		return createText("Pow.Label", getLabelLeft(left), getLabelRight(right));
	}
}
