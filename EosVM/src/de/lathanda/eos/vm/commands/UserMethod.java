package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MProcedure;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Methode aufrufen
 *
 * Stacklayout [..., para1, ... , paran, target]
 * @author Peter (Lathanda) Schneider
 */
public class UserMethod extends Command {
	private final MProcedure method;

	public UserMethod(MProcedure method) {
		this.method = method;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		m.call(method);
		return true;
	}
}
