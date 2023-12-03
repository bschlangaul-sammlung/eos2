package de.lathanda.eos.config;

import java.text.MessageFormat;

public class LangProperty {
	public LangProperty() {}
	public String id;
	public String scan;
	public String label;
	public String tooltip;
	public String description;
	public String type;
	public String getter;
	public String setter;
	public String view;
	public boolean uses(String id) {
		return type.equals(id);
	}
	public String getFullSignature() {
		return MessageFormat.format(SIGNATURE,  label, Language.def.getLangClassByID(type).label);
	}
	private static final String SIGNATURE = "{0}:{1}";
}
