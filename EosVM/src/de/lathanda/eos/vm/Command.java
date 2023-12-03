package de.lathanda.eos.vm;

/**
 * Basisklasse aller Kommandos der Virtuellen Maschine.
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class Command {
	public abstract boolean execute(Machine m) throws Exception;

	public void prepare(Machine m) {
	}
}
