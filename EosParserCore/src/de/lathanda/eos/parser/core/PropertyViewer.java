package de.lathanda.eos.parser.core;

import java.lang.reflect.Method;
import de.lathanda.eos.vm.MObject;

public abstract class PropertyViewer {
	public final String name;
	private PropertyViewer(String name) {
		this.name = name;
	}
	public abstract Object getValue();
	public static class SystemTypePropertyViewer extends PropertyViewer {
		private final Method method;
		private final Object target;
		public SystemTypePropertyViewer(String name, Method method, Object target) {
			super(name);
			this.method = method;
			this.target = target;
		}
		public Object getValue() {
			try {
				return method.invoke(target);
			} catch (Exception e) {
				return "";
			}
		}		
	}
	public static class UserClassPropertyViewer extends PropertyViewer {
		private final MObject target;
		public UserClassPropertyViewer(String name, Object target) {
			super(name);
			this.target = (MObject)target;
		}
		public Object getValue() {
			try {
				return target.getProperty(name);
			} catch (Exception e) {
				return "";
			}
		}		
	}
}
