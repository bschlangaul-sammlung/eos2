package de.lathanda.eos.config;

import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import de.lathanda.eos.base.util.GuiToolkit;
import de.lathanda.eos.baseparser.AutoCompleteEntry;
import de.lathanda.eos.baseparser.PredefinedVariable;
import de.lathanda.eos.baseparser.SystemFunctionType;
import de.lathanda.eos.baseparser.SystemMethodType;
import de.lathanda.eos.baseparser.SystemType;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.baseparser.UserType;
import de.lathanda.eos.vm.MissingTypeException;
import de.lathanda.eos.vm.ReservedVariables;


public class Language {
	public static final Language def;
	static {
		def = new Language();
		def.init();
	}
	private TreeMap<String, LanguageConfig> langs = new TreeMap<>();
	private LanguageConfig effectiv;
	/**
	 * Templates
	 */
	private final TreeSet<AutoCompleteEntry> templates = new TreeSet<>();
	/**
	 * Labels
	 */
	private final TreeMap<String, String> labels = new TreeMap<>();
	/**
	 * Predefined Variables
	 */
	private final TreeMap<String, PredefinedVariable> predefinedVariables = new TreeMap<>(); 
	/**
	 * Help
	 */
	private HelpToolkit helpEntries;

	private LangFlags flags = new LangFlags();
	private Language() {}
	private void init() {
		flags = new LangFlags();
		effectiv = new LanguageConfig("effectiv", flags);
		LangXMLScanner lxs = new LangXMLScanner(this);
		lxs.load();
	}

	public Collection<LanguageConfig> getAvailableLanguageConfigs() {
		return langs.values();
	}
	public boolean isLockProperties() {
		return flags.isLockProperties();
	}
	public void setLockProperties(boolean lockProperties) {
		this.flags.setLockProperties(lockProperties);
	}

	public void registerLabels(ResourceBundle res) {
		for (String key : res.keySet()) {
			labels.put(key, res.getString(key));
		}
	}

	public boolean containsLabel(String id) {
		return labels.containsKey(id);
	}

	public String getLabel(String id) {
		return labels.getOrDefault(id, id);
	}	
	public String getClassLabelForID(String id) {
		LangClass lc = effectiv.getLangClass(id);
		if (lc != null) {
			return lc.label;
		} else {
			return id;
		}
	}
	public String getClassLabel(Type t) {
		if (t instanceof SystemType) {
			return getLangClassByID(t.getID()).label; 
		} else if (t instanceof UserType){
			return ((UserType)t).getLabel();
		} else {
			return t.getID();
		}
	}
	public LanguageConfig getOrCreateLanguageConfig(String extid) {
		LanguageConfig lc;
		if (langs.containsKey(extid)) {
			lc = langs.get(extid);
		} else {
			lc = new LanguageConfig(extid, flags);
			langs.put(lc.getID(), lc);
		}

		return lc;
	}
	public void addPluginMenues(JMenuBar mainMenu) {
		TreeMap<String, JMenuItem> menues = new TreeMap<>();
		for(LangMenuItem lmi : effectiv.getMenuItems()) {
			ActionListener al;
			try {
				al = (lmi.action == null || lmi.action.isEmpty())?null:(ActionListener)Class.forName(lmi.action).getDeclaredConstructor().newInstance();
				if (lmi.pid != null && !lmi.pid.isEmpty()) {
					JMenuItem mi = GuiToolkit.createMenuItem(lmi.label, lmi.tooltip, al);
					menues.put(lmi.id, mi);
				} else {
					JMenu mi = GuiToolkit.createMenue(lmi.label);
					mainMenu.add(mi);
					menues.put(lmi.id, mi);					
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"class not found " + lmi.action,
						"fatal error", JOptionPane.OK_OPTION);
				System.exit(-1);
			}
		}
		for(LangMenuItem lmi : effectiv.getMenuItems()) {
			if (lmi.pid != null && !lmi.pid.isEmpty()) {
				menues.get(lmi.pid).add(menues.get(lmi.id));
			}
		}
	}
	public void registerClass(String id, String pid, String javaclass) {
		LangClass lc = effectiv.getOrCreateLangClass(id);
		lc.pid = pid;
		lc.javaclass = javaclass;	
	}
	public void registerProperty(String cls_id, String property_id, String type, String getter, String setter, String view) {
		LangClass lc = effectiv.getOrCreateLangClass(cls_id);
		LangProperty lp = lc.getOrCreateProperty(property_id);
		lp.id = property_id;
		lp.type = type;
		lp.getter = getter;
		lp.setter = setter;
		lp.view = view;
	}
	public void registerMethod(String cls_id, String method_id, String javamethod, LinkedList<String> parametertypes, String return_type) {
		LangClass lc = effectiv.getOrCreateLangClass(cls_id);
		LangMethod lm = lc.getOrCreateMethod(method_id);
		lm.id = method_id;
		lm.javamethod = javamethod;
		for(String paratype : parametertypes) {
			LangParameter lpa = new LangParameter();
			lpa.type = paratype;
			lm.addParameter(lpa);
		}		
		lm.ret = return_type;
	}
	public void registerModule(String id, String javaclass) {
		LangModule lm = effectiv.getOrCreateLangModule(id);
		lm.javaclass = javaclass;
	}
	public void registerGlobalFunction(String module_id, String function_id, String javamethod, LinkedList<String> parametertypes, String return_type) {
		LangModule lm = effectiv.getOrCreateLangModule(module_id);
		LangFunction lf = lm.getOrCreateFunction(function_id);
		lf.ret = return_type;
		lf.javamethod = javamethod;
		for(String paratype : parametertypes) {
			LangParameter lpa = new LangParameter();
			lpa.type = paratype;
			lf.parameters.add(lpa);
		}
	}
	public Collection<HelpPage> getHelpData() {
		return helpEntries.getHelpPages();
	}
	public Set<AutoCompleteEntry> getTemplates() {
		return templates;
	}

