package de.lathanda.eos.parser.base;

import de.lathanda.eos.baseparser.Environment;
import de.lathanda.eos.baseparser.Expression;
import de.lathanda.eos.baseparser.MethodType;
import de.lathanda.eos.baseparser.Node;
import de.lathanda.eos.baseparser.Parameter;
import de.lathanda.eos.baseparser.Parameters;
import de.lathanda.eos.baseparser.ProgramUnit;
import de.lathanda.eos.baseparser.Sequence;
import de.lathanda.eos.baseparser.Type;
import de.lathanda.eos.baseparser.UserFunctionType;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.ReservedVariables;
import de.lathanda.eos.vm.commands.CreateVariable;
import de.lathanda.eos.vm.commands.LoadVariable;
import de.lathanda.eos.vm.commands.StoreVariable;

import java.util.ArrayList;

/**
 * Speichert und behandelt eine
 * Funktion, Methode oder Benutzerfunktion.
 * 
 * @author Peter (Lathanda) Schneider
 */
public class SubRoutine extends Node implements ProgramUnit {
	private final String name;
	private final Sequence sequence;
	private final Parameters parameters;
	private final Type returnType;
	private final boolean globalAccess;
	private MethodType methodType;

	public SubRoutine(String name, Parameters parameters, Sequence sequence, Type returnType, boolean globalAccess) {
		this.name = name;
		this.parameters = (parameters != null) ? parameters : new Parameters();
		this.sequence = sequence;
		this.returnType = (returnType == null) ? Type.getVoid() : returnType;
		this.globalAccess = globalAccess;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Sequence getSequence() {
		return sequence;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		for (Parameter p : parameters.getParameters()) {
			ops.add(new CreateVariable(p.getName().toLowerCase(), p.getType().getMType()));
			ops.add(new StoreVariable(p.getName().toLowerCase()));
		}
		if (returnType != null && !returnType.isVoid()) {
			ops.add(new CreateVariable(ReservedVariables.RESULT, returnType.getMType()));
		}

		sequence.compile(ops, autoWindow);
		if (returnType != null && !returnType.isVoid()) {
			ops.add(new LoadVariable(ReservedVariables.RESULT));
		}
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		if (parameters != null) {
			parameters.registerParameters(env);
		}
		if (returnType != null && !returnType.isVoid()) {
			env.setVariableType("1x", returnType);
		}
		sequence.resolveNamesAndTypes(with, env);
	}

	public void registerSub(Environment env) {
		createMethodType(env);
		if (env.isFunctionDefined(name, (parameters == null) ? 0 : parameters.size())) {
			env.addError(marker, "DoubleMethodDefinition", name, (parameters == null) ? 0 : parameters.size());
		} else {
			if (parameters != null) {
				env.setFunctionSignature(name.toLowerCase(), parameters.size(), methodType);
			} else {
				env.setFunctionSignature(name.toLowerCase(), 0, methodType);
			}
		}
	}

	public MethodType getMethodType(Environment env) {
		if (methodType == null) {
			createMethodType(env);
		}
		return methodType;
	}

	private void createMethodType(Environment env) {
		if (methodType != null) {
			return;
		}
		Type[] para;
		if (parameters != null) {
			para = parameters.getTypes();
		} else {
			para = new Type[] {};
		}
		if (returnType != null && returnType.isUnknown()) {
			env.addError(marker, "UnknownType", returnType);
		}
		methodType = new UserFunctionType(name, para, returnType);
	}

	public boolean getGlobalAccess() {
		return globalAccess;
	}

	@Override
	public String getLabel() {
		return name;
	}

	public Type[] getParameterTypes() {
		return parameters.getTypes();
	}
}
