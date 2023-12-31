package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;

import de.lathanda.eos.gui.diagram.LoopForeverUnit;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.commands.DebugPoint;
import de.lathanda.eos.interpreter.commands.Jump;

/**
 * Speichert und behandelt eine Endlosschleife.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class RepeatForever extends Loop implements LoopForeverUnit {
    public RepeatForever(Sequence sequence) {
        super(sequence, null);
    }  

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        ArrayList<Command> body =  new ArrayList<>();
        sequence.compile(body, autoWindow);
        ops.addAll(body);
        ops.add(new DebugPoint());        
        ops.add(new Jump(-(1+body.size())));
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        sequence.resolveNamesAndTypes(with, env);
    }
    @Override
    public String toString() {
        return "repeat forever\n" + sequence + "endrepeat";
    }    
    @Override
    public String getLabel() {
        return createText("RepeatForever.Label");
    }

    @Override
    public boolean isPre() {
        return true;
    }    
}
