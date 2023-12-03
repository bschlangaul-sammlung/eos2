package de.lathanda.eos.parser.core;

import java.util.ArrayList;
import de.lathanda.eos.parser.core.text.Text;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Marker;

/**
 * Basisklasse aller Syntaxbaumknoten
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public abstract class Node extends MarkedNode {	

	public abstract void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception;

	public abstract void resolveNamesAndTypes(Expression with, Environment env);

	public Node() {
		marker = new Marker();
	}

	/**
	 * Erzeugt eine Textlesbare Version dieses Knotens für Struktogramme.
	 * @return
	 */
	@Override
	public abstract String getLabel();

	protected final String createText(String id, Object... args) {
		return Text.LABEL.formatMessage(id, args);
	}
}
