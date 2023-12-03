package de.lathanda.eos.interpreter.exceptions;

import de.lathanda.eos.common.gui.Messages;

/**
 * Das Attribut existiert bereits.
 *
 * @author Peter (Lathanda) Schneider
 */
public class WrongPlaceForDeclarationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2853444994907416099L;
	/**
	 * 
	 */

	private final String cls;
	private final String property;
    public WrongPlaceForDeclarationException(String property, String cls) {
        this.cls = cls;
        this.property = property;
    }
    @Override
    public String getLocalizedMessage() {
        return Messages.formatError("WrongPropertyDef", property, cls);
    }      
}
