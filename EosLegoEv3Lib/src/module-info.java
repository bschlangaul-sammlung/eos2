import de.lathanda.eos.base.extension.XMLProvider;
import de.lathanda.eos.ev3.extension.ExtensionLoader;

module EosLegoEv3Lib {
	exports de.lathanda.eos.ev3.comm;
	exports de.lathanda.eos.ev3.exceptions;
	exports de.lathanda.eos.ev3.bytecode;
	exports de.lathanda.eos.ev3.brick;

	requires java.desktop;
	requires de.lathanda.eos.base;
	provides XMLProvider with ExtensionLoader;
}