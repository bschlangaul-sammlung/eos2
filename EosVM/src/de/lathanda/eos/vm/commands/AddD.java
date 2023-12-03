package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Double Addition berechnen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class AddD extends Command {

	@Override
	public boolean execute(Machine m) throws Exception {
		Object b = m.pop();
		Object a = m.pop();
		m.push(((Number) a).doubleValue() + ((Number) b).doubleValue());
		return true;
	}

	@Override
	public String toString() {
		return "Add{" + '}';
	}
}
