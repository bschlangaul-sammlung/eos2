package de.lathanda.eos.config;

import java.text.MessageFormat;
import java.util.LinkedList;

public class LangMethod {
	public LangMethod() {}
	public String id;
	public String scan;
	public String label;
	private LinkedList<LangParameter> parameters = new LinkedList<>();
	public String javamethod;
	public String tooltip;
	public String ret;
	public String description;
	public boolean uses(String id) {
		if (ret.equals(id)) {
			return true;
		}
		for(LangParameter lpa: parameters) {
			if (lpa.type.equals(id)) {
				return true;
			}
		}
		return false;
	}
	public String getFullSignature() {
		StringBuilder para = new StringBuilder();
		for (LangParameter lpa: parameters) {
			if (!para.isEmpty()) {
				para.append(", ");
			}
			para.append(lpa.getFullSignature());
		}
		return MessageFormat.format(SIGNATURE,  label, para, Language.def.getLangClassByID(ret).label);
	}
	public void addParameter(LangParameter lpa) {
		parameters.add(lpa);
	}
	public LinkedList<LangParameter> getParameters() {
		return parameters;
	}
	private static final String SIGNATURE = "{0}({1}):{2}";
}
