package de.lathanda.eos.vm;

import de.lathanda.eos.base.event.CleanupListener;
import de.lathanda.eos.vm.exceptions.ConstantWriteAccessViolation;
import de.lathanda.eos.vm.exceptions.MemoryAccessViolation;
import de.lathanda.eos.vm.exceptions.ProgramTerminationException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Virtuelle Maschine zur Simulation eines Stack basierenden Assemblers.
 *
 * @author Peter (Lathanda) Schneider
 * 
 */
public class Machine implements AbstractMachine {

	private final LinkedList<Object> stack;
	private final LinkedList<Context> callstack;
	/**
	 * Aktiver Kontext (Aktueller Bereich)
	 */
	private Context context;
	/**
	 * Globaler Kontext. Außerhalb von Funtionen ist dieser mit dem aktuellen Kontext identisch.
	 */
	private Context global;
	private TreeMap<String, Variable> constants = new TreeMap<>();
	private final TreeMap<String, MProcedure> userfunction;
	private final Executer executer = new Executer();
	private final SpeedExecuter speedexecuter = new SpeedExecuter();
	private DebugInfo debugInfo = new DebugInfo();
	private volatile boolean executing = false;
	private volatile boolean running = false;
	private volatile boolean isStarting = true;
	private volatile boolean isFinished = false;
	private volatile long delay = 5000;
	private final DebugMulticaster dmc = new DebugMulticaster();

	private final LinkedList<CleanupListener> gc = new LinkedList<>();
	private TreeMap<Integer, DebugPoint> breakpoints = new TreeMap<>();
	
	LinkedList<MachineListener> machineListeners = new LinkedList<>();
	
	public Machine() {
		stack = new LinkedList<>();
		callstack = new LinkedList<>();
		context = new Context();
		global = context;
		userfunction = new TreeMap<>();
	}
	public void addMachineListener(MachineListener m) {
		machineListeners.add(m);
	}
	public void removeMachineListener(MachineListener m) {
		machineListeners.remove(m);
	}

	public void addDebugListener(DebugListener dl) {
		dmc.add(dl);
	}

	public void removeDebugListener(DebugListener dl) {
		dmc.remove(dl);
	}

	public void setBreakpoint(int linenumber, boolean active) {
		DebugPoint debug = getDebugPoint(linenumber);
		if (debug != null) {
			debug.setActiveBreakpoint(active);
		}
	}

	public void addPossibleBreakpoint(DebugPoint debugPoint, int linenumber) {
		if (!breakpoints.containsKey(linenumber)) {
			breakpoints.put(linenumber, debugPoint);
		}
	}

	public boolean hasBreakpoint(int linenumber) {
		DebugPoint debug = getDebugPoint(linenumber);
		if (debug != null) {
			return debug.isActiveBreakpoint();
		} else {
			return false;
		}
	}

	@Override
	public int getBreakpointPosition(int linenumber) {
		DebugPoint dp = getDebugPoint(linenumber);
		if (dp != null) {
			return dp.getMarker().getBeginPosition();
		} else {
			return -1;
		}
	}

	private DebugPoint getDebugPoint(int linenumber) {
		if (breakpoints.containsKey(linenumber)) {
			return breakpoints.get(linenumber);
		} else {
			Map.Entry<Integer, DebugPoint> entry = breakpoints.higherEntry(linenumber);
			if (entry == null) {
				return null;
			} else {
				return entry.getValue();
			}
		}
	}

	public void reinit() {
		userfunction.clear();
		global = new Context();
		reset();
	}

	private void reset() {
		isFinished = false;
		// clear runtime informations
		stack.clear();
		// clear constant
		constants.clear();
		// clear memory
		callstack.clear();
		// restore starting memory
		context = global;
		global.reset();
		// create new debug container
		debugInfo = new DebugInfo();
		// destroy all windows
		gc.forEach(w -> w.terminate());
		gc.clear();
		// inform debug listeners
		dmc.fireDebugPoint(debugInfo);
	}

