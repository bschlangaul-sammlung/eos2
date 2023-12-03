package de.lathanda.eos.vm;

import java.util.ArrayList;

public class MProcedure {
	private final Command[] ops;
	private final boolean globalAccess;

	public MProcedure(ArrayList<Command> ops, boolean globalAccess) {
		this.ops = new Command[ops.size()];
		ops.toArray(this.ops);
		this.globalAccess = globalAccess;
	}

	public MProcedure(Command[] ops, boolean globalAccess) {
		this.ops = ops;
		this.globalAccess = globalAccess;
	}

	public Command[] getOps() {
		return ops;
	}

	public boolean getGlobalAccess() {
		return globalAccess;
	}

	public void prepare(Machine m) {
		for (Command command : ops) {
			command.prepare(m);
		}
	}

}
