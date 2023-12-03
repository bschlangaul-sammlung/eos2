package de.lathanda.eos.parser.core;


public interface ParserSource {
	String getID();

	String getName();

	String getDescription();

	String getHelp();

	BasicParser createParser(String source);
}
