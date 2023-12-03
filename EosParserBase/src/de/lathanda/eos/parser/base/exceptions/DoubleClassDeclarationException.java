package de.lathanda.eos.parser.base.exceptions;

import de.lathanda.eos.parser.base.text.Text;

/**
 * Die Klasse existiert bereits
 *
 * @author Peter (Lathanda) Schneider
 */
public class DoubleClassDeclarationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8821078166258701578L;
	private final String cls;

	public DoubleClassDeclarationException(String cls) {
		this.cls = cls;
	}

	@Override
	public String getLocalizedMessage() {
		return Text.ERROR.formatMessage("DoubleClassDef", cls);
	}
}
