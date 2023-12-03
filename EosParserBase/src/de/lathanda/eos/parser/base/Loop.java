package de.lathanda.eos.parser.base;

import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.parser.core.Node;
import de.lathanda.eos.parser.core.Sequence;

/**
 * Gemeinsame Klasse aller Schleifen.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public abstract class Loop extends Node implements LoopUnit {
	protected final Sequence sequence;
	protected final Expression condition;

	public Loop(Sequence sequence, Expression condition) {
		this.sequence = sequence;
		this.condition = condition;
	}

	@Override
	public Sequence getSequence() {
		return sequence;
	}

	public Expression getCondition() {
		return condition;
	}
}
