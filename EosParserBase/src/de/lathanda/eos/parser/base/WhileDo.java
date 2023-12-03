package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.Loop;
import de.lathanda.eos.baseparser.Sequence;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.DebugPoint;
import de.lathanda.eos.vm.commands.Jump;
import de.lathanda.eos.vm.commands.JumpIf;

import java.util.ArrayList;

/**
 * Speichert und behandelt eine bedingte Schleife.
 *
 * @author Peter (Lathanda) Schneider
 * 
 */
public class WhileDo extends Loop {
	public WhileDo(Expression condition, Sequence sequence) {
		super(sequence, condition);
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ArrayList<Command> cond = new ArrayList<>();
		ArrayList<Command> body = new ArrayList<>();
		cond.add(new DebugPoint(condition.getMarker()));
		condition.compile(cond, autoWindow);
		sequence.compile(body, autoWindow);
		ops.add(new Jump(body.size() + 1));
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
		return "do\n" + sequence + "while " + condition;
	}

	@Override
	public String getLabel() {
		return createText("DoWhile.Label", condition.getLabel());
	}

	@Override
	public boolean isPre() {
		return true;
	}
}