	public Object pop() {
		if (stack.isEmpty()) {
			throw new ProgramTerminationException();
		}
		return stack.pop();
	}

	public void push(Object obj) {
		stack.push(obj);
	}

	public Object peek() {
		return stack.peek();
	}

	public Object get(String variable) {
		if (context.memory.containsKey(variable)) {
			return context.memory.get(variable).get();
		} else if (context.globalAccess && global.memory.containsKey(variable)) {
			return global.memory.get(variable).get();
		} else {
			if (constants.containsKey(variable)) {
				return constants.get(variable);
			} else {
				throw new MemoryAccessViolation(variable);
			}
		}
	}

	public void set(String variable, Object data) {
		if (context.memory.containsKey(variable)) {
			context.memory.get(variable).set(data);
		} else if (context.globalAccess && global.memory.containsKey(variable)) {
			global.memory.get(variable).set(data);
		} else {
			if (constants.containsKey(variable)) {
				throw new ConstantWriteAccessViolation(variable);				
			} else {
				throw new MemoryAccessViolation(variable);
			}
		}
	}

	public void createConstant(String name, MType mt, Object value) {
		Variable v = new Variable(mt, name);
		v.set(value);
		constants.put(name, v);
		
	}
	public LinkedList<MemoryEntry> getMemory() {
		LinkedList<MemoryEntry> result = new LinkedList<>();
		for (Entry<String, Variable> entry : global.memory.entrySet()) {
			Variable v = entry.getValue();
			result.add(new MemoryEntry(entry.getKey(), v.get(), v.type.getID()));
		}
		if (context != global) {
			for (Entry<String, Variable> entry : context.memory.entrySet()) {
				Variable v = entry.getValue();
				result.add(new MemoryEntry(entry.getKey(), v.get(), v.type.getID()));
			}
		}
		return result;
	}

	public void jump(int relativ) {
		context.index += relativ;
	}

	public void create(String variable, MType type) throws Exception {
		Variable v = createInitVariable(variable, type);
		context.memory.put(variable, v);
	}

	public void define(String variable, MType type) throws Exception {
		Variable v = new Variable(type, variable);
		context.memory.put(variable, v);
	}

	public Variable createInitVariable(String name, MType type) throws Exception {
		Variable v = new Variable(type, name);
		if (!type.isAbstract()) {
			v.set(type.newInstance(this));
			if (v.get() instanceof CleanupListener) {
				gc.add((CleanupListener) v.get());
			}
		}
		return v;
	}

	public void create(String variable, MType type, Object data) throws Exception {
		Variable v = new Variable(type, variable);
		v.set(data);
		context.memory.put(variable, v);
	}

	private boolean microStep() throws Exception {
		Context actcontext = context;
		try {
			if (actcontext.program[actcontext.index].execute(this)) {
				actcontext.index++;
			}
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			machineListeners.forEach((ml)->ml.missingProgram());
		} catch (ProgramTerminationException pte) {
			machineListeners.forEach((ml)->ml.handleError(pte));
			stop();
			return true;
		} catch (Exception e) {
			machineListeners.forEach((ml)->ml.handleOrThrowException(e));
			actcontext.index++;
		}
		return update();
	}

	private boolean update() {
		// remark - operations can create a sub context
		while (context.program.length <= context.index) {
			if (callstack.size() == 0) {
				isFinished = true;
				messageFinishing();
				return true;
			} else {
				context = callstack.pop();
			}
		}
		return false;
	}

	/**
	 * called by interpreter if debug point is reached
	 * @param codeRange 
	 */
	public void debugStop(Marker codeRange) {
		executing = false;
		debugInfo.setCodeRange(codeRange);
		dmc.fireDebugPoint(debugInfo);
	}

	/**
	 * Returns actual debugInfo
	 * @return
	 */
	public DebugInfo getDebugInfo() {
		return debugInfo;
	}

