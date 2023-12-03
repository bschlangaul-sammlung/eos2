package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Logisches Oder berechnen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Or extends Command {

	@Override
	public boolean execute(Machine m) throws Exception {
		Boolean b = (Boolean) m.pop();
		Boolean a = (Boolean) m.pop();
		m.push(a || b);
		return true;
	}

	@Override
	public String toString() {
		return "Or{" + '}';
	}
}
