package de.lathanda.eos.interpreter.parser.en;

import java.util.LinkedList;

import de.lathanda.eos.vm.Marker;

/**
 * Diese Klasse verpackt ein Template, da javacc 
 * die Templatenotation nicht versteht.
 * <> erzeugen einen Fehler. 
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class MarkerStack extends LinkedList<Marker> {
    private static final long serialVersionUID = -4276688126529265580L;
}
