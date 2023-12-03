package de.lathanda.eos.gui;

import de.lathanda.eos.baseparser.AbstractProgram;

/**
 * Callback um die Autovervollständigung auszulösen.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface AutoCompleteHook {

	void insertString(int pos, String text, AbstractProgram program);

}
