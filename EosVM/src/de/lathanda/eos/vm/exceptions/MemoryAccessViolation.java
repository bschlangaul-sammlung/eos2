package de.lathanda.eos.vm.exceptions;

import de.lathanda.eos.vm.text.Text;

/**
 * Es wurde versucht einen Speicher (Variable) auszulesen, der nie angelegt wurde.
 * Dies kann nur passieren bei einem Fehler im Compiler oder bei
 * Fehlern in der Konfiguration.
 *
 * @author Peter (Lathanda) Schneider
 */
public class MemoryAccessViolation extends RuntimeException {
	private static final long serialVersionUID = -2812099102982159089L;
	private final String variable;

	public MemoryAccessViolation(String variable) {
		super(variable);
		this.variable = variable;
	}

	@Override
	public String getLocalizedMessage() {
		return Text.ERROR.formatMessage("MemoryAccessViolation", variable);
	}
}
