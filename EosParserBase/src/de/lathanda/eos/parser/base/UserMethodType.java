package de.lathanda.eos.parser.base;

import java.util.ArrayList;

import de.lathanda.eos.parser.core.MethodType;
import de.lathanda.eos.parser.core.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MProcedure;
import de.lathanda.eos.vm.MType;
import de.lathanda.eos.vm.commands.UserMethod;

/**
 * Methodendefinition.
 * Speichert alle Daten die zum behandeln von Methoden notwendig sind.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class UserMethodType implements MethodType {
	protected final Type[] parameters;
	protected final Type ret;
	protected final String name;
	protected MProcedure method;

	/**
	 * Erzeugt eine Userdefinierte Methode
	 * @param name
	 * @param parameters
	 * @param ret
	 */
	public UserMethodType(String name, Type[] parameters, Type ret) {
		this.parameters = parameters;
		this.ret = ret;
		this.name = name;
	}

	public boolean checkType(Type[] args) {
		if (parameters.length != args.length) {
			return false;
		}
		for (int i = 0; i < args.length; i++) {
			if (!parameters[i].checkType(args[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Type getReturnType() {
		return ret;
	}

	@Override
	public Type getParameterType(int i) {
		return parameters[i];
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		boolean first = true;
		res.append(name).append("(");
		for (AutoCompleteType t : parameters) {
			if (first) {
				res.append(t);
			} else {
				res.append(",").append(t);
				first = false;
			}
		}
		res.append(")");
		if (ret != null && !ret.isVoid()) {
			res.append(":").append(ret);
		}
		return res.toString();
	}

	@Override
	public MType[] getParameters() {
		MType[] para = new MType[parameters.length];
		for (int i = 0; i < para.length; i++) {
			para[i] = parameters[i].getMType();
		}
		return para;
	}

	public void compile(ArrayList<Command> ops, Expression target, boolean autowindow) throws Exception {
		target.compile(ops, autowindow);
		ops.add(new UserMethod(method));
	}

	@Override
	public String getName() {
		return name;
	}

	public void createMProcedure(ArrayList<Command> ops) {
		method = new MProcedure(ops, false);
	}
}
