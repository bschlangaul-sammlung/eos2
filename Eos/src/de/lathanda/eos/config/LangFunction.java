package de.lathanda.eos.config;

import java.util.LinkedList;

public class LangFunction {
	public final LangModule module;
	public LangFunction(LangModule module) {
		this.module = module;
	}
	public String id;	
	public String scan;
	public String label;
	public String tooltip;
	public String description;
	public String ret;
	public String javamethod;
	public LinkedList<LangParameter> parameters = new LinkedList<>();
}
