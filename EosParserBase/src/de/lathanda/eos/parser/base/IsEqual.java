package de.lathanda.eos.parser.base;

import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.parser.core.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.commands.EQ;

import java.util.ArrayList;

/**
 * Speichert und behandelt einen Test auf Gleichheit.
 *
 * @author Peter (Lathanda) Schneider
 * 
 */
public class IsEqual extends Expression {
	private final Expression left;
	private final Expression right;

	public IsEqual(Expression left, Expression right) {
		this.left = left;
		this.right = right;
		type = Type.getBoolean();
		prio = 2;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		left.compile(ops, autoWindow);
		right.compile(ops, autoWindow);
		ops.add(new EQ());
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		left.resolveNamesAndTypes(with, env);
		right.resolveNamesAndTypes(with, env);
	}

	@Override
	public String toString() {
		return "(" + left + " == " + right + ")";
	}

	@Override
	public String getLabel() {
		return createText("Equals.Label", getLabelLeft(left), getLabelRight(right));
	}
}
