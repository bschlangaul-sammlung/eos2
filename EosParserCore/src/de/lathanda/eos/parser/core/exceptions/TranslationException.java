package de.lathanda.eos.parser.core.exceptions;

import de.lathanda.eos.vm.ErrorInformation;

public class TranslationException extends Exception {
	private static final long serialVersionUID = -5298737133568481826L;
	private ErrorInformation ei;

	public TranslationException(ErrorInformation ei) {
		this.ei = ei;
	}

	public ErrorInformation getErrorInformation() {
		return ei;
	}
}
