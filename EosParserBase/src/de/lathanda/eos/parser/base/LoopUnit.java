package de.lathanda.eos.parser.base;

import de.lathanda.eos.parser.core.ProgramSequence;
import de.lathanda.eos.vm.ProgramNode;
/**
 * Schnittstelle markiert Syntaxknoten für Diagrammdarstellungen als Schleife.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface LoopUnit extends ProgramNode {
	boolean isPre();

	@Override
	String getLabel();

	ProgramSequence getSequence();
}
