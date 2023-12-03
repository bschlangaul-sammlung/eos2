package de.lathanda.eos.parser.core;

import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.Marker;
import de.lathanda.eos.vm.ProgramNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Speichert und behandelt Anweisungssequenzen.
 *
 * @author Peter (Lathanda) Schneider
 *
 */
public class Sequence extends Node implements ProgramSequence {

	private final ArrayList<Node> sequence;
	int index = 0;

	public Sequence() {
		this.sequence = new ArrayList<>();
	}

	public Sequence(List<Node> sequence) {
		this.sequence = new ArrayList<>();
		this.sequence.addAll(sequence);
	}

	public void append(Sequence s) {
		for (Node n : s.sequence) {
			append(n);
		}
	}

	public void append(Node s) {
		sequence.add(s);
		if (marker == null) {
			marker = new Marker(s, this);
		} else {
			marker.extend(s.getMarker());
		}
	}

	@Override
	public ArrayList<ProgramNode> getInstructions() {
		ArrayList<ProgramNode> temp = new ArrayList<ProgramNode>(sequence.size());
		sequence.forEach(i -> temp.add(i));
		return temp;
	}

	@Override
	public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
		for (Node instruction : sequence) {
			if (instruction.getType().isVoid()) {
				// none void instruction would corrupt the parameter stack
				// we can choose between adding an artificial consumer or to not
				// compile the instruction
				// as this type of instruction has no effect, we do not compile
				// it
				instruction.compile(ops, autoWindow);
			}
		}
	}

	@Override
	public void resolveNamesAndTypes(Expression with, Environment env) {
		sequence.stream().forEachOrdered((instruction) -> {
			instruction.resolveNamesAndTypes(with, env);
			if (!instruction.getType().isVoid()) {
				env.addError(instruction.getMarker(), "StatementWithReturn", instruction);
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (MarkedNode n : sequence) {
			res.append(n).append("\n");
		}
		return res.toString();
	}

	@Override
	public String getLabel() {
		return null; // no label
	}
}
