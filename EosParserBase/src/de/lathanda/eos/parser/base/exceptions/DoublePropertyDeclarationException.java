package de.lathanda.eos.parser.base.exceptions;

import de.lathanda.eos.parser.base.text.Text;

/**
 * Das Attribut existiert bereits.
 *
 * @author Peter (Lathanda) Schneider
 */
public class DoublePropertyDeclarationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8475409666820140095L;
	private final String cls;
	private final String property;

	public DoublePropertyDeclarationException(String property, String cls) {
		this.cls = cls;
		this.property = property;
	}

	@Override
	public String getLocalizedMessage() {
		return Text.ERROR.formatMessage("DoublePropertyDef", property, cls);
	}
}
