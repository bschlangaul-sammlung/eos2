package de.lathanda.eos.parser.core;

import java.util.ArrayList;


import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MType;

/**
 * Methodendefinition.
 *
 * @author Peter (Lathanda) Schneider
 */
public interface MethodType {

	public boolean checkType(Type[] args);

	public Type getReturnType();

	public Type getParameterType(int i);

	public MType[] getParameters();

	public String getName();
	
	public void compile(ArrayList<Command> ops, Expression target, boolean autowindow) throws Exception;
	
}
