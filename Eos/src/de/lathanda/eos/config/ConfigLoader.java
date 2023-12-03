package de.lathanda.eos.config;

import java.io.File;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigLoader {
	private static ConfigLoader singleton = new ConfigLoader();
	private LinkedList<Document> docs = new LinkedList<>();

	private ConfigLoader() {
	}

	public ConfigLoader getInstance() {
		return singleton;
	}

	public void load(File base) throws Exception {
		File[] configs = base.listFiles((File f) -> {
			return f.getName().endsWith(".xml");
		});
		for (File f : configs) {
			if (f.exists() && f.isFile()) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(f);
				docs.add(doc);
			}
		}
	}

	public void apply() {
		for (Document doc : docs) {
			apply(doc);
		}
	}

	private void apply(Document doc) {
		NodeList n = doc.getChildNodes();
		for (int i = 0; i < n.getLength(); i++) {
			switch (n.item(i).getNodeName()) {
			case "types":
				applyTypes(n.item(i));
				break;
			default:
				// ignore wrong types
			}
		}
	}

	private void applyTypes(Node e) {
		NodeList n = e.getChildNodes();
		for (int i = 0; i < n.getLength(); i++) {
			switch (n.item(i).getNodeName()) {
			case "class":
				applyClass(n.item(i));
				break;
			default:
				// ignore wrong types
			}
		}
	}

	private void applyClass(Node e) {
		var attributes = e.getAttributes();
		attributes.getNamedItem("class").getNodeValue();
		attributes.getNamedItem("class").getNodeValue();
		NodeList n = e.getChildNodes();
		for (int i = 0; i < n.getLength(); i++) {
			switch (n.item(i).getNodeName()) {
			case "method":
				break;
			case "property":
				break;
			case "function":
				break;
			default:
				// ignore wrong types
			}
		}
	}
}
