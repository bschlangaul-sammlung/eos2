package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Integer Division berechnen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class DivideI extends Command {

	@Override
	public boolean execute(Machine m) throws Exception {
		Object b = m.pop();
		Object a = m.pop();
		m.push(((Number) a).intValue() / ((Number) b).intValue());
		return true;
	}

	@Override
	public String toString() {
		return "Divide{" + '}';
	}
}
