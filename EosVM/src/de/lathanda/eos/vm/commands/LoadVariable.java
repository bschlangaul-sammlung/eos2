package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Wert aus Variable laden.
 *
 * @author Peter (Lathanda) Schneider
 */
public class LoadVariable extends Command {
	private String variable;

	public LoadVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		m.push(m.get(variable));
		return true;
	}

	@Override
	public String toString() {
		return "LoadVariable{" + variable + '}';
	}

}
