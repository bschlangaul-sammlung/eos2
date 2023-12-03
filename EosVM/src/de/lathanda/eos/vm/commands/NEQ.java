package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Auf Ungleichheit prüfen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class NEQ extends Command {

	public NEQ() {
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		Object b = m.pop();
		Object a = m.pop();
		m.push(!a.equals(b));
		return true;
	}

	@Override
	public String toString() {
		return "NEQ{" + '}';
	}

}
