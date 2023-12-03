module de.lathanda.eos.lib {
	exports de.lathanda.eos.geo;
	exports de.lathanda.eos.geo.gui;
	exports de.lathanda.eos.geo.exceptions;
	exports de.lathanda.eos.geo.gui.event;

	requires transitive de.lathanda.eos.base;
	requires transitive java.desktop;
	
	opens de.lathanda.eos.geo.icons;
}