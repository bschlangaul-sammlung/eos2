package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.baseparser.AlternativeUnit;
import de.lathanda.eos.baseparser.LoopForeverUnit;
import de.lathanda.eos.baseparser.LoopTimesUnit;
import de.lathanda.eos.baseparser.LoopUnit;
import de.lathanda.eos.vm.ProgramNode;

public class Toolkit {

	public static ConnectedUnit create(ProgramNode n) {
		if (n instanceof AlternativeUnit) {
			return new Alternative((AlternativeUnit) n);
		} else if (n instanceof LoopForeverUnit) {
			return new LoopForever((LoopForeverUnit) n);
		} else if (n instanceof LoopTimesUnit) {
			return new LoopTimes((LoopTimesUnit) n);
		} else if (n instanceof LoopUnit) {
			LoopUnit lu = (LoopUnit) n;
			if (lu.isPre()) {
				return new LoopWhile(lu);
			} else {
				return new LoopDoWhile(lu);
			}
		} else {
			return new Statement(n);
		}
	}

}
