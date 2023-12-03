package de.lathanda.eos.parser.base;

import java.util.ArrayList;
import java.util.TreeMap;

import de.lathanda.eos.baseparser.AutoCompleteInfo;
import de.lathanda.eos.baseparser.AutoCompleteInformation;
import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.MarkedNode;
import de.lathanda.eos.baseparser.Method;
import de.lathanda.eos.baseparser.Property;
import de.lathanda.eos.baseparser.Self;
import de.lathanda.eos.baseparser.Signatures;
import de.lathanda.eos.baseparser.UserClass;
import de.lathanda.eos.baseparser.UserType;
import de.lathanda.eos.parser.base.exceptions.CyclicStorageException;
import de.lathanda.eos.parser.base.exceptions.DoubleMethodDeclarationException;
import de.lathanda.eos.parser.base.exceptions.DoublePropertyDeclarationException;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.ReservedVariables;
import de.lathanda.eos.vm.commands.LoadVariable;

/**
 * Speichert und behandelt Benutzerdefinierte Klassen
 *
 * @author Peter (Lathanda) Schneider
 */
public class UserClass extends MarkedNode {
	private final TreeMap<String, Method> meths = new TreeMap<>();
	private final TreeMap<String, Property> props = new TreeMap<>();
	private final String name;
	private String sup;
	private Self self;

	/** 
	 * @param name Name der Klasse
	 */
	public UserClass(String name) {
		this.name = name;
		sup = null;
		self = new Self();
		type = self.getType();
	}

	public void setSuperClass(String name) {
		sup = name;
	}

	public void define() {
		self.define();
	}

	public void addProperty(Property prop) throws DoublePropertyDeclarationException {
		for (String name : prop.getNames()) {
			if (props.containsKey(name)) {
				throw new DoublePropertyDeclarationException(name, this.name);
			}
			props.put(name, prop);
		}
	}

	public void addMethod(Method meth) throws DoubleMethodDeclarationException {
		String key = Signatures.createVMMethodSignature(meth.getMethodType());
		if (meths.containsKey(key)) {
			throw new DoubleMethodDeclarationException(meth.toString(), this.name);
		}
		meths.put(key, meth);
	}

	public String getName() {
		return name;
	}

	@Override
	public String getLabel() {
		return name;
	}

	public void bind(Environment env) {
		self.bind(env);
	}

	public void checkCyclicStorage() throws CyclicStorageException {
		self.ut.checkCyclicStorage();
	}

	public void compile() throws Exception {
		self.ut.compile();
	}

	public void resolveNamesAndTypes(Environment env) {
		env.storeVariables();
		for (Property p : props.values()) {
			p.resolveNamesAndTypes(self, env);
		}
		for (Method m : meths.values()) {
			env.setVariableType(ReservedVariables.SELF, self.ut);
			m.resolveNamesAndTypes(self, env);
			env.restoreVariables();
		}
		env.restoreVariables();
	}

	public AutoCompleteInformation getAutoComplete() {
		return new AutoCompleteInfo(name, self.ut, AutoCompleteInformation.CLASS, name.length(), 0);
	}

	private class Self extends Expression {
		private UserType ut;

		public Self() {
			ut = new UserType(UserClass.this.name);
			type = ut;
		}

		public void define() {
			ut.define();
		}

		public void bind(Environment env) {
			ut.setSuperclass(sup);
			for (Method m : meths.values()) {
				ut.addMethod(m);
			}
			for (Property p : props.values()) {
				ut.addProperty(p);
			}
			ut.bind(env);
		}

		@Override
		public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
			ops.add(new LoadVariable(ReservedVariables.SELF));
		}

		@Override
		public void resolveNamesAndTypes(Expression with, Environment env) {
			// nothing to do
		}

		@Override
		public String getLabel() {
			return createText("Class.Self");
		}
	}
}
