package de.lathanda.eos.base.util;

public enum ErrorBehavior {
		/**
		 * Bei Fehler Programm anhalten.
		 */
		ABORT,
		/**
		 * Bei Fehlern eine Warnung ausgeben.
		 */
		WARN,
		/**
		 * Fehler, was ist ein Fehler?
		 */
		IGNORE,
		/**
		 * Fehler, Trace ausgeben aber ignorieren
		 */
		TRACE;

		/**
		 * Zahl in Verhalten umwandeln.
		 * @param value
		 * @return
		 */
		public static ErrorBehavior decode(int value) {
			switch (value) {
			case 0:
				return ABORT;
			case 1:
				return WARN;
			case 2:
				return IGNORE;
			case 3:
				return TRACE;
			}
			return WARN;
		}

		/**
		 * Verhalten in Zahl umwandeln.
		 * @return
		 */
		public int encode() {
			switch (this) {
			case ABORT:
				return 0;
			case WARN:
				return 1;
			case IGNORE:
				return 2;
			case TRACE:
				return 3;
			}
			return 1;
		}
	}