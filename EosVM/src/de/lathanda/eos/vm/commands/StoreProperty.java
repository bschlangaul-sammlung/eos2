package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MClass;
import de.lathanda.eos.vm.MObject;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: Wert in Attribut eines benutzerdefinierten Objektes speichern.
 *
 * @author Peter (Lathanda) Schneider
 */
public class StoreProperty extends Command {
	private final String variable;

	public StoreProperty(MClass cls, String variable) {
		this.variable = variable;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		MObject obj = (MObject) m.pop();
		Object data = m.pop();
		obj.setProperty(variable, data);
		return true;
	}

	@Override
	public String toString() {
		return "StoreProperty{" + variable + '}';
	}

}
