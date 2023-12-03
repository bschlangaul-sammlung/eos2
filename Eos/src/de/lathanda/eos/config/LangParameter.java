package de.lathanda.eos.config;

import java.text.MessageFormat;

public class LangParameter {
	public String name;
	public String tooltip;
	public String description;
	public String type;
	public String getFullSignature() {
		return MessageFormat.format(SIGNATURE,  name, Language.def.getLangClassByID(type).label);
	}
	private static final String SIGNATURE = "{0}:{1}";	
}
