package de.lathanda.eos.parser.core;

import java.text.MessageFormat;

import de.lathanda.eos.parser.core.text.Text;
import de.lathanda.eos.vm.ErrorInformation;
import de.lathanda.eos.vm.Marker;

/**
 * Übersetzungsfehler.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class CompilerError implements ErrorInformation {
	private Marker code;
	private String message;

	public CompilerError(String errorId, Object... data) {
		this.code = null;
		this.message = MessageFormat.format(Text.ERROR.getMessage(errorId), data);
	}

	public CompilerError(Marker code, String message) {
		this.code = code;
		this.message = message;
	}

	public CompilerError(Marker code, Exception e) {
		this.code = code;
		this.message = MessageFormat.format(Text.ERROR.getMessage("Exception"), e.getLocalizedMessage());
	}

	public CompilerError(Marker code, String errorId, Object... data) {
		this.code = code;
		this.message = MessageFormat.format(Text.ERROR.getMessage(errorId), data);
	}

	@Override
	public Marker getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Error{" + "code=" + code + ", message=" + message + '}';
	}
}
