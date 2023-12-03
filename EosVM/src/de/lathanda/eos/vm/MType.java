package de.lathanda.eos.vm;

import java.util.TreeMap;

public interface MType {
	Object checkAndCast(Object obj);

	String getID();

	boolean isAbstract();

	Object newInstance(Machine m) throws Exception;

	Object createJavaObject(Machine m) throws Exception;

	TreeMap<String, Variable> createProperties(Machine m) throws Exception;

}
