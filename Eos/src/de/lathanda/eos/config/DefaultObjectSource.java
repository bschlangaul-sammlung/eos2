package de.lathanda.eos.config;

import de.lathanda.eos.vm.ObjectSource;

public class DefaultObjectSource implements ObjectSource {
	private Class<?> cls;
	public DefaultObjectSource(String cls) throws ClassNotFoundException {
		this.cls = Class.forName(cls);
	}
	@Override
	public Object createObject() {
		try {
			return cls.getConstructor().newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
}
