package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.commands.NEQ;

import java.util.ArrayList;

/**
 * Speichert und behandelt einen logischen Ungleich-Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class IsNotEqual extends Expression {

	private final Expression left;
	private final Expression right;

	public IsNotEqual(Expression left, Expression right) {
		this.left = left;
		this.right = right;
		type = Type.getBoolean();
		prio = 3;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		left.compile(ops, autoWindow);
		right.compile(ops, autoWindow);
		ops.add(new NEQ());
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		left.resolveNamesAndTypes(with, env);
		right.resolveNamesAndTypes(with, env);
	}

	@Override
	public String toString() {
		return "(" + left + " != " + right + ")";
	}

	@Override
	public String getLabel() {
		return createText("NotEqual.Label", getLabelLeft(left), getLabelRight(right));
	}
}
