package de.lathanda.eos.config;

import java.util.LinkedList;
import java.util.TreeMap;

public class LangModule {
	public final String id;
	public LangModule(String id) {
		this.id = id;
	}
	public String label;
	public String tooltip;
	public String description;
	public String javaclass;
	public Class<?> cls;
	public TreeMap<String, LangFunction> functions = new TreeMap<>();
	public LinkedList<String> examples = new LinkedList<>();	
	public LangFunction getOrCreateFunction(String key) {
		if (functions.containsKey(key)) {
			return functions.get(key);
		} else {
			LangFunction lf = new LangFunction(this);
			functions.put(key, lf);
			return lf;
		}
	}
}
