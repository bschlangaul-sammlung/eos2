module de.lathanda.eos.parsetree {
	exports de.lathanda.eos.parser.base;
	exports de.lathanda.eos.parser.base.exceptions;
	
	requires transitive de.lathanda.eos.vm;
	requires transitive de.lathanda.eos.parser.core;
	opens de.lathanda.eos.parser.base.text;
}