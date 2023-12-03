package de.lathanda.eos.parser.de;

import de.lathanda.eos.interpreter.parser.core.BasicParser;
import de.lathanda.eos.interpreter.parser.core.ParserFactory;

public class EosParserFactory implements ParserFactory {
	public static final EosParserFactory def = new EosParserFactory();
	@Override
	public BasicParser createParser(String source) {		
		return new EosParser(source);
	}
	@Override
	public String getID() {
		return "eos.parser";
	}

}
