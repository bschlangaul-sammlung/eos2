package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MClass;
import de.lathanda.eos.vm.MObject;
import de.lathanda.eos.vm.Machine;;

/**
 * 
 * Assembler Befehl: Wert aus Attribut eines benutzerdefinierten Objektes laden.
 *
 * @author Peter (Lathanda) Schneider
 */
public class LoadProperty extends Command {
	private final String variable;

	public LoadProperty(MClass cls, String variable) {
		this.variable = variable;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		MObject obj = (MObject) m.pop();
		m.push(obj.getProperty(variable));
		return true;
	}

	@Override
	public String toString() {
		return "LoadProperty{" + variable + '}';
	}

}
