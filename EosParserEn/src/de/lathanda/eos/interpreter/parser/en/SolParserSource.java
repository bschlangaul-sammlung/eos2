package de.lathanda.eos.interpreter.parser.en;

import java.util.ResourceBundle;

import de.lathanda.eos.parser.base.BasicParser;
import de.lathanda.eos.parser.base.ParserSource;

public class SolParserSource implements ParserSource {
	public static final ResourceBundle LANG = ResourceBundle.getBundle("sollang.sollang"); 
	@Override
	public String getName() {
		return LANG.getString("Title");
	}

	@Override
	public String getDescription() {
		return LANG.getString("Description");
	}

	@Override
	public String getHelp() {
		return LANG.getString("Help");
	}

	@Override
	public BasicParser createParser(String source) {
		return new SolParser(source);
	}

	@Override
	public String getID() {
		return "eos2.english";
	}

}
