import de.lathanda.eos.base.extension.XMLProvider;
import de.lathanda.eos.lang.LanguageLoader;

module de.lathanda.eos.gui {
	requires de.lathanda.eos.lib;
	requires de.lathanda.eos.robot;
	requires java.datatransfer;
	requires transitive java.desktop;
	requires java.xml;
	requires de.lathanda.eos.parsetree;
	requires de.lathanda.eos.parser.de;
	requires de.lathanda.eos.vm;
	requires de.lathanda.eos.base;
	opens de.lathanda.eos.examples;
	opens de.lathanda.eos.lang;
	opens de.lathanda.eos.gui.text;
	opens de.lathanda.eos.gui.images;
	opens de.lathanda.eos.gui.icons;
	uses XMLProvider;
	provides XMLProvider with LanguageLoader;
	
	exports de.lathanda.eos.gui.icons;
}