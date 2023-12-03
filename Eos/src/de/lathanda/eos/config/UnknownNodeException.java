package de.lathanda.eos.config;

import org.w3c.dom.Node;

import de.lathanda.eos.gui.Messages;

public class UnknownNodeException extends Exception {
	private static final long serialVersionUID = -3285637640502466454L;
	private Node node;
	public UnknownNodeException(Node node) {
		this.node = node;
	}
	@Override
	public String getLocalizedMessage() {
		return Messages.formatError("UnknownNode", node.getNodeName(), node.getNodeValue());
	}
}
