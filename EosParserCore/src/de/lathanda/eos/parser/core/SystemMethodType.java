package de.lathanda.eos.parser.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MType;

/**
 * Methodendefinition.
 * Speichert alle Daten die zum behandeln von Methoden notwendig sind.
 *
 * @author Peter (Lathanda) Schneider
 */
public class SystemMethodType implements MethodType {
	protected final Type[] parameters;
	protected final Type ret;
	protected final String label;
	protected final Method method;
	protected final String id;

	/**
	 * Erzeugt eine Java Methode
	 * @param target
	 * @param parameters
	 * @param ret
	 * @param name
	 * @param originalName
	 * @throws NoSuchMethodException
	 */
	public SystemMethodType(String id, SystemType target, SystemType[] parameters, Type ret, String javaname, String label)
			throws NoSuchMethodException {
		this.parameters = parameters;
		Class<?>[] parametersClass = new Class<?>[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parametersClass[i] = parameters[i].convertToClass();
		}
		this.method = target.convertToClass().getMethod(javaname, parametersClass);
		this.ret = ret;
		this.label = label;
		this.id = id;
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
		res.append(label).append("(");
		for (AutoCompleteType t : parameters) {
			if (first) {
				res.append(t.toString());
			} else {
				res.append(",").append(t.toString());
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

	@Override
	public String getName() {
		return label;
	}

	public void compile(ArrayList<Command> ops, Expression target, boolean autowindow) throws Exception {
		if (target != null) {
			target.compile(ops, autowindow);
		}
		ops.add(new de.lathanda.eos.vm.commands.Method(getParameters(), method));
	}

}
