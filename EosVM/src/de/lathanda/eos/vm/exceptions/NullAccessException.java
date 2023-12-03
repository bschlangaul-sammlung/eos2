package de.lathanda.eos.vm.exceptions;

import de.lathanda.eos.vm.text.Text;

/**
 * Dieser Fehler wird ausgelöst, wenn auf eine Variable zugegriffen wird,
 * bevor sie einen Wert hat.
 * Dies ist nur bei abstrakten Typen möglich.
 *
 * @author Peter (Lathanda) Schneider
 */
public class NullAccessException extends RuntimeException {
	private static final long serialVersionUID = -5428419995880814279L;

	public NullAccessException() {
		super();
	}

	@Override
	public String getLocalizedMessage() {
		return Text.ERROR.getMessage("AccessNull");
	}
}
