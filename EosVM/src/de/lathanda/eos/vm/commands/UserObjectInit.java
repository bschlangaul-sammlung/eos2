package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MClass;
import de.lathanda.eos.vm.MObject;
import de.lathanda.eos.vm.Machine;

/**
 * 
 * Assembler Befehl: User Objekt erzeugen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserObjectInit extends Command {
	private final MClass cls;

	public UserObjectInit(MClass cls) {
		this.cls = cls;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		MObject obj = new MObject(cls, m);
		m.push(obj);
		return true;
	}

	@Override
	public String toString() {
		return "Create MObject{" + cls + '}';
	}

}
