package de.lathanda.eos.parser.base;

import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.Expression;
import de.lathanda.eos.parser.core.Node;
import de.lathanda.eos.parser.core.SystemType;
import de.lathanda.eos.parser.core.Type;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.DebugPoint;
import de.lathanda.eos.vm.ReservedVariables;
import de.lathanda.eos.vm.commands.CreateVariable;
import de.lathanda.eos.vm.commands.LoadVariable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Speichert und behandelt eine Variablendeklaration.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Declaration extends Node {
	protected final List<String> names;
	protected Type vartype;

	public Declaration() {
		this.names = new LinkedList<>();
		this.type = Type.getVoid();
		this.vartype = Type.getUnknown();
	}

	public void addName(String name) {
		names.add(name.toLowerCase());
	}

	public List<String> getNames() {
		return names;
	}

	public void setType(Type vartype) {
		this.vartype = vartype;
		names.stream().forEach((name) -> Program.addGuess(name, vartype));
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		ops.add(new DebugPoint(marker));
		for (String name : names) {
			if (autoWindow && vartype.inherits(SystemType.getFigure()) && !vartype.isAbstract()) {
				ops.add(new CreateVariable(name, vartype.getMType()));
				ops.add(new LoadVariable(name));
				ops.add(new LoadVariable(ReservedVariables.WINDOW));
				SystemType.getAddFigureMethod().compile(ops, null, autoWindow);
			} else {
				ops.add(new CreateVariable(name, vartype.getMType()));
			}
		}
		;
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		names.stream().forEach((name) -> {
			if (env.isVariableDefined(name)) {
				env.addError(marker, "DoubleVariableDefinition", name);
			} else {
				env.setVariableType(name.toLowerCase(), vartype);
			}
		});

		if (vartype.isUnknown()) {
			env.addError(marker, "UnknownType", vartype);
		} else if (vartype == SystemType.getWindow()) {
			// a window variable was found, this information is used to
			// determine if automatic window has to be generated
			env.setWindowExists();
		} else if (vartype.inherits(SystemType.getFigure())) {
			// a figure variable was found, this information is used to
			// determine if automatic window has to be generated
			env.setFigureExists();
		}
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		boolean first = true;
		for (String name : names) {
			if (first) {
				res.append(name);
			} else {
				res.append(",").append(name);
				first = false;
			}
		}
		res.append(":").append(vartype);
		return res.toString();
	}

	@Override
	public String getLabel() {
		StringBuilder text = new StringBuilder();
		boolean first = true;
		for (String name : names) {
			if (first) {
				text.append(name);
				first = false;
			} else {
				text.append(", ").append(name);
			}
		}
		text.append(":").append(vartype);
		return text.toString();
	}
}
