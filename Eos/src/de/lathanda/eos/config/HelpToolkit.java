package de.lathanda.eos.config;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.baseparser.Program;
import de.lathanda.eos.baseparser.exceptions.TranslationException;
import de.lathanda.eos.gui.GuiConfiguration;
import de.lathanda.eos.gui.HtmlExport;
import de.lathanda.eos.parser.de.EosParserFactory;

import static de.lathanda.eos.gui.text.Text.*;

public class HelpToolkit {
	private LinkedList<HelpPage> roots = new LinkedList<>();
	public HelpToolkit(String id, String label, String tooltip, String description, Collection<LangModule> modules, TreeMap<String, LangClass> classes, Collection<LangHelp> helpEntries) {
		TreeMap<String, HelpPage> pages = new TreeMap<>();
		HelpPage page = new HelpPage(id, null, label, tooltip, MessageFormat.format(HELP_TEMPLATE.getString("language"),  label, description));	
		pages.put(page.getId(),  page);
		for(LangModule lm: modules) {
			HelpPage pagem = new HelpPage(lm.id, id, lm.label, lm.tooltip, moduleHtml(lm));
			pages.put(pagem.getId(),  pagem);
		}
		for (LangHelp lh: helpEntries) {
			HelpPage pagem = new HelpPage(lh.id, lh.pid, lh.title, lh.tooltip, helpHtml(lh));
			pages.put(pagem.getId(),  pagem);			
		}
		for (LangClass lc : classes.values()) {
			HelpPage pagem = new HelpPage(lc.id, id, lc.label, lc.tooltip, classHtml(lc));
			pages.put(pagem.getId(),  pagem);			
		}
		for(var hp:pages.values()) {
			if(hp.getPid() != null && pages.containsKey(hp.getPid())) {
				pages.get(hp.getPid()).addChild(hp);
			} else {
				roots.add(hp);
			}			
		}
		
	}
	public Collection<HelpPage> getHelpPages() {
		return roots;
	}
	private String classHtml(LangClass lc) {
		return MessageFormat.format(HELP_TEMPLATE.getString("class"),  
				lc.label, lc.description, 
				inheritanceHtml(lc), 
				propertiesHtml(lc.properties.values()), 
				methodsHtml(lc.methods.values()), 
				examplesHtml(lc.examples)
		);				
	}
	private Object propertiesHtml(Collection<LangProperty> lps) {
		if (lps.size() > 0) {
			StringBuilder html = new StringBuilder();
			for(var lp:lps) {
				html.append(propertyHtml(lp)+"\n");
			}
			return MessageFormat.format(HELP_TEMPLATE.getString("properties"), html.toString());
		} else {
			return "";
		}
	}
	private String propertyHtml(LangProperty lp) {
		return  MessageFormat.format(HELP_TEMPLATE.getString("property"),  lp.label, createType(lp.type), lp.description);
	}
	private String methodsHtml(Collection<LangMethod> lfs) {
		if (lfs.size() > 0) {
			StringBuilder html = new StringBuilder();
			for(var lf:lfs) {
				html.append(methodHtml(lf)+"\n");
			}
			return MessageFormat.format(HELP_TEMPLATE.getString("methods"), html.toString());
		} else {
			return "";
		}
	}
	private String inheritanceHtml(LangClass lc) {
		ArrayList<String> classes = new ArrayList<>();
		String id = lc.pid;
		while (id != null  && !id.isEmpty()) {
			classes.add(createType(id));
			id = Language.def.getLangClassByID(id).pid;
		}
		if (classes.size() == 0) return "";
		return MessageFormat.format(HELP_TEMPLATE.getString("inheritance"),  lc.label, createEnum(classes));		
	}
	private String createEnum(ArrayList<String> texts) {
		StringBuilder text = new StringBuilder();
		text.append(texts.get(0));
		for (int i = 1; i < texts.size() - 1; i++) {
			text.append(HELP_TEMPLATE.getString("enum1"));
			text.append(texts.get(i));
		}
		if (texts.size() > 1) {
			text.append(HELP_TEMPLATE.getString("enum2"));
			text.append(texts.get(texts.size()-1));
		}
		return text.toString();
	}
	private String helpHtml(LangHelp lh) {
		return MessageFormat.format(HELP_TEMPLATE.getString("help"),  lh.title, lh.description, examplesHtml(lh.examples));		
	}
	public String moduleHtml(LangModule lm) {
		return MessageFormat.format(HELP_TEMPLATE.getString("module"),  lm.label, lm.description, functionsHtml(lm.functions.values()),examplesHtml(lm.examples));		
	}
	public String functionsHtml(Collection<LangFunction> lfs) {
		StringBuilder html = new StringBuilder();
		for(var lf:lfs) {
			html.append(functionHtml(lf)+"\n");
		}
		return html.toString();		
	}
	public String examplesHtml(Collection<String> es) {
		
		StringBuilder html = new StringBuilder();
		for(String file:es) {
			try {
				String code =  ResourceLoader.getResourceAsString(file, "UTF-8");
				Program program = new Program(
					code, 
					new EosParserFactory(), 
					Language.def.getDefaultWindowName(), 
					false,
					Language.def.getPredefinedVariables()
				);
				program.parse(System.getProperty("user.dir"));
				html.append(MessageFormat.format(HELP_TEMPLATE.getString("example"), HtmlExport.export2html(program, "")));
			} catch (TranslationException e) {
				//error in example
				GuiConfiguration.def.log("error in example "+ file);
				GuiConfiguration.def.log(e.getErrorInformation().getMessage());
				//write log, no further task needed
			} catch (IOException e) {
				//missing example
				GuiConfiguration.def.log("missing example "+ file);
				//write log, no further task needed
			}
		}
		return html.toString();
		
	}
	public String functionHtml(LangFunction lf) {
		if (lf.ret == null || lf.ret.isEmpty()) {
			return  MessageFormat.format(HELP_TEMPLATE.getString("function"),  lf.label, parameterSignatureHtml(lf.parameters), lf.description, parameterHtml(lf.parameters));
		} else {
			return  MessageFormat.format(HELP_TEMPLATE.getString("functionRet"),  lf.label, parameterSignatureHtml(lf.parameters), createType(lf.ret), lf.description, parameterHtml(lf.parameters));
		}
	}
	private String methodHtml(LangMethod lf) {
		if (lf.ret == null || lf.ret.isEmpty()) {
			return  MessageFormat.format(HELP_TEMPLATE.getString("method"),  lf.label, parameterSignatureHtml(lf.getParameters()), lf.description, parameterHtml(lf.getParameters()));
		} else {
			return  MessageFormat.format(HELP_TEMPLATE.getString("methodRet"),  lf.label, parameterSignatureHtml(lf.getParameters()), createType(lf.ret), lf.description, parameterHtml(lf.getParameters()));
		}
	}	
	private String parameterHtml(LinkedList<LangParameter> lpas) {
		StringBuilder html = new StringBuilder();
		for(var lpa:lpas) {
			html.append(MessageFormat.format(HELP_TEMPLATE.getString("function"), lpa.name, createType(lpa.type), lpa.description));
		}
		return html.toString();
	}
	private String parameterSignatureHtml(LinkedList<LangParameter> lpas) {
		StringBuilder html = new StringBuilder();
		for(var lpa:lpas) {
			if (!html.isEmpty()) {
				html.append(HELP_TEMPLATE.getString("enum1"));
			}
			html.append(MessageFormat.format(HELP_TEMPLATE.getString("parameterSignature"), lpa.name, createType(lpa.type)));
		}
		return html.toString();		
	}
	private String createType(String id) {
		return MessageFormat.format(HELP_TEMPLATE.getString("type"), id, Language.def.getClassLabelForID(id));
	}
}
