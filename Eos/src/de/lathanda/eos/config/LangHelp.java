package de.lathanda.eos.config;

import java.util.LinkedList;

public class LangHelp {
	public final String id;	
	public LangHelp(String id) {
		this.id = id;
	}
	public String pid;
	public String title;
	public String tooltip;
	public String description;
	public LinkedList<String> examples = new LinkedList<String>();
}
