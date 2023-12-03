package de.lathanda.eos.vm.commands;

import java.lang.reflect.InvocationTargetException;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MObject;
import de.lathanda.eos.vm.MType;
import de.lathanda.eos.vm.Machine;
import de.lathanda.eos.vm.Variable;
import de.lathanda.eos.vm.exceptions.NullAccessException;

/**
 * 
 * Assembler Befehl: Methode aufrufen
 *
 * Stacklayout [..., para1, ... , paran, target]
 * @author Peter (Lathanda) Schneider
 */
public class Method extends Command {
	private final MType[] parameters;
	private final java.lang.reflect.Method method;

	public Method(MType[] parameters, java.lang.reflect.Method method) {
		this.parameters = parameters;
		this.method = method;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		Object target = m.pop();
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < args.length; i++) {
			args[i] = m.pop();
			args[i] = parameters[i].checkAndCast(args[i]);
		}
		if (target == null) {
			throw new NullAccessException();
		}
		try {
			Object result;
			if (target instanceof MObject) {
				result = method.invoke(((MObject) target).getJavaObject(), args);
			} else if (target instanceof Variable) {
				result = method.invoke(((Variable) target).get(), args);
			} else {
				result = method.invoke(target, args);
			}
			if (result != null) {
				m.push(result);
			}
		} catch (InvocationTargetException ite) {
			throw (Exception) ite.getTargetException();
		}
		return true;
	}

	@Override
	public String toString() {
		return "Method{" + method.getName() + "(" + parameters.length + ") }";
	}

}
