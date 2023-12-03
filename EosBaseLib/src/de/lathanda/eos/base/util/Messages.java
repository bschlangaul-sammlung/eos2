package de.lathanda.eos.base.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * \brief Texte
 * 
 * Zentrale Quelle für alle übersetzungsabhängigen Texte.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.5
 */
public class Messages {
	private ResourceBundle rb;

	public Messages(ResourceBundle rb) {
		this.rb = rb;
	}

	public String formatMessage(String id, Object... info) {
		return MessageFormat.format(rb.getString(id), info);
	}

	public String getMessage(String id) {
		return rb.getString(id);
	}
}
