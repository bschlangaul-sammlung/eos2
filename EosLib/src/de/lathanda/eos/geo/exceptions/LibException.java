package de.lathanda.eos.geo.exceptions;

import static de.lathanda.eos.geo.text.Text.ERROR;
public class LibException extends RuntimeException {
	private static final long serialVersionUID = 5681484621769034552L;
	private final String localMessage;

	public LibException(String errorID) {
		localMessage = ERROR.getString(errorID);
	}

	@Override
	public String getLocalizedMessage() {
		return localMessage;
	}
}
