package de.lathanda.eos.config;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import de.lathanda.eos.baseparser.AutoCompleteEntry;
import de.lathanda.eos.baseparser.BasicParser;
import de.lathanda.eos.baseparser.ParserSource;
import de.lathanda.eos.baseparser.SystemType;

public class LanguageConfig {
	private static TreeMap<String, ParserSource> parserSourceMap = new TreeMap<>();
	/**
	 * Attributnamen
	 */
	private final TreeMap<String, String> names = new TreeMap<>();
	private String id;
	private String label;
	private String tooltip;
	private String description;
	private LangFlags flags;
	private String parserSource;
	private TreeMap<String, LangModule> modules         = new TreeMap<>();
	private TreeMap<String, LangPredefinedVariablePool> variablePools = new TreeMap<>();
	private TreeMap<String, LangClass>  classes         = new TreeMap<>();
	private LinkedList<LangHelp>        helpEntries     = new LinkedList<>();
	private TreeMap<String, LangMenuItem> menuItems     = new TreeMap<>();
	TreeSet<AutoCompleteEntry> templates = new TreeSet<>();	
	public LanguageConfig(String id, LangFlags flags) {
		this.flags = flags;
		this.id = id;
	}

	public String getID() {
		return id;
	}
	public void apply(Language lang) {
		lang.apply(this);
		generateAutoCompletion();
		generateHelp(lang);		
	}

	private void generateHelp(Language lang) {
		lang.registerHelp(new HelpToolkit(id, label, tooltip, description, modules.values(), classes, helpEntries));		
	}

	private void generateAutoCompletion() {
		SystemType clsType = SystemType.getClassType();
		for(LangClass lc:classes.values()) {
			SystemType cls = SystemType.getInstanceByID(lc.id);
			if (lc.show) {
				for(String scan:lc.scan) {
					clsType.registerAutoCompleteEntry(AutoCompleteEntry.createClassEntry(cls, scan, lc.label, lc.tooltip));
				}
			}			
			for(LangMethod lm:lc.methods.values()) {
				String[] parameters = new String[lm.getParameters().size()];
				int i = 0;
				for (LangParameter lpa:lm.getParameters()) {
					parameters[i++] = lpa.name;
				}
				cls.registerAutoCompleteEntry(AutoCompleteEntry.createMethodEntry(cls, lm.scan, lm.label, parameters, lm.tooltip)); 
				
			}
			if (!flags.isLockProperties()) {
				for(LangProperty lp:lc.properties.values()) {
					cls.registerAutoCompleteEntry(AutoCompleteEntry.createPropertyEntry(cls, lp.scan, lp.label, lp.tooltip)); 				
				}
			}
		}				
	}

	public BasicParser createParser(String source) {
		ParserSource ps = parserSourceMap.get(parserSource);

		return ps.createParser(source);
	}
	public String toString() {
		return (label== null)?id:label;
	}

