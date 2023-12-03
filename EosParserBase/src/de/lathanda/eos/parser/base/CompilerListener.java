package de.lathanda.eos.parser.base;

import java.util.LinkedList;

import de.lathanda.eos.parser.core.AbstractProgram;
import de.lathanda.eos.vm.ErrorInformation;

/**
 * Kommunikationsschnittstelle vom Kompiler zur Anzeige.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public interface CompilerListener {
	void compileComplete(LinkedList<ErrorInformation> errors, AbstractProgram program);
}
