package de.lathanda.eos.parser.core;

import de.lathanda.eos.parser.core.exceptions.TranslationException;

/**
 * Parser Schnittstelle.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface BasicParser {
	void parse(AbstractProgram program, String path) throws TranslationException;

	int getLine(int pos);
}
