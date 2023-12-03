package de.lathanda.eos.parser.base.exceptions;

import de.lathanda.eos.parser.base.text.Text;

/**
 * Es wurde versucht eine Klasse zu definieren die sich selbst enthält.
 *
 * @author Peter (Lathanda) Schneider

 */
public class CyclicStorageException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5295897311459322613L;
	private final String cls;

	public CyclicStorageException(String cls) {
		super(cls);
		this.cls = cls;
	}

	@Override
	public String getLocalizedMessage() {
		return Text.ERROR.formatMessage("CyclicStorage", cls);
	}
}
