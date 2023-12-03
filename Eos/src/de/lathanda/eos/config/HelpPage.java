package de.lathanda.eos.config;

import java.util.Collection;
import java.util.LinkedList;


public class HelpPage {
	private String html;
	private String tooltip;
	private String id;
	private String title;
	private String pid;
	private LinkedList<HelpPage> children;
	
	public HelpPage(String id, String pid, String title, String tooltip, String html) {
		super();
		this.html = html;
		this.tooltip = tooltip;
		this.id = id;
		this.title = title;
		this.pid = pid;
		children = new LinkedList<>();
	}
	public String getHtml() {
		return html;
	}
	public void addChild(HelpPage hp) {
		children.add(hp);
		
	}
	public String getTooltip() {
		return tooltip;
	}
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getPid() {
		return pid;
	}
	public Collection<HelpPage> getChildren() {
		return children;
	}
	
}