package de.lathanda.eos.vm.commands;

import java.lang.reflect.Method;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MType;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Globale Funktion aufrufen.
 * Eine Funktion der Klasse SystemFunctions wird aufgerufen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Function extends Command {
	private final MType[] parameters;
	private final Method method;

	public Function(MType[] parameters, Method method) {
		this.parameters = parameters;
		this.method = method;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < args.length; i++) {
			args[i] = m.pop();
			args[i] = parameters[i].checkAndCast(args[i]);
		}
		m.push(method.invoke(null, args));
		return true;
	}

	@Override
	public String toString() {
		return "Function{" + method.getName() + "(" + parameters.length + ") }";
	}

}
