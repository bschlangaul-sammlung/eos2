package de.lathanda.eos.config;

import java.util.LinkedList;
import java.util.TreeMap;

import de.lathanda.eos.baseparser.SystemType;

public class LangClass {
	public final String id;
	public LangClass(String id) {
		this.id = id;
	}
	public String pid;
	public String[] scan;
	public String label;
	public String tooltip;
	public String description;
	public String javaclass;
	public SystemType type;
	public TreeMap<String, LangMethod> methods = new TreeMap<>();
	public TreeMap<String, LangProperty> properties = new TreeMap<>();
	public LinkedList<String> examples = new LinkedList<>();
	public boolean show;
	public LangProperty getOrCreateProperty(String key) {
		if (properties.containsKey(key)) {
			return properties.get(key);
		} else {
			LangProperty lp = new LangProperty();
			properties.put(key,  lp);
			return lp;
		}
	}
	public LangMethod getOrCreateMethod(String key) {
		if (methods.containsKey(key)) {
			return methods.get(key);
		} else {
			LangMethod lm = new LangMethod();
			methods.put(key,  lm);
			return lm;
		}
	}
}
