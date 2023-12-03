package de.lathanda.eos.parser.core;


import de.lathanda.eos.vm.Marker;

/**
 * Parameter.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class Parameter {
	private final String name;
	private final Type type;
  private final Marker marker;
    
   public Parameter(String name, Type parameterType, Marker marker) {
      this.name = name;
      this.type = parameterType;
      this.marker = marker;
  }

	public String getName() {
		return name;
	}

	public void registerParameter(Environment env) {
    	if (env.isVariableDefined(name)) {
    		env.addError(marker, "DoubleVariableDefinition", name);
    	} else {
    		env.setVariableType(name.toLowerCase(), type);
    	} 
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return name + ":" + type;
	}
}
