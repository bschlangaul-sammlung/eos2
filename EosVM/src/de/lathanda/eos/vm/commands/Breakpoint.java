package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;
import de.lathanda.eos.vm.Marker;

/**
 * 
 * Debuggingstelle.
 * Dieser Befehl hat zwei Wirkungen.
 * Er informiert jeden DebugListener (Quellcode Markierung, Objektdiagramm, ...) 
 * über das Erreichen der neuen Stelle im Quellcode.
 * 
 *
 * @author Peter (Lathanda) Schneider
 */
public class Breakpoint extends Command {
	private final Marker marker;

	public Breakpoint(Marker marker) {
		this.marker = marker;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		m.debugStop(marker);
		m.pause();
		return true;
	}

	@Override
	public String toString() {
		return "Breakpoint{" + marker + '}';
	}

	public Marker getMarker() {
		return marker;
	}
}
