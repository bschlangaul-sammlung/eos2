package de.lathanda.eos.parser.core;

import de.lathanda.eos.vm.MJavaClass;
import de.lathanda.eos.vm.MType;
import de.lathanda.eos.vm.MissingTypeException;
import de.lathanda.eos.vm.ObjectSource;

import java.util.LinkedList;
import java.util.TreeMap;


/**
 * Typdefinition.
 * Diese Klasse enthält alle Informationen zu den Eos Typen. 
 *
 * @author Peter (Lathanda) Schneider
 */
public class SystemType extends Type implements Comparable<Type>, AutoCompleteType {
	protected final TreeMap<String, MethodType> getProperty  = new TreeMap<>();
	protected final TreeMap<String, MethodType> setProperty  = new TreeMap<>();
	protected final TreeMap<String, java.lang.reflect.Method> viewProperty = new TreeMap<>();
	// type maps
	private static final TreeMap<String, SystemType> nameType = new TreeMap<>();
	private static final TreeMap<String, SystemType> idType = new TreeMap<>();
	protected static SystemType CLASS;
	// special types
	private static SystemType WINDOW;
	private static SystemMethodType WINDOW_ADD_FIGURE;
	private static SystemType FIGURE;

	private final ObjectSource objSrc;
	private final Class<?> cls;

	private final String name;
	private SystemType(String id, String name, ObjectSource objSrc, Class<?> cls) {
		super(id);
		this.objSrc = objSrc;
		this.cls = cls;
		this.name = name;
	}

	public static SystemType registerType(String id, String[] names, ObjectSource objSrc, String clsname) throws ClassNotFoundException {
		SystemType type;
		Class<?> cls = switch (clsname) {
			case "?", "class", "#"  -> null;
			case "boolean" -> boolean.class;
			case "double" -> double.class;
			case "int" -> int.class;
			default -> Class.forName(clsname);
		};
		if (names == null || names.length == 0) {
			//interal type
			type = new SystemType(id, id, objSrc, cls);			
		} else {
			type = new SystemType(id, names[0], objSrc, cls);
			for (String name : names) {
				nameType.put(name.trim().toLowerCase(), type);
			}
		}
		idType.put(id, type);
		switch (id) {
		case "integer":
			INTEGER = type;
			break;
		case "?":
			UNKNOWN = type;
			break;
		case "class":
			CLASS = type;
			break;
		case "#":
			VOID = type;
			break;
		case "boolean":
			BOOLEAN = type;
			break;
		case "real":
			REAL = type;
			break;
		case "string":
			STRING = type;
			break;
		case "color":
			COLOR = type;
			break;
		case "linestyle":
			LINESTYLE = type;
			break;
		case "fillstyle":
			FILLSTYLE = type;
			break;
		case "alignment":
			ALIGNMENT = type;
			break;
		case "window":
			WINDOW = type;
			try {
				WINDOW_ADD_FIGURE = new SystemMethodType("1af", WINDOW, new SystemType[] { FIGURE }, VOID, "addFigure", "");
				type.registerMethod(getAddFigureMethod());
			} catch (NoSuchMethodException e) { e.printStackTrace(); System.exit(-1);}
			break;
		case "figure":
			FIGURE = type;
			break;
		}
		return type;
	}

	public static void registerSuper(String id, String superType) throws MissingTypeException {
		Type t = idType.get(id);
		if (t == null) {
			throw new MissingTypeException(id);
		}
		if (superType != null) {
			Type st = idType.get(superType);
			if (st == null) {
				throw new MissingTypeException(superType);
			}
			t.superType = st;
		} else {
			t.superType = null;
		}
	}

	// basic types
	public static SystemType getFigure() {
		return FIGURE;
	}

	public static SystemType getWindow() {
		return WINDOW;
	}

	public static SystemType getClassType() {
		return CLASS;
	}

	public static SystemType getInstanceByID(String id) {
		if (idType.containsKey(id)) {
			return idType.get(id);
		} else {
			return UNKNOWN;
		}
	}

	public static SystemType getInstanceByName(String name) {
		if (nameType.containsKey(name.toLowerCase())) {
			return nameType.get(name.toLowerCase());
		} else {
			return UNKNOWN;
		}
	}

	public static LinkedList<Type> getAll() {
		LinkedList<Type> allTypes = new LinkedList<>();
		for (Type t : nameType.values()) {
			allTypes.add(t);
		}
		return allTypes;
	}

	/**
	 * Liefert die Methode, um ein Attribut zu setzen.
	 * @param name Attributname
	 * @return
	 */
	public MethodType getAssignProperty(String name) {
		String key = name.toLowerCase();
		if (setProperty.containsKey(key)) {
			return setProperty.get(key);
		} else if (superType != null) {
			return superType.getAssignProperty(name);
		} else {
			return null;
		}
	}

	/**
	 * Liefert die Methode, um ein Attribut zu lesen.
	 * @param name Attributname
	 * @return
	 */
	public MethodType getReadProperty(String name) {
		String key = name.toLowerCase();
		if (getProperty.containsKey(key)) {
			return getProperty.get(key);
		} else if (superType != null) {
			return superType.getReadProperty(name);
		} else {
			return null;
		}
	}
	@Override
	public LinkedList<PropertyViewer> getPropertyViewers(Object target) {
		LinkedList<PropertyViewer> list = new LinkedList<>();
		Type act = this; 
		while (act != null) {
			for(var m:viewProperty.entrySet()) {
				list.add(new PropertyViewer.SystemTypePropertyViewer(m.getKey(), m.getValue(), target));
			}
			act = act.superType;
		}
		return list;
	}
	@Override
	public TreeMap<String, MethodType> getAllReadProperties() {
		return getProperty;
	}

	@Override
	public TreeMap<String, MethodType> getAllAssignProperties() {
		return setProperty;
	}

	public void registerReadProperty(MethodType mt, String name) {
		getProperty.put(name, mt);
	}
	public void registerViewProperty(String viewingMethod, String label) throws NoSuchMethodException {
		java.lang.reflect.Method m = cls.getMethod(viewingMethod, new Class<?>[] {});
		viewProperty.put(label, m);		
	}
	public void registerAssignProperty(SystemMethodType mt, String name) {
		setProperty.put(name, mt);
	}

	public Class<?> convertToClass() {
		return cls;
	}

	public boolean isAbstract() {
		return objSrc == null;
	}

	public MType getMType() {
		return new MJavaClass(id, cls, objSrc);
	}

	public static MethodType getAddFigureMethod() {
		return WINDOW_ADD_FIGURE;
	}

	@Override
	public String toString() {
		return name;
	}


}