	public void setParser(String parser) {
		this.parserSource = parser;		
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setDescription(String description) {
		this.description = description;		
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;		
	}

	public void addTemplate(LangTemplate lt) {
		AutoCompleteEntry ace = AutoCompleteEntry.createTemplateEntry(lt.id,  lt.label, lt.template, lt.start, lt.length);
		templates.add(ace);		
	}
	public void registerName(String key, String name) {
		names.put(key,  name);
	}

	public void registerHelp(LangHelp lh) {
		helpEntries.add(lh);		
	}	
	public LangModule getOrCreateLangModule(String id) {
		if (modules.containsKey(id)) {
			return modules.get(id);
		} else {
			LangModule lm = new LangModule(id);
			modules.put(id,  lm);
			return lm;
		}
	}
	public LangClass getOrCreateLangClass(String id) {
		if (classes.containsKey(id)) {
			return classes.get(id);
		} else {
			LangClass lc = new LangClass(id);
			classes.put(id,  lc);
			return lc;
		}
	}
	public LangPredefinedVariablePool getOrCreatePredefinedVariablePool(String id) {
		if (variablePools.containsKey(id)) {
			return variablePools.get(id);
		} else {
			LangPredefinedVariablePool lpo = new LangPredefinedVariablePool();
			lpo.id = id;
			variablePools.put(id,  lpo);
			return lpo;
		}		
	}
	/**
	 * überträgt Technische Information 
	 * @param lco Quellconfig mit Technischen Daten.
	 * @return Merge, in der Regel die Zielconfig.
	 */
	public LanguageConfig mergeSystem(LanguageConfig lco) {
		for(LangModule lm : modules.values()) {
			LangModule lm2 = lco.getOrCreateLangModule(lm.id);
			lm.cls = lm2.cls;
			lm.javaclass = lm2.javaclass;
			for(LangFunction lf : lm.functions.values()) {
				LangFunction lf2 = lm2.getOrCreateFunction(lf.id);
				lf.ret = lf2.ret;
				lf.javamethod = lf2.javamethod;
				if (lf.parameters.size() != lf2.parameters.size()) {
					JOptionPane.showMessageDialog(null,
							"number of parameter missmatch " + lm.id+"."+lf.label+"("+lf.id+")"+"\nneeded "+lf2.parameters.size()+" found "+lf.parameters.size(),
							"fatal error", JOptionPane.OK_OPTION);
					System.exit(-1);	
				}
				Iterator<LangParameter> itp = lf2.parameters.iterator();
				for(LangParameter lpa:lf.parameters) {
					lpa.type = itp.next().type; 
				}
			}
		}
		for(LangClass lc2 : lco.classes.values()) {
			LangClass lc = classes.get(lc2.id);
			if (lc == null) {
				//technical class without text
				classes.put(lc2.id, lc2);
				continue;
			}
			lc.pid = lc2.pid;
			lc.javaclass = lc2.javaclass;
			for(LangMethod lm:lc.methods.values()) {
				LangMethod lm2 = lc2.getOrCreateMethod(lm.id);
				lm.ret = lm2.ret;
				lm.javamethod = lm2.javamethod;
				if (lm.getParameters().size() != lm2.getParameters().size()) {
					JOptionPane.showMessageDialog(null,
							"number of parameter missmatch " + lc.id+"."+lm.label+"("+lm.id+")"+"\nneeded "+lm2.getParameters().size()+" found "+lm.getParameters().size(),
							"fatal error", JOptionPane.OK_OPTION);
					System.exit(-1);	
				}
				Iterator<LangParameter> itp = lm2.getParameters().iterator();
				for(LangParameter lpa:lm.getParameters()) {
					lpa.type = itp.next().type; 
				}
				
			}
			for(LangProperty lp:lc.properties.values()) {
				LangProperty lp2 = lc2.getOrCreateProperty(lp.id);
				lp.type = lp2.type;
				lp.getter = lp2.getter;
				lp.setter = lp2.setter;
				lp.view   = lp2.view;
			}
		}
		for(LangPredefinedVariablePool lpo: variablePools.values()) {
			LangPredefinedVariablePool lpo2 = lco.getOrCreatePredefinedVariablePool(lpo.id);
			lpo.javaclass = lpo2.javaclass;
			for(LangPredefinedVariable lpv : lpo.variables.values()) {
				LangPredefinedVariable lpv2 = lpo2.variables.get(lpv.id);
				if (lpv2 == null) {
					JOptionPane.showMessageDialog(null,
							"missing predefined variable " + lpv.id,
							"fatal error", JOptionPane.OK_OPTION);
					System.exit(-1);						
				}
				lpv.javaname = lpv2.javaname;
				lpv.type     = lpv2.type;
			}
		}
		return this;
	}

	public void addMenuItem(String id, String pid, String label, String action, String tooltip) {
		LangMenuItem lmi = new LangMenuItem();
		lmi.id = id;
		lmi.pid = pid;
		lmi.label = label;
		lmi.action = action;
		lmi.tooltip = tooltip;		
		menuItems.put(id, lmi);		
	}
	public Collection<LangMenuItem> getMenuItems() {
		return menuItems.values();
	}
	public LangMenuItem getMenuItem(String id) {
		return menuItems.get(id);
	}
	public Collection<LangClass> getClasses() {
		return classes.values();
	}
	public Collection<LangModule> getModules() {
		return modules.values();
	}
	public Collection<LangPredefinedVariablePool> getPredefinedVariablePool() {
		return variablePools.values();
	}
	public LangClass getLangClass(String id) {
		return classes.get(id);
	}
}
