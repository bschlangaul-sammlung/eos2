package de.lathanda.eos.interpreter.parsetree;

import java.util.LinkedList;
import java.util.List;

import de.lathanda.eos.common.interpreter.Marker;

/**
 * Speichert und behandelt eine Parameterliste.
 * 
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Parameters {
    private final LinkedList<Parameter> parameters;
    private final LinkedList<String> nameBuffer;
    public Parameters() {
        parameters = new LinkedList<>();
        nameBuffer = new LinkedList<>();
    }
    public void append(Parameter parameter) {
        parameters.add(parameter);
    }
    public int size() {
        return parameters.size();
    }
    public List<Parameter> getParameters() {
        return parameters;
    }
    public Type[] getTypes() {
        Type[] para = new Type[parameters.size()];
        for(int i = 0; i < para.length; i++) {
            para[i] = parameters.get(i).getType();
        }        
        return para;
    }

    public void registerParameters(Environment env) {
        for (Parameter p : parameters) {
            p.registerParameter(env);
        }
    }

    public void addName(String name) {
        nameBuffer.add(name);
    }

    public void setType(Type type, Marker marker) {
        nameBuffer.stream().forEachOrdered(name -> append(new Parameter(name, type, marker)));
        nameBuffer.clear();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        res.append("(");
        for (Parameter p : parameters) {
            if (first) {
                res.append(p);
            } else {
                res.append(",").append(p);
                first = false;
            }
        }
        res.append(")");
        return res.toString();
    }    
}
