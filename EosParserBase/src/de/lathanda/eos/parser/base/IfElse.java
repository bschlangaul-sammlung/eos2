package de.lathanda.eos.parser.base;


import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.parser.core.Node;
import de.lathanda.eos.parser.core.Sequence;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.DebugPoint;
import de.lathanda.eos.vm.commands.Jump;
import de.lathanda.eos.vm.commands.JumpIfNot;

import java.util.ArrayList;

/**
 * Speichert und behandelt eine Wenn-Dann-Sonst Struktur.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class IfElse extends Node implements AlternativeUnit {

	Expression condition;
	Sequence a;
	Sequence b;

	public IfElse(Expression condition, Sequence a, Sequence b) {
		this.condition = condition;
		this.a = a;
		this.b = b;
	}

	public Expression getCondition() {
		return condition;
	}

	@Override
	public Sequence getThen() {
		return a;
	}

	@Override
	public Sequence getElse() {
		return b;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ArrayList<Command> aop = new ArrayList<>();
		ArrayList<Command> bop = new ArrayList<>();
		ArrayList<Command> cond = new ArrayList<>();

		cond.add(new DebugPoint(condition.getMarker()));
		condition.compile(cond, autoWindow);

		a.compile(aop, autoWindow);
		if (b != null) {
			b.compile(bop, autoWindow);
		}

		ops.addAll(cond);
		ops.add(new JumpIfNot(aop.size() + 2));
		ops.addAll(aop);
		ops.add(new Jump(bop.size() + 1));
		ops.addAll(bop);

	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		a.resolveNamesAndTypes(with, env);
		if (b != null) {
			b.resolveNamesAndTypes(with, env);
		}
		condition.resolveNamesAndTypes(with, env);
		if (!condition.getType().isBoolean()) {
			env.addError(marker, "ConditionType", condition.getType());
		}
	}

	@Override
	public String toString() {
		return "if " + condition + " then\n" + a + "else\n" + b + "endif";
	}

	@Override
	public String getLabel() {
		return createText("IfElse.Label", condition.getLabel());
	}
}
