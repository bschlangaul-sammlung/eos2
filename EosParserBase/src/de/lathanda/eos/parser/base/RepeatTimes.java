package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.Loop;
import de.lathanda.eos.baseparser.LoopTimesUnit;
import de.lathanda.eos.baseparser.Sequence;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.DebugPoint;
import de.lathanda.eos.vm.ReservedVariables;
import de.lathanda.eos.vm.commands.CreateVariable;
import de.lathanda.eos.vm.commands.GE;
import de.lathanda.eos.vm.commands.Jump;
import de.lathanda.eos.vm.commands.JumpIf;
import de.lathanda.eos.vm.commands.LoadConstant;
import de.lathanda.eos.vm.commands.LoadVariable;
import de.lathanda.eos.vm.commands.StoreVariable;
import de.lathanda.eos.vm.commands.SubtractI;

import java.util.ArrayList;

/**
 * Speichert und behandelt eine Zählschleife.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class RepeatTimes extends Loop implements LoopTimesUnit {

	private final Expression timesE;
	private String varName;
	private int loopId;

	public RepeatTimes(Expression times, Sequence sequence) {
		super(sequence, null);
		this.timesE = times;
	}

	@Override
	public Expression getTimes() {
		return timesE;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ArrayList<Command> body = new ArrayList<>();
		sequence.compile(body, autoWindow);
		ops.add(new DebugPoint(timesE.getMarker()));
		ops.add(new CreateVariable(varName, Type.getInteger().getMType()));
		timesE.compile(ops, autoWindow);

		ops.add(new StoreVariable(varName));
		ops.add(new Jump(body.size() + 1));

		ops.addAll(body);

		ops.add(new DebugPoint(timesE.marker));
		ops.add(new LoadVariable(varName));
		ops.add(new LoadConstant(1));
		ops.add(new SubtractI());
		ops.add(new StoreVariable(varName));
		ops.add(new LoadVariable(varName));
		ops.add(new LoadConstant(0));
		ops.add(new GE());
		ops.add(new JumpIf(-(8 + body.size())));
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		sequence.resolveNamesAndTypes(with, env);
		if (timesE != null) {
			timesE.resolveNamesAndTypes(with, env);
			if (!timesE.getType().isNumber()) {
				env.addError(marker, "RepeatTimesNumber", timesE.getType());
			}
		}
		loopId = env.getUID();
		varName = ReservedVariables.REPEAT_TIMES_INDEX + loopId;
	}

	@Override
	public String toString() {
		return "repeat " + timesE + " times\n" + sequence + "endrepeat";
	}

	@Override
	public String getLabel() {
		return createText("RepeatTimes.Label", timesE.getLabel());
	}

	@Override
	public boolean isPre() {
		return true;
	}

	@Override
	public int getIndexId() {
		return loopId;
	}
}
