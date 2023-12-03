module de.lathanda.eos.vm {
	exports de.lathanda.eos.vm.commands;
	exports de.lathanda.eos.vm.exceptions;
	exports de.lathanda.eos.vm;

	requires java.desktop;
	requires transitive de.lathanda.eos.base;
	
	opens de.lathanda.eos.vm.text;
}