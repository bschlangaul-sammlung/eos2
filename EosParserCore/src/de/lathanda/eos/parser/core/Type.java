package de.lathanda.eos.parser.core;

import java.util.LinkedList;
import java.util.TreeMap;
import de.lathanda.eos.vm.MType;

/**
 * Basis Typdefinition.
 * Diese Klasse enthält alle Informationen zu den Eos Typen. 
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class Type implements Comparable<Type>, AutoCompleteType {
	protected Type superType;
	// properties
	protected final String id;

	protected final TreeMap<String, LinkedList<MethodType> > methods = new TreeMap<>();
	protected final LinkedList<AutoCompleteEntry> autocompletes = new LinkedList<>();

	protected static SystemType VOID;
	protected static SystemType UNKNOWN;
	protected static SystemType INTEGER;
	protected static SystemType REAL;
	protected static SystemType BOOLEAN;
	protected static SystemType STRING;
	protected static SystemType FILLSTYLE;
	protected static SystemType LINESTYLE;
	protected static SystemType COLOR;
	protected static SystemType ALIGNMENT;

	protected Type(String id) {
		this.id = id;
	}

	public boolean checkType(Type right) {
		return !mergeTypes(right).isUnknown();
	}

	public boolean isNumber() {
		return this == INTEGER || this == REAL;
	}

	public boolean isBoolean() {
		return this == BOOLEAN;
	}

	public boolean isVoid() {
		return this == VOID;
	}

	public boolean isInteger() {
		return this == INTEGER;
	}

	@Override
	public boolean isUnknown() {
		return this == UNKNOWN;
	}

	public Type mergeTypes(Type right) {
		if (this == right) {
			return this;
		} else if (right != null) {
			if (this == INTEGER && right == REAL || this == REAL && right == INTEGER) {
				return REAL;
			} else {
				return mergeTypes(right.superType);
			}
		} else {
			return UNKNOWN;
		}
	}

	public static SystemType getVoid() {
		return VOID;
	}

	public static SystemType getUnknown() {
		return UNKNOWN;
	}

	public static SystemType getInteger() {
		return INTEGER;
	}

	public static SystemType getBoolean() {
		return BOOLEAN;
	}

	public static SystemType getDouble() {
		return REAL;
	}

	public static SystemType getString() {
		return STRING;
	}

	public static SystemType getColor() {
		return COLOR;
	}

	public static SystemType getLineStyle() {
		return LINESTYLE;
	}

	public static SystemType getFillStyle() {
		return FILLSTYLE;
	}

	public static SystemType getAlignment() {
		return ALIGNMENT;
	}

	public void registerAutoCompleteEntry(AutoCompleteEntry ace) {
		autocompletes.add(ace);
	}

	public void registerMethod(MethodType mt) {
		LinkedList<MethodType> list;
		String key = Signatures.createPreselectionSignature(mt.getName(), mt.getParameters().length);
		if (methods.containsKey(key)) {
			list = methods.get(key);
		} else {
			list = new LinkedList<>();
			methods.put(key, list);
		}
		list.add(mt);
	}

	/**
	 * Liefert die Methoden zum zur Signature.
	 * Die Signature ist der Name gefolgt von der Parameterzahl in Klammern
	 * ZB verschieben(2)
	 * @param name Name in lokaler Sprache
	 * @param parameters Parametertypen
	 * @return
	 */
	public MethodType getMethod(String name, Type[] parameters) {
		LinkedList<MethodType> list = getMethods(name, parameters.length);
		for(MethodType mt:list) {
			if(mt.checkType(parameters)) {
				return mt;
			}
		}
		return null;
	}
	private LinkedList<MethodType> getMethods(String name, int parameterCount) {
		String key = Signatures.createPreselectionSignature(name, parameterCount);
		
		LinkedList<MethodType> list = new LinkedList<>();
		if (methods.containsKey(key)) {
			list.addAll(methods.get(key));
		}
		if (superType != null) {
			list.addAll(superType.getMethods(name, parameterCount));
		}
		return list;
	} 
	
	
	public boolean inherits(Type b) {
		if (b == this) {
			return true;
		} else if (superType != null) {
			return superType.inherits(b);
		} else {
			return false;
		}
	}

	public LinkedList<Type> getTypeList() {
		LinkedList<Type> typelist = new LinkedList<Type>();
		Type act = this;
		while (act != null) {
			typelist.add(act);
			act = act.superType;
		}
		return typelist;
	}

	@Override
	public LinkedList<AutoCompleteInformation> getAutoCompletes() {
		LinkedList<AutoCompleteInformation> completes = new LinkedList<>();
		fillAutoCompletes(completes);
		return completes;
	}

	protected void fillAutoCompletes(LinkedList<AutoCompleteInformation> list) {
		list.addAll(autocompletes);
		if (superType != null) {
			superType.fillAutoCompletes(list);
		}
	}

	@Override
	public int compareTo(Type b) {
		return this.id.compareTo(b.id);
	}

	@Override
	public String toString() {
		return id;
	}

	public String getID() {
		return id;
	}

	public Type getSuperType() {
		return superType;
	}

	public abstract MType getMType();

	public abstract boolean isAbstract();

	public abstract MethodType getReadProperty(String name);

	public abstract MethodType getAssignProperty(String name);

	public abstract TreeMap<String, MethodType> getAllReadProperties();

	public abstract TreeMap<String, MethodType> getAllAssignProperties();
	
	public abstract LinkedList<PropertyViewer> getPropertyViewers(Object target);	
}
