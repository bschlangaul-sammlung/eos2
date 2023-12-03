package de.lathanda.eos.gui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * \brief Texte
 * 
 * Zentrale Quelle für alle übersetzungsabhängigen Texte.
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class Messages {
	private static ResourceBundle ERROR = ResourceBundle.getBundle("de.lathanda.eos.gui.text.error");
	private static ResourceBundle GUI = ResourceBundle.getBundle("de.lathanda.eos.gui.text.gui");

	private Messages() {
	}

	public static String getError(String id) {
		return ERROR.getString(id);
	}

	public static String formatError(String id, Object... info) {
		return MessageFormat.format(ERROR.getString(id), info);
	}

	public static String getString(String id) {
		return GUI.getString(id);
	}
}
