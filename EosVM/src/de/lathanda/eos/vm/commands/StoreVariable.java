package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Wert in Variable speichern.
 *
 * @author Peter (Lathanda) Schneider
 */
public class StoreVariable extends Command {
	private final String variable;

	public StoreVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		m.set(variable, m.pop());
		return true;
	}

	@Override
	public String toString() {
		return "StoreVariable{" + variable + '}';
	}

}
