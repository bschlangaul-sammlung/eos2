package de.lathanda.eos.vm;

/**
 * 
 * Debuggingstelle.
 * Dieser Befehl hat zwei Wirkungen.
 * Er informiert jeden DebugListener (Quellcode Markierung, Objektdiagramm, ...) 
 * über das Erreichen der neuen Stelle im Quellcode.
 * Ist er aktiv unterbricht er zusätzlich die Ausführung.
 * 
 *
 * @author Peter (Lathanda) Schneider
 */
public class DebugPoint extends Command {
	private final Marker marker;
	private boolean activeBreakpoint;

	public DebugPoint() {
		this.marker = new Marker();
		activeBreakpoint = false;
	}

	public DebugPoint(Marker marker) {
		this.marker = marker;
		activeBreakpoint = false;
	}

	@Override
	public boolean execute(Machine m) throws Exception {
		m.debugStop(marker);
		if (activeBreakpoint) {
			m.pause();
		}
		return true;
	}

	public boolean isActiveBreakpoint() {
		return activeBreakpoint;
	}

	public void setActiveBreakpoint(boolean activeBreakpoint) {
		this.activeBreakpoint = activeBreakpoint;
	}

	public void toggleActiveBreakpoint() {
		activeBreakpoint = !activeBreakpoint;
	}

	@Override
	public String toString() {
		return "DebugPoint{" + marker + '}';
	}

	@Override
	public void prepare(Machine m) {
		m.addPossibleBreakpoint(this, marker.getBeginLine());
	}

	public Marker getMarker() {
		return marker;
	}
}
