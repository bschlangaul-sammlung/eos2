package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.MethodType;
import de.lathanda.eos.baseparser.SystemType;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.ReservedVariables;
import de.lathanda.eos.vm.commands.LoadVariable;

import java.util.ArrayList;

/**
 * Speichert und verwaltet einen lesenden Variablenzugriff.
 *
 * @author Peter (Lathanda) Schneider
 */
public class PropertyRead extends Expression {

	private Expression target;
	private String member;
	private MethodType methodType;
	private boolean isVariable;

	public PropertyRead(Expression target, String member) {
		this.target = target;
		this.member = member.toLowerCase();
		prio = 7;
	}

	public String getMember() {
		return member;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		if (isVariable) {
			ops.add(new LoadVariable(member));
		} else if (target != null && methodType != null) {
			methodType.compile(ops, target, autoWindow);
		}
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		if (target != null) {
			// access member
			target.resolveNamesAndTypes(with, env);
			accessMember(env);
		} else {
			// try direct variable
			type = env.getVariableType(member);
			if (!type.isUnknown()) {
				// variable exists
				isVariable = true;
			} else if (with != null) {
				// try with
				target = with;
				target.resolveNamesAndTypes(null, env);
				// access member
				accessMember(env);
			} else if (member.equals(env.getDefaultWindowName())) {
				env.setFigureExists();
				if (env.getAutoWindow()) {
					member = ReservedVariables.WINDOW;
					isVariable = true;
					type = SystemType.getWindow();
				}
			} else {
				env.addError(marker, "UnknownVariable", member);
				type = Type.getUnknown();
			}
		}

	}

	private void accessMember(Environment env) {
		// access member
		type = target.getType();
		methodType = type.getReadProperty(member);
		isVariable = false;
		if (methodType == null) {
			env.addError(marker, "UnknownMember", type + "." + member);
			type = Type.getUnknown();
		} else {
			if (env.getLockProperties()) {
				env.addError(marker, "LockedMember", type + "." + member);
				type = Type.getUnknown();
			} else {
				type = methodType.getReturnType();
			}
		}
	}

	@Override
	public String toString() {
		if (target != null) {
			return target + "." + member;
		} else {
			return member;
		}
	}

	@Override
	public String getLabel() {
		if (target != null) {
			return createText("ReadA.Label", getLabelRight(target), member);
		} else {
			if (member.equals(ReservedVariables.RESULT)) {
				return createText("Result");
			} else {
				return createText("ReadB.Label", member);
			}
		}
	}

}
