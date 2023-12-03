package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MType;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Variable anlegen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class CreateVariable extends Command {
	private final String name;
	private final MType type;

	public CreateVariable(String name, MType type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		m.create(name, type);
		return true;
	}

	@Override
	public String toString() {
		return "ReserveMemory{" + name + ":" + type + '}';
	}

}
