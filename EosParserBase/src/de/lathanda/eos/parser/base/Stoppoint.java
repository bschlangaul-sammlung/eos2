package de.lathanda.eos.parser.base;

import java.util.ArrayList;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.Node;
import de.lathanda.eos.vm.Command;

/**
 * Beendet das Programm.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class Stoppoint extends Node {
	public Stoppoint() {
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ops.add(new de.lathanda.eos.vm.commands.Stoppoint(marker));
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		// not much to resolve it terminates the program
	}

	@Override
	public String getLabel() {
		return createText("Stop.Label");
	}
}
