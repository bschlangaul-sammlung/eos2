package de.lathanda.eos.parser.base;

import de.lathanda.eos.parser.core.Node;
import de.lathanda.eos.parser.core.ProgramUnit;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.ReservedVariables;
import de.lathanda.eos.vm.commands.CreateVariable;
import de.lathanda.eos.vm.commands.DefineVariable;
import de.lathanda.eos.vm.commands.LoadVariable;
import de.lathanda.eos.vm.commands.StoreVariable;

import java.util.ArrayList;

/**
 * Speichert und behandelt eine Methode innerhalb einer Benutzer definierten Klasse.
 * 
 * @author Peter (Lathanda) Schneider
 */
public class Method extends Node implements ProgramUnit {
	private final String name;
	private final Sequence sequence;
	private final Parameters parameters;
	private final Type returnType;
	private UserMethodType methodType;
	private final UserClass uc;

	public Method(String name, Parameters parameters, Sequence sequence, Type returnType, UserClass uc) {
		this.name = name;
		this.parameters = (parameters != null) ? parameters : new Parameters();
		this.sequence = sequence;
		this.returnType = (returnType == null) ? Type.getVoid() : returnType;
		this.uc = uc;
	}

	public String getName() {
		return name;
	}

	public Sequence getSequence() {
		return sequence;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		for (Parameter p : parameters.getParameters()) {
			ops.add(new DefineVariable(p.getName(), p.getType().getMType()));
			ops.add(new StoreVariable(p.getName()));
		}
		if (returnType != null && !returnType.isVoid()) {
			ops.add(new CreateVariable(ReservedVariables.RESULT, returnType.getMType()));
		}

		sequence.compile(ops, autoWindow);
		if (returnType != null && !returnType.isVoid()) {
			ops.add(new LoadVariable(ReservedVariables.RESULT));
		}
		methodType.createMProcedure(ops);
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

	public MethodType getMethodType(Environment env) {
		if (methodType == null) {
			createMethodType(env);
		}
		return methodType;
	}

	public MethodType getMethodType() {		
		return methodType;
	}
	public void createMethodType(Environment env) {
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
		methodType = new UserMethodType(name, para, returnType);
	}

	@Override
	public String getLabel() {
		return name;
	}

	public AutoCompleteInformation getAutoComplete() {
		StringBuilder autocomplete = new StringBuilder(name);
		autocomplete.append("(");
		boolean first = true;
		int start = autocomplete.length()+1;
		int length = 0; 
		for(var p:parameters.getParameters()) {
			if (!first) {
				autocomplete.append(", ");
			} else {
				start = autocomplete.length();
				length = p.getName().length();
			}
			autocomplete.append(p.getName());
		}
		autocomplete.append(")");
		return new AutoCompleteInfo(autocomplete.toString(), uc.getType(), AutoCompleteInformation.METHOD, start, length);
	}

}
