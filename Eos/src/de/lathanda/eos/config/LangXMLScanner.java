package de.lathanda.eos.config;

import java.util.LinkedList;
import java.util.Locale;
import java.util.ServiceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.lathanda.eos.base.extension.XMLProvider;

public class LangXMLScanner {
	private Language lang;
	public LangXMLScanner(Language lang) {
		this.lang = lang;
	}
	public void load() {
		ServiceLoader<XMLProvider> sp = ServiceLoader.load(XMLProvider.class);
		LinkedList<Document> docs = new LinkedList<>();
		for(var xml : sp) {
			for(Document d : xml.getXML()) {
				docs.add(d);
			}
		}
		while (!docs.isEmpty()) {
			Document d = docs.pop();
			try {
				scan(d);
			} catch (UnknownNodeException e) {
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}	
	public void scan(Document d) throws UnknownNodeException {
		NodeList roots = d.getChildNodes();
		for(int i = 0; i < roots.getLength(); i++) {
			Node n = roots.item(i);
			if (n instanceof Element) {
				Element e = (Element)n;				
				switch(e.getTagName()) {
				case "types":
					scanTypes(e);
					break;
				case "lang":
					scanLang(e);
					break;
				case "extension":
					scanExtension(e);
					break;
				default:
					throw new UnknownNodeException(n);	
				}
			}
		}		
	}
	private void scanExtension(Element e) throws UnknownNodeException {
		String extid = e.getAttribute("extends");	
		LanguageConfig lc = lang.getOrCreateLanguageConfig(extid);
		scanLangBody(e, lc, false);				
	}
	private void scanLang(Element e) throws UnknownNodeException {
		String id = e.getAttribute("id");
		LanguageConfig lc = lang.getOrCreateLanguageConfig(id);
		scanLangBody(e, lc, true);		
	}
	private void scanLangBody(Element n, LanguageConfig lc, boolean setParser) throws UnknownNodeException {
		Locale locale = Locale.getDefault();		
		var body = n.getChildNodes();
		Element text = null;
		for(int i = 0; i < body.getLength(); i++) {
			Node node = body.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element)node;				
				switch (e.getTagName()) {
				case "text":
					if (text == null) {
						text = e;
					}
					String country = e.getAttribute("lang");
					if (locale.getLanguage().equals(new Locale(country).getLanguage())) {
						text = e;
					}
					break;				
				default:
					throw new UnknownNodeException(node);
				}
			}
		}	
		if (text != null) {
			if (setParser) {
				String parser = text.getAttribute("parser");
				lc.setParser(parser);
			}
			scanText(text, lc);
		}
	}
	private void scanText(Element text, LanguageConfig lc) throws UnknownNodeException {
		var body = text.getChildNodes();
		for(int i = 0; i < body.getLength(); i++) {
			Node node = body.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element)node;	 
				switch (e.getTagName()) {
				case "label":
					lc.setLabel(e.getTextContent().trim());
					break;	
				case "tooltip":
					lc.setTooltip(e.getTextContent().trim());
					break;
				case "description":
					lc.setDescription(readHtmlContent(e));
					break;
				case "global":
					scanGlobalT(e, lc);
					break;
				case "class":
					scanClassT(e, lc);
					break;				
				case "help":
					scanHelp(e, lc);
					break;				
				case "module":
					scanModuleT(e, lc);
					break;				
				case "system":
					scanSystem(e, lc);
					break;		
				case "menuitem":
					lc.addMenuItem(e.getAttribute("id"), e.getAttribute("pid"), e.getAttribute("label"), e.getAttribute("action"), e.getTextContent().trim());			
					break;
				case "templates":
					scanTemplates(e, lc);
					break;				
				default:
					throw new UnknownNodeException(node);
				}
			}
		}			
	}
	private void scanGlobalT(Element global, LanguageConfig lc) throws UnknownNodeException {
		var pool = lc.getOrCreatePredefinedVariablePool(global.getAttribute("id"));
		var v= global.getChildNodes();
		for(int i = 0; i < v.getLength(); i++) {
			Node node = v.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element)node;	 
				switch (e.getTagName()) {
				case "variable":
					LangPredefinedVariable lpv = new LangPredefinedVariable();
					lpv.id = e.getAttribute("id");
					lpv.scan = e.getAttribute("scan");
					pool.variables.put(lpv.id, lpv);
					break;
				default:
					throw new UnknownNodeException(node);
				}
			}
		}	
	}
	private void scanTemplates(Element templates, LanguageConfig lc) throws UnknownNodeException {
		var t = templates.getChildNodes();
		for(int i = 0; i < t.getLength(); i++) {
			Node node = t.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element)node;	 
				switch (e.getTagName()) {
				case "template":
				{
					LangTemplate lt = new LangTemplate();
					lt.id = e.getAttribute("id");
					var temp = e.getChildNodes();
					for(int j = 0; j < temp.getLength(); j++) {
						Node node2 = temp.item(j);
						if (node2.getNodeType() == Node.ELEMENT_NODE) {
							Element ele = (Element)node2;	 
							switch (ele.getTagName()) {
							case "label":
								lt.label = ele.getTextContent().trim();
								break;				
							case "content":
								readTemplateContent(ele, lt);
								break;		
							default:
								throw new UnknownNodeException(node);
							}
						}
						
					}					
					lc.addTemplate(lt);
					break;
				}
				default:
					throw new UnknownNodeException(node);
				}
			}
		}			
	}
	private void scanSystem(Element e, LanguageConfig lc) throws UnknownNodeException {
		//Hard coded system variable names
		//ReservedVariables Interface, used in Unit class for diagram display
		//In diagrams system variables like loop count become visible, the values
		//are used to exchange the hard coded ids with user readable translated names 
		var labels = e.getChildNodes();
		for(int i = 0; i < labels.getLength(); i++) {
			Node label = labels.item(i);
			if (label.getNodeType() == Node.ELEMENT_NODE) {
				Element lab = (Element)label;	 
				switch (lab.getTagName()) {
				case "label":
					lc.registerName(lab.getAttribute("id"), lab.getTextContent().trim());
					break;				
				default:
					throw new UnknownNodeException(lab);
				}
			}
		}
	}			
    
	private void scanModuleT(Element e, LanguageConfig lc) throws UnknownNodeException {	
		LangModule lm = lc.getOrCreateLangModule(e.getAttribute("id"));
		lm.javaclass = e.getAttribute("class");
		var functions = e.getChildNodes();
		for(int i = 0; i < functions.getLength(); i++) {
			Node function = functions.item(i);
			if (function.getNodeType() == Node.ELEMENT_NODE) {
				Element functionE = (Element)function;	 				
				switch (functionE.getTagName()) {
				case "label":
					lm.label = functionE.getTextContent().trim(); 
					break;
				case "tooltip":
					lm.tooltip = readHtmlContent(functionE);
					break;
				case "description":
					lm.description = readHtmlContent(functionE);
					break;
				case "function":
					scanFunction(functionE, lm, lc);
					break;	
				case "example":
					lm.examples.add(functionE.getAttribute("file"));
					break;					
				default:
					throw new UnknownNodeException(function);
				}
			}
		}				
	}
	private void scanFunction(Element function, LangModule module, LanguageConfig lc) throws UnknownNodeException {
		LangFunction lf = module.getOrCreateFunction(function.getAttribute("scan"));
		lf.scan = function.getAttribute("scan");
		lf.id   = function.getAttribute("id");
		var children = function.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childE = (Element)child;	 				
				switch (childE.getTagName()) {
				case "label":
					lf.label = childE.getTextContent().trim();
				case "tooltip":
					lf.tooltip = readHtmlContent(childE);
					break;
				case "description":
					lf.description = readHtmlContent(childE);
					break;
				case "parameter":
					LangParameter lp = new LangParameter();
					scanParameter(childE, lp);
					lf.parameters.add(lp);
					break;	
				default:
					throw new UnknownNodeException(function);
				}
			}
		}		
	}
	private void scanParameter(Element parameter, LangParameter lp) throws UnknownNodeException {
		lp.name = parameter.getAttribute("name");
		var children = parameter.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "tooltip":
					lp.tooltip = readHtmlContent(childe);
					break;
				case "description":
					lp.description = readHtmlContent(childe);
					break;
				default:
					throw new UnknownNodeException(childe);
				}
			}
		}
	}
	private void scanHelp(Element e, LanguageConfig lc) throws UnknownNodeException {
		LangHelp lh = new LangHelp(e.getAttribute("id"));
		lh.pid = e.getAttribute("parent");
		lh.description = "";
		lh.title = "";		
		var children = e.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "label":
					lh.title = childe.getTextContent().trim();
					break;
				case "tooltip":
					lh.tooltip = childe.getTextContent().trim();
					break;
				case "description":
					lh.description = readHtmlContent(childe);
					break;
				case "example":
					lh.examples.add(childe.getAttribute("file"));
					break;
				default:
					throw new UnknownNodeException(childe);
				}
			}
		}
		lc.registerHelp(lh);		
	}
	private void scanClassT(Element e, LanguageConfig lc) throws UnknownNodeException {
		LangClass langc = lc.getOrCreateLangClass(e.getAttribute("id"));
		langc.description = "";
		langc.show = !e.getAttribute("show").equals("false"); //"" has to be true, therefore we accept any none false text as true
		if (e.getAttribute("scan").isBlank()) {
			langc.scan = new String[] {}; //empty scan attribute means existent, but auto completion invisible method, for example deprecation
			//remark String.split on "" does return an Array of size 1 split on ",," will return an empty array.
			//isn't java a beautiful and logical language?
		} else {
			langc.scan = e.getAttribute("scan").split(",");		
			for(int i = 0; i < langc.scan.length; i++) {
				langc.scan[i] = langc.scan[i].trim();
			}
		}
		var children = e.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "label":
					langc.label = childe.getTextContent().trim();
					break;
				case "tooltip":
					langc.tooltip = readHtmlContent(childe);
					break;
				case "description":
					langc.description = readHtmlContent(childe);
					break;
				case "example":
					langc.examples.add(childe.getAttribute("file"));
					break;
				case "property":
					LangProperty lp = langc.getOrCreateProperty(childe.getAttribute("scan"));
					scanProperty(lp, childe);
					break;
				case "method":
					LangMethod lm = langc.getOrCreateMethod(childe.getAttribute("scan"));
					scanMethod(lm, childe);					
					break;
				default:
					throw new UnknownNodeException(childe);
				}
			}
		}
	}
	private void scanProperty(LangProperty lp, Element e) throws UnknownNodeException {		
		lp.scan = e.getAttribute("scan");
		lp.id   = e.getAttribute("id");		
		assert(!lp.scan.isEmpty());
		var children = e.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "label":
					lp.label = childe.getTextContent().trim();
					break;
				case "tooltip":
					lp.tooltip = readHtmlContent(childe);
					break;
				case "description":
					lp.description = readHtmlContent(childe);
					break;
				default:
					throw new UnknownNodeException(childe);
				}
			}
		}		
	}
	private void scanMethod(LangMethod lm, Element e) throws UnknownNodeException {
		lm.scan = e.getAttribute("scan");
		lm.id   = e.getAttribute("id");
		assert(!lm.scan.isEmpty());
		var children = e.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "label":
					lm.label = childe.getTextContent().trim();
					break;
				case "tooltip":
					lm.tooltip = readHtmlContent(childe);
					break;
				case "description":
					lm.description = readHtmlContent(childe);
					break;
				case "parameter":
					LangParameter lp = new LangParameter();
					scanParameter(childe, lp);
					lm.addParameter(lp);
					break;	
				default:
					throw new UnknownNodeException(childe);
				}
			}
		}		
	}
	private void scanTypes(Element e) throws UnknownNodeException {
		//technical class file
		var children = e.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);		
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "class":
					scanClass(childe);
					break;
				case "module":
					scanModule(childe);
					break;
				case "global":
					scanGlobal(childe);
					break;
				default:
					throw new UnknownNodeException(childe);
				}
			}
		}
	
	}

	private void scanGlobal(Element global) throws UnknownNodeException {
		String poolId = global.getAttribute("id");
		lang.registerVariablePool(poolId, global.getAttribute("class"));
		var v= global.getChildNodes();
		for(int i = 0; i < v.getLength(); i++) {
			Node node = v.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element)node;	 
				switch (e.getTagName()) {
				case "variable":
					lang.registerPredefinedVariable(
						poolId,
						e.getAttribute("id"),
						e.getAttribute("name"),
						e.getAttribute("type")
					);
					break;
				default:
					throw new UnknownNodeException(node);
				}
			}
		}			
	}
	private void scanModule(Element mod) throws UnknownNodeException {
		String javaclass = mod.getAttribute("class");
		String id        = mod.getAttribute("id");
		lang.registerModule(id, javaclass);
		var children = mod.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);		
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "function":
					String f_id         = childe.getAttribute("id");
					String f_javamethod = childe.getAttribute("name");
					String f_return     = childe.getAttribute("return");
					LinkedList<String> f_parameters = new LinkedList<>();
					var parameters = childe.getChildNodes();
					for(int j = 0; j < parameters.getLength(); j++) {
						Node parameter = parameters.item(j);		
						if (parameter.getNodeType() == Node.ELEMENT_NODE) {
							Element parae = (Element)parameter;
							switch (parae.getTagName()) {
							case "parameter":
								f_parameters.add(parae.getAttribute("type"));	
								break;
							default:
								throw new UnknownNodeException(childe);
							}
						}
					}					
					lang.registerGlobalFunction(id, f_id, f_javamethod, f_parameters, f_return);					
					break;
				default:
					throw new UnknownNodeException(childe);
				}
			}
		}		
		
	}
	private void scanClass(Element cls) throws UnknownNodeException {
		String javaclass = cls.getAttribute("class");
		String id        = cls.getAttribute("id");
		String pid       = cls.getAttribute("super");
		lang.registerClass(id, pid, javaclass);
		var children = cls.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);		
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "property":
					String p_id   = childe.getAttribute("id");
					String p_type = childe.getAttribute("type");
					String getter = childe.getAttribute("getter");
					String setter = childe.getAttribute("setter");
					String view   = childe.getAttribute("view");
					if (view.isEmpty()) {
						view = getter;
					}
					lang.registerProperty(id, p_id, p_type, getter, setter, view);
					break;
				case "method":
					String m_id         = childe.getAttribute("id");
					String m_javamethod = childe.getAttribute("name");
					String m_return     = childe.getAttribute("return");
					LinkedList<String> m_parameters = new LinkedList<>();
					var parameters = childe.getChildNodes();
					for(int j = 0; j < parameters.getLength(); j++) {
						Node parameter = parameters.item(j);		
						if (parameter.getNodeType() == Node.ELEMENT_NODE) {
							Element parae = (Element)parameter;
							switch (parae.getTagName()) {
							case "parameter":
								m_parameters.add(parae.getAttribute("type"));
								break;
							default:
								throw new UnknownNodeException(childe);
							}
						}
					}					
					lang.registerMethod(id, m_id, m_javamethod, m_parameters, m_return);					
					break;
				default:
					throw new UnknownNodeException(childe);
				}
			}
		}		
	}
	private String readHtmlContent(Element e) {
		StringBuilder text = new StringBuilder();
		var children = e.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "indent":
					text.append("&nbsp;&nbsp;&nbsp;&nbsp;");
					break;
				case "nbsp":
					text.append("&nbsp;");
					break;
				default:
					if (childe.getTextContent().isEmpty()) {
						text.append("<"+childe.getTagName()+"/>");
					} else {
						text.append("<"+childe.getTagName()+">");
						text.append(childe.getTextContent());						
						text.append("</"+childe.getTagName()+">");						
					}
				}
			} else {
				text.append(child.getTextContent());						
			}
		}		
		String html = text.toString();
		html = html.replaceAll("\u00B1", "&plusmn;");
		html = html.replaceAll("Ü", "&Uuml;");
		html = html.replaceAll("ü", "&uuml;");
		html = html.replaceAll("Ä", "&Auml;");
		html = html.replaceAll("ä", "&auml;");
		html = html.replaceAll("Ö", "&Ouml;");
		html = html.replaceAll("ö", "&ouml;");
		html = html.replaceAll("ß", "&szlig;");
		return html;					
	}
	private void readTemplateContent(Element e, LangTemplate lt) {
		var children = e.getChildNodes();
		boolean newline = true;
		lt.start = -1;
		lt.length = 0;
		StringBuilder text = new StringBuilder();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childe = (Element)child;			
				switch (childe.getTagName()) {
				case "indent":
					text.append("    ");
					newline = false;
					break;
				case "select":
					lt.start = text.length();
					text.append(childe.getTextContent());
					lt.length = text.length() - lt.start;
					newline = false;
					break;
				case "br":
					text.append("\n");
					newline = true;
					break;
				case "nbsp":
					text.append(" ");
					newline = false;
				}
			} else {
				if (newline) {
					text.append(getPackedContent(child).replaceAll("^\\s+",""));
				} else {
					text.append(getPackedContent(child));
				}
			}
		}		
		lt.template = text.toString().replaceAll("\\s+$","");
	}
	private static String getPackedContent(Node element) {
	    if (element != null) {
	        String text = element.getTextContent();
	        if (text != null) {
	            return text.replaceAll("\\s+", " ");
	        }
	    }
	    return "";
	}		
}
