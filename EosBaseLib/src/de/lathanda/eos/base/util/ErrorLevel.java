package de.lathanda.eos.base.util;

public enum ErrorLevel {
	STATUS(-1), INFORMATION(0), WARNING(1), ERROR(2), FATAL(3);

	public final int level;

	private ErrorLevel(int level) {
		this.level = level;
	}
}
