package de.lathanda.eos.parser.base;

import java.util.ArrayList;

import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.MethodType;
import de.lathanda.eos.baseparser.Property;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.baseparser.UserType;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.MType;
import de.lathanda.eos.vm.commands.StoreProperty;
/**
 * Zuweisung bei Benutzerdefinierten Attributen.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class UserAssignProperty implements MethodType {
	private final UserType userType;
	private final Property property;
	private final String name;
	public UserAssignProperty(UserType userType, String name, Property property) {
		this.property = property;
		this.userType = userType;
		this.name = name;
	}

	@Override
	public boolean checkType(Type[] types) {
		return types.length == 0;
	}

	@Override
	public Type getReturnType() {
		return Type.VOID;
	}

	@Override
	public Type getParameterType(int i) {
		if (i == 0) {
			return property.vartype;
		} else {
			return null;
		}
	}

	@Override
	public MType[] getParameters() {
		return new MType[] {};
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void compile(ArrayList<Command> ops, Expression target, boolean autowindow) throws Exception {
		target.compile(ops, autowindow);
		ops.add(new StoreProperty(userType.getMClass(), name));
	}
}
