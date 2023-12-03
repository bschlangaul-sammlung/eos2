package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.ProgramSequence;
import de.lathanda.eos.vm.ProgramNode;
/**
 * Schnittstelle markiert Syntaxknoten für Diagrammdarstellungen als Endlosschleife.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface LoopForeverUnit extends ProgramNode {
	ProgramSequence getSequence();
}
