package de.lathanda.eos.vm.exceptions;

import de.lathanda.eos.vm.text.Text;

/**
 * Der übergebene Typ ist mit dem erwarteten Typ nicht kompatibel.
 * Dieser Fehler kann eigentlich nur auftreten, wenn der Compiler bei 
 * der Typprüfung einen Fehler macht oder die Konfiguration falsch ist.
 *
 * @author Peter (Lathanda) Schneider
 */
public class TypeMissMatchException extends RuntimeException {
	private static final long serialVersionUID = -1331782960608862330L;
	private final String expected;
	private final String found;

	public TypeMissMatchException(String expected, String found) {
		this.expected = expected;
		this.found = found;
	}

	@Override
	public String getLocalizedMessage() {
		return Text.ERROR.formatMessage("InvalidTypeCast", found, expected);
	}
}
