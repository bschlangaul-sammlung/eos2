package de.lathanda.eos.parser.core;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.lathanda.eos.parser.core.exceptions.WrongPlaceForDeclarationException;
import de.lathanda.eos.vm.ErrorInformation;
import de.lathanda.eos.vm.Marker;

/**
 * Umgebung für die semantische Übersetzung. Sie enthält alle Variablen und
 * Funktionen die gerade bekannt bzw. gültig sind.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class Environment {

	/*
	 * semantic errors
	 */
	private final LinkedList<ErrorInformation> errors = new LinkedList<>();
	/*
	 * currently valid local variables
	 */
	private final TreeMap<String, Type> variables = new TreeMap<>();
	/*
	 * currently valid global variables
	 */
	private final TreeMap<String, Type> storedVariables = new TreeMap<>();
	/*
	 * global language constants
	 */
	private final TreeMap<String, Type> constants = new TreeMap<>();
	/*
	 * user defined functions
	 */
	private final TreeMap<String, MethodType> functions;
	/*
	 * flag indicates if a window is part of the program
	 */
	private boolean hasWindow = false;
	/*
	 * flag indicates if the program contains at least one figure
	 */
	private boolean hasFigure = false;
	/*
	 * name of default window
	 */
	private final String defaultWindowName;
	/*
	 * lock properties
	 */
	private final boolean lockProperties;
	/*
	 * variable declaration is only allow on level 0 or within procedures.
	 */
	private int variableLock = 0;
	/*
	 * next unique id, for creating artifical unique variables, eg counting variable
	 * for counting loop
	 */
	private int uid = 0;
	/**
	 * program associated with this environment
	 */
	private final AbstractProgram program;

	public Environment(AbstractProgram p, String defaultWindowName, boolean lockProperties, Collection<PredefinedVariable> prevar) {
		this.lockProperties = lockProperties;
		this.defaultWindowName = defaultWindowName;
		this.functions = new TreeMap<>();
		for(PredefinedVariable pv:prevar) {
			constants.put(pv.getScan(), pv.getType());			
		}
		program = p;
	}

	public Environment(boolean lockProperties, AbstractProgram p, String defaultWIndowName, Collection<PredefinedVariable> prevar) {
		this.lockProperties = lockProperties;
		this.defaultWindowName = defaultWIndowName;
		this.functions = new TreeMap<>();
		for(PredefinedVariable pv:prevar) {
			constants.put(pv.getScan(), pv.getType());			
		}
		program = p;
	}

	public AbstractProgram getProgram() {
		return program;
	}

	public void addError(Marker marker, String errorId, Object... data) {
		errors.add(new CompilerError(marker, errorId, data));
	}

	public LinkedList<ErrorInformation> getErrors() {
		return errors;
	}

	public Type getVariableType(String name) {
		return variables.getOrDefault(name, constants.getOrDefault(name, Type.getUnknown()));
	}

	public boolean isVariableConstant(String member) {
		return constants.containsKey(member);
	}

	public void setVariableType(String name, Type type) {
		if (variableLock == 0) {
			variables.put(name, type);
		} else {
			throw new WrongPlaceForDeclarationException(name, type.getID());
		}
	}

	public boolean isVariableDefined(String name) {
		return variables.containsKey(name) || constants.containsKey(name);
	}

	public void resetVariables() {
		variables.clear();
	}

	public void resetAll() {
		variables.clear();
		errors.clear();
		functions.clear();
		storedVariables.clear();
		hasFigure = false;
		hasWindow = false;
	}

	public MethodType getFunctionSignature(String name, int args) {
		return functions.get(name.toLowerCase() + "(" + args + ")");
	}

	public void setFunctionSignature(String name, int args, MethodType methodType) {
		functions.put(name.toLowerCase() + "(" + args + ")", methodType);
	}

	public boolean isFunctionDefined(String name, int args) {
		return functions.containsKey(name.toLowerCase() + "(" + args + ")");
	}

	public int getUID() {
		return uid++;
	}

	public void setWindowExists() {
		hasWindow = true;
	}

	public void setFigureExists() {
		hasFigure = true;
	}

	public boolean getAutoWindow() {
		return !hasWindow && hasFigure;
	}

	public void prohibitVariableDeclaration() {
		variableLock++;
	}

	public void allowVariableDeclaration() {
		variableLock--;
	}

	public void storeVariables() {
		storedVariables.putAll(variables);
	}

	public void restoreVariables() {
		variables.clear();
		variables.putAll(storedVariables);
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("environment\n");
		res.append("hasWindow = ").append(hasWindow).append("\n");
		res.append("hasFigure = ").append(hasFigure).append("\n");
		res.append("variableLock = ").append(variableLock).append("\n");
		res.append("local variable:\n");
		for (Entry<String, Type> v : variables.entrySet()) {
			res.append(v.getKey()).append(":").append(v.getValue()).append("\n");
		}
		res.append("global variable:\n");
		for (Entry<String, Type> v : storedVariables.entrySet()) {
			res.append(v.getKey()).append(":").append(v.getValue()).append("\n");
		}
		res.append("user defined functions:\n");
		for (Entry<String, MethodType> f : functions.entrySet()) {
			res.append(f.getValue()).append("\n");
		}
		res.append("endenvironment\n");
		return res.toString();
	}

	public String getDefaultWindowName() {
		return defaultWindowName;
	}

	public boolean getLockProperties() {
		return lockProperties;
	}
}
