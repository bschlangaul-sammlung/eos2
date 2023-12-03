package de.lathanda.eos.parser.core;

/**
 * Gemeinsame abstrakte Klasse aller Ausdrücke.
 * Die Klasse stellt eine Reihe von Hilfsfunktionen zur Verfügung,
 * die in der Grammatik verwendet werden.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public abstract class Expression extends Node {
	protected int prio = 0;


	protected String getLabelLeft(Expression e) {
		if (prio > e.prio) {
			return "(" + e.getLabel() + ")";
		} else {
			return e.getLabel();
		}
	}

	protected String getLabelRight(Expression e) {
		if (prio >= e.prio) {
			return "(" + e.getLabel() + ")";
		} else {
			return e.getLabel();
		}
	}
}
