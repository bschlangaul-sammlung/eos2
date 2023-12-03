package de.lathanda.eos.interpreter.commands;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MType;
import de.lathanda.eos.interpreter.Machine;

/**
 * 
 * Assembler Befehl: Variable anlegen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class DefineVariable extends Command {
    private final String name;
    private final MType type;

    public DefineVariable(String name, MType type) {
        this.name = name;
        this.type  = type;
    }
    
    @Override
    public boolean execute(Machine m) throws Exception {
        m.define(name, type);
        return true;
    }

    @Override
    public String toString() {
        return "ReserveMemory{" + name + ":" + type + '}';
    }
    
}
