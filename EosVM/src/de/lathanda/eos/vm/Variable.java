package de.lathanda.eos.vm;

/**
 * Variable
 * 
 * Behälter zum speichern einer Variablen in der Maschine.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Variable {

	public final MType type;
	private Object data;
	public final String name;

	protected Variable(MType type, String name) {
		this.type = type;
		this.name = name;
	}

	public Object get() {
		return data;
	}

	public void set(Object data) {
		if (data != null) {
			this.data = type.checkAndCast(data);
		} else {
			this.data = null;
		}
	}
}
