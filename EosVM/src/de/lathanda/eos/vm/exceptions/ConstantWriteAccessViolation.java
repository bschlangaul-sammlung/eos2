package de.lathanda.eos.vm.exceptions;

import de.lathanda.eos.vm.text.Text;

/**
 * Es wurde versucht einen Speicher (Variable) auszulesen, der nie angelegt wurde.
 * Dies kann nur passieren bei einem Fehler im Compiler oder bei
 * Fehlern in der Konfiguration.
 *
 * @author Peter (Lathanda) Schneider
 */
public class ConstantWriteAccessViolation extends RuntimeException {
	private static final long serialVersionUID = -520910152858350882L;
	private final String variable;

	public ConstantWriteAccessViolation(String variable) {
		super(variable);
		this.variable = variable;
	}

	@Override
	public String getLocalizedMessage() {
		return Text.ERROR.formatMessage("ConstantWriteAccessViolation", variable);
	}
}
