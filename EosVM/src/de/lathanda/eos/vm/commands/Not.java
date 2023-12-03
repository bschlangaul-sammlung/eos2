package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Logisches Nicht berechnen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Not extends Command {

	public Not() {
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		Boolean a = (Boolean) m.pop();
		m.push(!a);
		return true;
	}

	@Override
	public String toString() {
		return "Not{" + '}';
	}

}
