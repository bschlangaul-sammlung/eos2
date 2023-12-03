package de.lathanda.eos.interpreter.parser.en;

import de.lathanda.eos.parser.base.BasicParser;
import de.lathanda.eos.parser.base.ParserFactory;

public class SolParserFactory implements ParserFactory {
	public static final SolParserFactory def = new SolParserFactory();
	@Override
	public BasicParser createParser(String source) {		
		return new SolParser(source);
	}
	@Override
	public String getID() {
		return "sol.parser";
	}

}
