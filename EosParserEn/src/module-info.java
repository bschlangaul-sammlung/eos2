module de.lathanda.eos.parser.en {
	exports de.lathanda.eos.interpreter.parser.en;
	requires de.lathanda.eos.base;
	requires de.lathanda.eos.vm;
	requires transitive de.lathanda.eos.parsetree;
	requires java.desktop;
	provides de.lathanda.eos.parser.base.ParserFactory with de.lathanda.eos.interpreter.parser.en.SolParserFactory;	
}