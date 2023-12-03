package de.lathanda.eos.vm;

import java.util.Map.Entry;

import de.lathanda.eos.vm.exceptions.TypeMissMatchException;

import java.util.TreeMap;

/**
 * Informationen über eine Benutzerdefinierte Klasse
 * @author Peter (Lathanda) Schneider
 *
 */
public class MClass implements MType {
	private String id;
	private TreeMap<String, MProcedure> methods;
	private TreeMap<String, MType> properties;
	private MType sup;
	private boolean isAbstract;

	public MClass(String name) {
		this(name, MJavaClass.BASE);
	}

	public MClass(String id, MType sup) {
		methods = new TreeMap<>();
		properties = new TreeMap<>();
		this.sup = sup;
		this.id = id;
		isAbstract = false;
	}

	public void addMethod(String signature, MProcedure m) {
		methods.put(signature, m);
	}

	public MProcedure getMethod(String signature) {
		return methods.get(signature.toString());
	}

	public void addProperty(String signature, MType t) {
		properties.put(signature, t);
	}

	@Override
	public Object checkAndCast(Object obj) {
		if (obj instanceof MObject) {
			MType objType = ((MObject) obj).getType();
			while (objType instanceof MClass) {
				if (objType == this) {
					return obj;
				}
				objType = ((MClass) objType).sup;
			}
			throw new TypeMissMatchException(id, ((MObject) obj).getType().getID());
		} else if (sup != null) {
			return sup.checkAndCast(obj);
		} else {
			if (obj instanceof MObject) {
				throw new TypeMissMatchException(id, ((MObject) obj).getType().getID());
			} else {
				throw new TypeMissMatchException(id, obj.getClass().toString());
			}
		}
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	@Override
	public Object newInstance(Machine m) throws Exception {
		return new MObject(this, m);
	}

	@Override
	public TreeMap<String, Variable> createProperties(Machine m) throws Exception {
		TreeMap<String, Variable> prop = sup.createProperties(m);
		for (Entry<String, MType> p : properties.entrySet()) {
			String s = p.getKey();
			prop.put(s, m.createInitVariable(p.getKey(), p.getValue()));
		}
		return prop;
	}

	public Object createJavaObject(Machine m) throws Exception {
		return sup.createJavaObject(m);
	}

	public void setSuper(MType sup) {
		this.sup = sup;
	}
}