	/**
	 * executes a single statement/expression
	 */
	public synchronized void singleStep() {
		if (!check())
			return;
		// stop continues execution
		pause();
		// execute a single step
		try {
			messageStarting();
			debugStep();
		} catch (Exception e) {
			machineListeners.forEach((ml)->ml.handleException(e));
		}
	}

	private void messageStarting() {
		if (isStarting) {
			machineListeners.forEach((ml)->ml.startProgram());
			isStarting = false;
		}
	}

	private void messageFinishing() {
		if (!isStarting) {
			machineListeners.forEach((ml)->ml.stopProgram());
			isStarting = true;
		}
	}

	public synchronized void skip() {
		if (!check())
			return;
		pause();
		new Thread(speedexecuter).start();
	}

	private void debugStep() throws Exception {
		if (isFinished) {
			reset();
		}
		executing = true;
		running = true;
		while (executing && running) {
			if (microStep()) {
				running = false;
			}
		}
	}

	@Override
	public void setDelay(long delay) {
		this.delay = delay;
	}

	@Override
	public synchronized void stop() {
		messageFinishing();
		// stop continues execution
		pause();
		// set machine back to start
		reset();
	}

	/**
	 * Überprüft ob es ein gültiges Programm gibt.
	 * @return
	 */
	private boolean check() {
		return context.program != null;
	}

	@Override
	public boolean isStarting() {
		return isStarting;
	}

	@Override
	public void run() {
		if (!check())
			return;
		pause();
		new Thread(executer).start();
	}

	@Override
	public synchronized void pause() {
		if (running) {
			running = false;
			this.notifyAll();
		}
	}

	public void call(String signature) {
		MProcedure uf = userfunction.get(signature);
		call(uf);
	}

	public void call(MProcedure proc) {
		callstack.push(context);
		context = new Context();
		context.program = proc.getOps();
		context.globalAccess = proc.getGlobalAccess();
		context.index = 0;
	}

	public void addUserFunction(String signature, MProcedure proc) {
		proc.prepare(this);
		userfunction.put(signature, proc);
	}

	public void setProgram(ArrayList<Command> ops) {
		for (Command command : ops) {
			command.prepare(this);
		}
		context.program = new Command[ops.size()];
		context.program = ops.toArray(context.program);
		context.index = 0;
	}

	private class Context {

		public int index = 0;
		public Command[] program;
		public boolean globalAccess;
		public TreeMap<String, Variable> memory = new TreeMap<>();

		public void reset() {
			index = 0;
			memory.clear();
		}
	}

	private class Executer implements Runnable {
		@Override
		public void run() {
			long laststep = System.nanoTime();
			messageStarting();
			running = true;
			try {
				while (running) {
					int diff = (int) (laststep + delay - System.nanoTime());
					if (diff > 1000000) {
						synchronized (Machine.this) {
							Machine.this.wait(diff / 1000000);
						}
					}

					laststep += delay;
					synchronized (Machine.this) {
						if (running) {
							debugStep();
						}
					}

				}
			} catch (ProgramTerminationException pte) {
				// thread was killed or execution ran into an error
				// either way generate message and terminate execution
				machineListeners.forEach((ml)->ml.handleError(pte));
				stop();
			} catch (Exception e) {
				// thread was killed or execution ran into an error
				// either way generate message and terminate execution
				machineListeners.forEach((ml)->ml.handleException(e));
				stop();
			}
		}
	}

	private class SpeedExecuter implements Runnable {
		@Override
		public void run() {
			messageStarting();
			running = true;
			try {
				while (running) {
					synchronized (Machine.this) {
						if (running) {
							debugStep();
						}
					}
				}
			} catch (ProgramTerminationException pte) {
				// thread was killed or execution ran into an error
				// either way generate message and terminate execution
				machineListeners.forEach((ml)->ml.handleError(pte));
				stop();
			} catch (Exception e) {
				// thread was killed or execution ran into an error
				// either way generate message and terminate execution
				machineListeners.forEach((ml)->ml.handleException(e));
				stop();
			}
		}
	}

}
