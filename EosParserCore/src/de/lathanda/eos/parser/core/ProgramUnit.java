package de.lathanda.eos.parser.core;

import de.lathanda.eos.vm.ProgramNode;
/**
 * Programmknoten.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface ProgramUnit extends ProgramNode {
	String getName();

	ProgramSequence getSequence();
}
