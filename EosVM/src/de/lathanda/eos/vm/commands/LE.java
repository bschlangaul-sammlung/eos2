package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Kleiner gleich Vergleich.
 *
 * @author Peter (Lathanda) Schneider
 */
public class LE extends Command {

	@Override
	public boolean execute(Machine m) throws Exception {
		Number b = (Number) m.pop();
		Number a = (Number) m.pop();
		m.push(a.doubleValue() <= b.doubleValue());
		return true;
	}

	@Override
	public String toString() {
		return "LE{" + '}';
	}
}
