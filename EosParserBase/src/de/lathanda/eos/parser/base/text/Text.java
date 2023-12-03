package de.lathanda.eos.parser.base.text;

import java.util.ResourceBundle;

import de.lathanda.eos.base.util.Messages;

public interface Text {
	static final Messages ERROR = new Messages(ResourceBundle.getBundle("de.lathanda.eos.parser.base.text.error"));
	static final Messages LABEL = new Messages(ResourceBundle.getBundle("de.lathanda.eos.parser.base.text.label"));
	
}
