package de.lathanda.eos.parser.core;


import de.lathanda.eos.vm.Marker;
import de.lathanda.eos.vm.ProgramNode;
/**
 * Markierbarer Knoten
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public abstract class MarkedNode implements ProgramNode {
	protected Marker marker;
	protected Type type = Type.getVoid();

	public MarkedNode() {
		super();
	}

	@Override
	public final Marker getMarker() {
		return marker;
	}

	public final void setMarker(Marker cr) {
		marker = cr;
		marker.setNode(this);
	}

	public final void sameMarker(Node node) {
		marker.extend(node.marker);
	}

	public final Type getType() {
		return type;
	}
}