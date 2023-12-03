package de.lathanda.eos.lang;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.base.extension.XMLProvider;

public class LanguageLoader implements XMLProvider {

	@Override
	public Document[] getXML() {
		Document[] extension = new Document[2];
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();		
			extension[0] = dBuilder.parse(ResourceLoader.getResourceAsStream("de/lathanda/eos/lang/eos2_classes.xml"));
			extension[1] = dBuilder.parse(ResourceLoader.getResourceAsStream("de/lathanda/eos/lang/eos2_lang.xml"));
		} catch (Exception e) {
			e.printStackTrace();
			return new Document[] {};
		}
		return extension;
	}
}
