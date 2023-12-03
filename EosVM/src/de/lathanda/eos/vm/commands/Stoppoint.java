package de.lathanda.eos.vm.commands;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Machine;
import de.lathanda.eos.vm.Marker;

/**
 * 
 * Programmende.
 * Dieser Befehl beendet das Programm.
 * 
 *
 * @author Peter (Lathanda) Schneider
 */
public class Stoppoint extends Command {
	private final Marker marker;

	public Stoppoint(Marker marker) {
		this.marker = marker;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		m.debugStop(marker);
		m.stop();
		return true;
	}

	@Override
	public String toString() {
		return "Stop{" + marker + '}';
	}

	public Marker getMarker() {
		return marker;
	}
}
