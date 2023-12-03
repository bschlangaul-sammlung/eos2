package de.lathanda.eos.config;

import java.util.TreeMap;

public class LangPredefinedVariablePool {
	String id;
	String javaclass;
	Class<?> cls;
	TreeMap<String, LangPredefinedVariable> variables = new TreeMap<>();
	public LangPredefinedVariable getOrCreatePredefinedVariable(String id) {
		if (variables.containsKey(id)) {
			return variables.get(id);
		} else {
			LangPredefinedVariable lpv = new LangPredefinedVariable();
			lpv.id = id;
			variables.put(id, lpv);
			return lpv;
		}
	}
}
