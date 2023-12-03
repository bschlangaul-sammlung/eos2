package de.lathanda.eos.parser.base;

import de.lathanda.eos.parser.core.ProgramSequence;
import de.lathanda.eos.vm.ProgramNode;

/**
 * Programmknoten der verzweigt.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface AlternativeUnit extends ProgramNode {

	@Override
	String getLabel();

	ProgramSequence getThen();

	ProgramSequence getElse();

}
