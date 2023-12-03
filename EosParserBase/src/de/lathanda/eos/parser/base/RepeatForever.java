package de.lathanda.eos.parser.base;

import java.util.ArrayList;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.Loop;
import de.lathanda.eos.baseparser.LoopForeverUnit;
import de.lathanda.eos.baseparser.Sequence;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.DebugPoint;
import de.lathanda.eos.vm.commands.Jump;

/**
 * Speichert und behandelt eine Endlosschleife.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class RepeatForever extends Loop implements LoopForeverUnit {
	public RepeatForever(Sequence sequence) {
		super(sequence, null);
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ArrayList<Command> body = new ArrayList<>();
		sequence.compile(body, autoWindow);
		ops.addAll(body);
		ops.add(new DebugPoint());
		ops.add(new Jump(-(1 + body.size())));
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		sequence.resolveNamesAndTypes(with, env);
	}

	@Override
	public String toString() {
		return "repeat forever\n" + sequence + "endrepeat";
	}

	@Override
	public String getLabel() {
		return createText("RepeatForever.Label");
	}

	@Override
	public boolean isPre() {
		return true;
	}
}
