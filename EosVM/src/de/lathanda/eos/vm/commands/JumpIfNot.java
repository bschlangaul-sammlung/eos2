package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Relativer Sprung bei falsch.
 *
 * @author Peter (Lathanda) Schneider
 */
public class JumpIfNot extends Command {
	int relativ;

	public JumpIfNot(int relativ) {
		this.relativ = relativ;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		Object obj = m.pop();
		if (!(Boolean) obj) {
			m.jump(relativ);
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "JumpIfNot{" + relativ + '}';
	}

}
