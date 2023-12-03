package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.commands.Not;

import java.util.ArrayList;

/**
 * Speichert und behandelt einen logischen Nicht-Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class LogicalNot extends Expression {

	private final Expression exp;

	public LogicalNot(Expression exp) {
		this.exp = exp;
		type = Type.getBoolean();
		prio = 6;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		exp.compile(ops, autoWindow);
		ops.add(new Not());
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		exp.resolveNamesAndTypes(with, env);
		if (!exp.getType().isBoolean()) {
			env.addError(marker, "InvalidBoolean", exp.getType());
		}
	}

	@Override
	public String toString() {
		return "not" + exp;
	}

	@Override
	public String getLabel() {
		return createText("Not.Label", getLabelRight(exp));
	}
}
