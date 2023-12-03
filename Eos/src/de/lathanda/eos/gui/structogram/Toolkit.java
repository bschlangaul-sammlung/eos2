package de.lathanda.eos.gui.structogram;

import de.lathanda.eos.baseparser.AlternativeUnit;
import de.lathanda.eos.baseparser.LoopUnit;
import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.vm.ProgramNode;

public class Toolkit {
	public static Unit create(ProgramNode n) {
		if (n instanceof AlternativeUnit) {
			return new Alternative((AlternativeUnit) n);
		} else if (n instanceof LoopUnit) {
			return new Loop((LoopUnit) n);
		} else {
			return new Statement(n);
		}
	}
}
