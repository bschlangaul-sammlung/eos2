package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Zwei Zeichenketten verbinden.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Concatenate extends Command {

	@Override
	public boolean execute(Machine m) throws Exception {
		Object b = m.pop();
		Object a = m.pop();
		m.push(a.toString() + b.toString());
		return true;
	}

	@Override
	public String toString() {
		return "Concatenate{" + '}';
	}
}
