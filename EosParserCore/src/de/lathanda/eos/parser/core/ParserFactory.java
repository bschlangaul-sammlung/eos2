package de.lathanda.eos.parser.core;


/**
 * Fabrikschnittstelle für Parser.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface ParserFactory {
	String getID();	
	BasicParser createParser(String source);
}
