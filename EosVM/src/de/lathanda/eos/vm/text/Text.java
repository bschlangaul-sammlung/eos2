package de.lathanda.eos.vm.text;

import java.util.ResourceBundle;

import de.lathanda.eos.base.util.Messages;

public interface Text {
	static final Messages ERROR = new Messages(ResourceBundle.getBundle("de.lathanda.eos.vm.text.error"));
}
