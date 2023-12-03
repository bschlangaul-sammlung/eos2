package de.lathanda.eos.parser.base;


import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.parser.core.Sequence;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.DebugPoint;
import de.lathanda.eos.vm.commands.JumpIf;

import java.util.ArrayList;

/**
 * Speichert und behandelt eine bedingte Schleife.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class DoWhile extends Loop {

	public DoWhile(Sequence sequence, Expression condition) {
		super(sequence, condition);
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ArrayList<Command> cond = new ArrayList<>();
		cond.add(new DebugPoint(condition.getMarker()));
		ArrayList<Command> body = new ArrayList<>();
		condition.compile(cond, autoWindow);
		sequence.compile(body, autoWindow);
		ops.addAll(body);
		ops.addAll(cond);
		ops.add(new JumpIf(-(cond.size() + body.size())));
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		sequence.resolveNamesAndTypes(with, env);
		condition.resolveNamesAndTypes(with, env);
		if (!condition.getType().isBoolean()) {
			env.addError(marker, "ConditionType", condition.getType());
		}
	}

	@Override
	public String toString() {
		return "while" + condition + "\n" + sequence + "endwhile";
	}

	@Override
	public String getLabel() {
		return createText("DoWhile.Label", condition.getLabel());
	}

	@Override
	public boolean isPre() {
		return false;
	}

}
