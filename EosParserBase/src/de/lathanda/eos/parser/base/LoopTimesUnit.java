package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.ProgramSequence;
import de.lathanda.eos.vm.ProgramNode;
/**
 * Schnittstelle markiert Syntaxknoten für Diagrammdarstellungen als Zählschleife.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface LoopTimesUnit extends ProgramNode {
	@Override
	String getLabel();

	ProgramSequence getSequence();

	ProgramNode getTimes();

	int getIndexId();
}
