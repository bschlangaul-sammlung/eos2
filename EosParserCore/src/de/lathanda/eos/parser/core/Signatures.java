package de.lathanda.eos.parser.core;


import de.lathanda.eos.vm.MType;

public class Signatures {
	public static String createPreselectionSignature(String name, int args ) {
		return name+"#"+args;
	}
	public static String createUserFunctionSignature(String name, Type[] parameters) {
		StringBuilder signature = new StringBuilder();
		signature.append(name);
		for(int i = 0; i < parameters.length; i++) {
			signature.append("#");
			signature.append(parameters[i].getID());
		}		
		return signature.toString();
	}
	public static String createUserMethodSignature(String name, Type[] parameters) {
		StringBuilder signature = new StringBuilder();
		signature.append(name);
		for(int i = 0; i < parameters.length; i++) {
			signature.append("#");
			signature.append(parameters[i].getID());
		}		
		return signature.toString();
	}
	public static String createVMMethodSignature(MethodType m) {
		StringBuilder signature = new StringBuilder();
		signature.append(m.getName());
		MType[] p = m.getParameters();
		for(int i = 0; i < p.length; i++) {
			signature.append("#");
			signature.append(p[i].getID());
		}		
		return signature.toString();
	}
}
