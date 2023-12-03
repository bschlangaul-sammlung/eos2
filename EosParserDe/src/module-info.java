module de.lathanda.eos.parser.de {
	exports de.lathanda.eos.interpreter.parser.de;

	requires de.lathanda.eos.base;
	requires de.lathanda.eos.vm;
	requires transitive de.lathanda.eos.parser.core;
	requires transitive de.lathanda.eos.parsetree;
	requires java.desktop;
	provides de.lathanda.eos.parser.core.ParserFactory with de.lathanda.eos.parser.de.EosParserFactory;
}