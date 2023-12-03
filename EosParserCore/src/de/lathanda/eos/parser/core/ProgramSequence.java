package de.lathanda.eos.parser.core;

import java.util.ArrayList;

import de.lathanda.eos.vm.ProgramNode;
/**
 * Programmsequenz.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface ProgramSequence {
	public ArrayList<ProgramNode> getInstructions();
}
