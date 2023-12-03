package de.lathanda.eos.parser.base.exceptions;

import de.lathanda.eos.parser.base.text.Text;

/**
 * Die Methode existiert bereits
 *
 * @author Peter (Lathanda) Schneider
 */
public class DoubleMethodDeclarationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6836224685813330769L;
	private final String cls;
	private final String property;

	public DoubleMethodDeclarationException(String property, String cls) {
		this.cls = cls;
		this.property = property;
	}

	@Override
	public String getLocalizedMessage() {
		return Text.ERROR.formatMessage("DoubleMethodDef", property, cls);
	}
}
