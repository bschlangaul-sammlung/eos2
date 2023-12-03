module de.lathanda.eos.base {
	exports de.lathanda.eos.game.tools;
	exports de.lathanda.eos.base.util;
	exports de.lathanda.eos.base.math;
	exports de.lathanda.eos.game;
	exports de.lathanda.eos.base.layout;
	exports de.lathanda.eos.game.geom.tesselation;
	exports de.lathanda.eos.base;
	exports de.lathanda.eos.game.geom;
	exports de.lathanda.eos.base.event;
	exports de.lathanda.eos.base.icons;
	exports de.lathanda.eos.base.extension;

	opens de.lathanda.eos.base.icons;
	requires transitive java.desktop;
	requires java.xml;
}