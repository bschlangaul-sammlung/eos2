package de.lathanda.eos.vm;

import java.util.Collection;
import java.util.TreeMap;

/**
 * Benutzerdefiniertes Objekt.
 * @author Peter (Lathanda) Schneider
 *
 */
public class MObject {
	private final TreeMap<String, Variable> properties;
	private final MClass cls;
	private final Object javaObject;

	public MObject(MClass mClass, Machine m) throws Exception {
		properties = mClass.createProperties(m);
		cls = mClass;
		javaObject = mClass.createJavaObject(m);
	}

	public MClass getType() {
		return cls;
	}

	public void setProperty(String s, Object value) {
		properties.get(s).set(value);
	}

	public Object getProperty(String s) {
		return properties.get(s);
	}

	public Object getJavaObject() {
		return javaObject;
	}
	public Collection<String> getProperties() {
		return properties.keySet();
	}
}
