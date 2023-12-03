import de.lathanda.eos.base.extension.XMLProvider;
import de.lathanda.eos.robot.extension.ExtensionLoader;
module de.lathanda.eos.robot {
	exports de.lathanda.eos.robot.exceptions;
	exports de.lathanda.eos.robot;
	exports de.lathanda.eos.robot.gui;
	exports de.lathanda.eos.robot.geom3d;

	requires transitive de.lathanda.eos.base;
	requires transitive java.desktop;
	requires java.xml;
	requires transitive jogamp.fat;
	
	opens de.lathanda.eos.robot.obj;
	opens de.lathanda.eos.robot.text;
	opens de.lathanda.eos.robot.icons;
	opens de.lathanda.eos.robot.extension;
	provides XMLProvider with ExtensionLoader;
}