	public void registerHelp(HelpToolkit help) {
		helpEntries = help;		
	}

	public void apply(LanguageConfig lc) {
		effectiv = lc.mergeSystem(effectiv);
		try {
			initSystem();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
				e.getLocalizedMessage() ,
				"fatal error", JOptionPane.OK_OPTION);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public LangClass getLangClassByID(String id) {
		return effectiv.getOrCreateLangClass(id);
	}

	private void initSystem() throws NoSuchMethodException, MissingTypeException, ClassNotFoundException {
		ObjectFactory objf = new ObjectFactory();
		//create types
		for(LangClass lc : effectiv.getClasses()) {
			try {
				lc.type = SystemType.registerType(lc.id, lc.scan, objf.getObjectSource(lc), lc.javaclass);
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null,
						"class not found " + lc.javaclass,
						"fatal error", JOptionPane.OK_OPTION);
				e.printStackTrace();
				System.exit(-1);
			}
		}		
		//link inheritance
		for(LangClass lc : effectiv.getClasses()) {
			if (lc.pid != null && !lc.pid.isEmpty()) {
				SystemType.registerSuper(lc.id, lc.pid);
			}
		}
		//create getters and setters for properties
		for(LangClass lc:effectiv.getClasses()) {
			for(LangProperty lp:lc.properties.values()) {
				if (lp.getter != null && !lp.getter.isEmpty()) {
					//getter
					lc.type.registerReadProperty(
						new SystemMethodType(lp.id, lc.type, new SystemType[] {}, SystemType.getInstanceByID(lp.type), lp.getter, lp.label),
						lp.label
					);
					//setter
					lc.type.registerAssignProperty(
						new SystemMethodType(lp.id, lc.type, new SystemType[] {SystemType.getInstanceByID(lp.type)}, SystemType.getVoid(), lp.setter, lp.label),
						lp.label
					);					
				}
				if (lp.view == null) {
					JOptionPane.showMessageDialog(null,
							"property not found or broken " + lc.id+"."+lp.label,
							"fatal error", JOptionPane.OK_OPTION);
					System.exit(-1);					
				}
				//reader (read access to property for object cards or other diagrams, this can include invisible properties)
				lc.type.registerViewProperty(
					lp.view,
					lp.label
				);
			}			
		}
		//create methods
		for(LangClass lc:effectiv.getClasses()) {
			for(LangMethod lm:lc.methods.values()) {
				SystemType[] parameters = new SystemType[lm.getParameters().size()];
				int i = 0;
				for(LangParameter lpa:lm.getParameters()) {
					parameters[i++] = SystemType.getInstanceByID(lpa.type);
					
				}
				if (lm.javamethod == null) {
					JOptionPane.showMessageDialog(null,
							"method not found or broken " + lc.id+"."+lm.label,
							"fatal error", JOptionPane.OK_OPTION);
					System.exit(-1);					
				}	
				lc.type.registerMethod(new SystemMethodType(lm.id, lc.type, parameters,	SystemType.getInstanceByID(lm.ret), lm.javamethod, lm.label));				
			}
		}		
		//create functions
		for(LangModule lm:effectiv.getModules()) {		
			lm.cls = Class.forName(lm.javaclass);
			for(LangFunction lf:lm.functions.values()) {
				SystemType[] parameters = new SystemType[lf.parameters.size()];
				int i = 0;
				for(LangParameter lpa:lf.parameters) {
					parameters[i++] = SystemType.getInstanceByID(lpa.type);
					
				}
				SystemFunctionType.registerSystemFunction(lm.cls, parameters, SystemType.getInstanceByID(lf.ret), lf.javamethod, lf.label);				
			}
		}	
		//create predefined variables
		for(LangPredefinedVariablePool lpvo:effectiv.getPredefinedVariablePool()) {
			Class<?> cls = Class.forName(lpvo.javaclass);
			for(LangPredefinedVariable lpv: lpvo.variables.values()) {
				try {
					PredefinedVariable pv = new PredefinedVariable(lpv.scan, SystemType.getInstanceByID(lpv.type), cls.getDeclaredField(lpv.javaname).get(null));
					predefinedVariables.put(pv.getScan(), pv);
				} catch (NoSuchFieldException | SecurityException |IllegalAccessException e) {
					JOptionPane.showMessageDialog(null,
							"predefined variable value not found " + lpvo.id+":"+lpv.scan+"("+lpv.id+")",
							"fatal error", JOptionPane.OK_OPTION);
					System.exit(-1);
				}
			}
		}
	}
	public String getDefaultWindowName() {
		return getLabel(ReservedVariables.WINDOW);
	}
	public void registerVariablePool(String id, String cls) {
		var pool = effectiv.getOrCreatePredefinedVariablePool(id);
		pool.javaclass = cls;
		
	}
	public void registerPredefinedVariable(String poolId, String id, String name, String type) {
		var pool = effectiv.getOrCreatePredefinedVariablePool(poolId);
		LangPredefinedVariable lpv = pool.getOrCreatePredefinedVariable(id);
		lpv.id = id;
		lpv.javaname = name;
		lpv.type = type;
	}
	public Collection<PredefinedVariable> getPredefinedVariables() {
		return predefinedVariables.values();
	}
}
