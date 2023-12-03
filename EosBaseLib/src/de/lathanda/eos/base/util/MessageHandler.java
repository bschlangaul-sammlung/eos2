package de.lathanda.eos.base.util;

import java.util.LinkedList;

/**
 * Diese Klasse empfängt Nachrichten und leitet sie gefiltert an alle 
 * Anzeigen weiter.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class MessageHandler {
	private ErrorBehavior eb = ErrorBehavior.WARN;

	public void clear() {
		logListener.forEach(log -> log.clearMessages());
	}

	public void sendStatus(String id) {
		fireMessage(id, ErrorLevel.STATUS);
	}

	public void sendInformation(String information) {
		fireMessage(information, ErrorLevel.INFORMATION);
	}

	public void sendWarning(String warning) {
		fireMessage(warning, ErrorLevel.WARNING);
	}

	public void sendError(String error) {
		fireMessage(error, ErrorLevel.ERROR);
	}

	public void sendFatal(String fatal) {
		fireMessage(fatal, ErrorLevel.FATAL);
	}
	public void setErrorBehavior(ErrorBehavior eb) {
		this.eb = eb;
	}
	
	public <T extends Exception> void handleOrThrowException(T e) throws T {
		switch (eb) {
		case WARN:
			sendWarning(e.getLocalizedMessage());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
			}
			break;
		case IGNORE:
			// well ignore it :)
			break;
		case TRACE:
			sendWarning(e.getLocalizedMessage());
			e.printStackTrace();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
			}
			break;
		case ABORT:
			throw e;
		}
	}

	public void handle(Exception e) {
		switch (eb) {
		case WARN:
			sendWarning(e.getLocalizedMessage());
			break;
		case IGNORE:
			// well ignore it :)
			break;
		case ABORT:
			sendError(e.getLocalizedMessage());
			break;
		case TRACE:
			sendWarning(e.getLocalizedMessage());
			e.printStackTrace();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
			}
		}
	}

	public void handleError(Exception e) {
		sendError(e.getLocalizedMessage());
	}

	private LinkedList<LogListener> logListener = new LinkedList<>();

	public synchronized void addLogListener(LogListener log) {
		logListener.add(log);
	}

	public synchronized void removeConfigurationListener(LogListener log) {
		logListener.remove(log);
	}

	public synchronized void fireMessage(String msg, ErrorLevel level) {
		logListener.forEach(log -> log.message(msg, level));
	}

	public interface LogListener {
		public void message(String msg, ErrorLevel level);

		public void clearMessages();
	}
}
