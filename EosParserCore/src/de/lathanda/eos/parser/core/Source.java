package de.lathanda.eos.parser.core;

/**
 * Kommunikationsschnittstelle vom Compiler zum Quellcode.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public interface Source {
	String getSourceCode();

	String getPath();
}
