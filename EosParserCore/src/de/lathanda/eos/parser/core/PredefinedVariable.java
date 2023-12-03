package de.lathanda.eos.parser.core;


public class PredefinedVariable {
	private final String scan;
	private final Type type;
	private final Object initialValue;
	
	public PredefinedVariable(String scan, Type type, Object initialValue) {
		super();
		this.scan = scan;
		this.type = type;
		this.initialValue = initialValue;
	}
	public String getScan() {
		return scan;
	}
	public Type getType() {
		return type;
	}
	public Object getInitialValue() {
		return initialValue;
	}	
}
