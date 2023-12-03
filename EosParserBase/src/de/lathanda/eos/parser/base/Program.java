package de.lathanda.eos.parser.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

import de.lathanda.eos.parser.core.AbstractProgram;
import de.lathanda.eos.parser.core.BasicParser;
import de.lathanda.eos.parser.core.Environment;
import de.lathanda.eos.parser.core.InfoToken;
import de.lathanda.eos.parser.core.MarkedNode;
import de.lathanda.eos.parser.core.ParserFactory;
import de.lathanda.eos.parser.core.PredefinedVariable;
import de.lathanda.eos.parser.core.ProgramSequence;
import de.lathanda.eos.parser.core.ProgramUnit;
import de.lathanda.eos.parser.core.Sequence;
import de.lathanda.eos.parser.core.Type;
import de.lathanda.eos.parser.core.exceptions.TranslationException;
import de.lathanda.eos.vm.AbstractMachine;
import de.lathanda.eos.vm.Command;
import de.lathanda.eos.vm.DebugPoint;
import de.lathanda.eos.vm.ErrorInformation;
import de.lathanda.eos.vm.MProcedure;
import de.lathanda.eos.vm.Machine;
import de.lathanda.eos.vm.Marker;
import de.lathanda.eos.vm.ReservedVariables;
import de.lathanda.eos.vm.commands.CreateVariable;

/**
 * Speichert und behandelt den Syntaxbaum des Programms.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Program implements AbstractProgram {
	private final ParserFactory parserFactory;
	private final String defaultWindowName;
	private final boolean lockProperties;
	private final Collection<PredefinedVariable> predefinedVariables;
	private final static TreeMap<String, Type> guessTable = new TreeMap<>();
	private final Sequence program;
	private final LinkedList<SubRoutine> sub;
	private final TreeMap<String, UserClass> userclass;
	private final LinkedList<MarkedNode> nodeList;
	private final LinkedList<InfoToken> tokenList;
	private final Environment env;
	private final String source;
	private final PrettyPrinter prettyPrinter;
	private final Machine machine;
	private BasicParser parser;
	public Program(Program org) {
		this(org.parserFactory, org.defaultWindowName, org.lockProperties, org.predefinedVariables);
	}
	public Program(ParserFactory parserFactory, String defaultWindowName, boolean lockProperties, Collection<PredefinedVariable> prevar) {
		this.defaultWindowName = defaultWindowName;
		this.lockProperties = lockProperties;
		this.predefinedVariables = prevar;
		this.parserFactory = parserFactory;
		this.machine = new Machine();
		this.program = new Sequence();
		this.prettyPrinter = new PrettyPrinter("");
		this.source = "";
		sub = new LinkedList<>();
		userclass = new TreeMap<>();
		env = new Environment(this, defaultWindowName, lockProperties, prevar);
		nodeList = new LinkedList<>();
		tokenList = new LinkedList<>();
	}

	public Program(String source, ParserFactory parserFactory, String defaultWindowName, boolean lockProperties, Collection<PredefinedVariable> prevar) {
		this.defaultWindowName = defaultWindowName;
		this.lockProperties = lockProperties;
		this.predefinedVariables = prevar;
		this.parserFactory = parserFactory;
		this.machine = new Machine();
		this.program = new Sequence();
		this.prettyPrinter = new PrettyPrinter(source);
		this.source = source;
		sub = new LinkedList<>();
		userclass = new TreeMap<>();
		env = new Environment(this, defaultWindowName, lockProperties, prevar);
		nodeList = new LinkedList<>();
		tokenList = new LinkedList<>();
	}

	@Override
	public synchronized void parse(String path) throws TranslationException {
		parser = parserFactory.createParser(source);
		parser.parse(this, path);
	}

	public void add(Sequence s) {
		program.append(s);
	}

	public void add(SubRoutine s) {
		sub.add(s);
	}

	public void add(UserClass u) {
		userclass.put(u.getName(), u);
	}

	public void addNode(MarkedNode node) {
		nodeList.add(node);
	}

	public void addToken(SourceToken sourceToken) {
		tokenList.add(sourceToken);
	}

	public LinkedList<MarkedNode> getNodeList() {
		return nodeList;
	}

	@Override
	public LinkedList<InfoToken> getTokenList() {
		return tokenList;
	}

	@Override
	public ProgramSequence getProgram() {
		return program;
	}

	public void merge(Program subprogram, Marker marker) {
		// override all debugging markers
		for (MarkedNode n : subprogram.nodeList) {
			n.setMarker(marker);
		}
		this.program.append(subprogram.program);
		this.sub.addAll(subprogram.sub);
		this.userclass.putAll(subprogram.userclass);
		this.nodeList.addAll(subprogram.nodeList);
	}

	@Override
	public LinkedList<ProgramUnit> getSubPrograms() {
		LinkedList<ProgramUnit> temp = new LinkedList<>();
		sub.forEach(s -> temp.add(s));
		return temp;
	}

	/**
	 * Wandelt den Syntaxbaum in ein ausführbares Programm dieser Maschine um. 
	 * @param m In diese Maschcine wird das Programm geschrieben.
	 * @throws Exception
	 */
	public void compile(Machine m) throws Exception {
		env.resetAll();
		// first scan, resolve types
		for (UserClass uc : userclass.values()) { // prepare user types
			uc.bind(env);
		}
		for (UserClass uc : userclass.values()) { // check for storage cycles
			uc.checkCyclicStorage();
		}

		for (SubRoutine s : sub) { // register all sub routines
			s.registerSub(env);
		}

		for (UserClass uc : userclass.values()) { // finish user types
			uc.resolveNamesAndTypes(env);
		}

		program.resolveNamesAndTypes(null, env);

		env.storeVariables();
		for (SubRoutine s : sub) {
			if (s.getGlobalAccess()) {
				env.restoreVariables();
			} else {
				env.resetVariables();
			}
			s.resolveNamesAndTypes(null, env);
		}
		env.restoreVariables();
		// second scan produce commands
		for (UserClass uc : userclass.values()) { // compile user types
			uc.compile();
		}
		ArrayList<Command> ops = new ArrayList<>();
		if (env.getAutoWindow()) {
			ops.add(new CreateVariable(ReservedVariables.WINDOW, SystemType.getWindow().getMType()));
		}
		program.compile(ops, env.getAutoWindow());
		ops.add(new DebugPoint());
		m.setProgram(ops);
		for (SubRoutine s : sub) {
			ops = new ArrayList<>();
			// automatic add to window only works with user function that have
			// global access to the window object
			// methods can, procedures cannot.
			s.compile(ops, s.getGlobalAccess() && env.getAutoWindow());
			m.addUserFunction(Signatures.createUserFunctionSignature(s.getLabel(), s.getParameterTypes()), new MProcedure(ops, s.getGlobalAccess()));
		}
	}

	@Override
	public LinkedList<ErrorInformation> getErrors() {
		return env.getErrors();
	}

	@Override
	public AutoCompleteType seekType(int position) {
		Type type;
		type = seekTypeSemantical(position);
		if (type == null || type.isUnknown() || type.isVoid()) {
			type = useBestGuess(position);
		}
		return type;
	}

	private Type seekTypeSemantical(int position) {
		MarkedNode target = null;
		for (MarkedNode node : nodeList) {
			if (node.getMarker().getEndPosition() < position) {
				if (node instanceof Expression && node.getMarker().getEndPosition() == position - 1) {
					target = node;
					break;
				}
			} else {
				break;
			}
		}
		if (target == null) {
			return Type.getUnknown();
		} else {
			return target.getType();
		}
	}

	private Type useBestGuess(int position) {
		int start = 0;
		for (int i = Math.min(position, source.length()); i-- > 0;) {
			if (Character.isWhitespace(source.charAt(i))) {
				start = i + 1;
				break;
			}
		}
		String name = source.substring(start, position).trim();
		if (guessTable.containsKey(name)) {
			return guessTable.get(name);
		} else {
			return Type.getUnknown();
		}
	}

	public static void addGuess(String name, Type type) {
		if (type != null && !type.isUnknown() && !type.isVoid()) {
			guessTable.put(name, type);
		}
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public synchronized int getLine(int pos) {
		return parser.getLine(pos);
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("program\n");
		res.append(program);
		res.append("endprogram\n");
		for (SubRoutine s : sub) {
			res.append(s);
		}
		for (UserClass uc : userclass.values()) {
			res.append(uc);
		}
		res.append(env);
		return res.toString();
	}

	public void prettyPrinterNewline(int position, int level) {
		prettyPrinter.newline(position, level);
	}

	@Override
	public String prettyPrint() {
		return prettyPrinter.prettyPrint();
	}

	@Override
	public void compile() throws TranslationException {
		try {
			machine.reinit();
			for(PredefinedVariable pv:predefinedVariables) {
				machine.createConstant(pv.getScan(), pv.getType().getMType(), pv.getInitialValue());
			}
			compile(machine);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TranslationException(new CompilerError("Compile.Error", e.getLocalizedMessage()));			
		}
	}

	@Override
	public AbstractMachine getMachine() {
		return machine;
	}

	@Override
	public LinkedList<AutoCompleteInformation> getClassAutoCompletes() {
		LinkedList<AutoCompleteInformation> classInfos = new LinkedList<>();
		classInfos.addAll(SystemType.getClassType().getAutoCompletes());
		for (UserClass uc : userclass.values()) {
			classInfos.add(uc.getAutoComplete());
		}
		return classInfos;
	}
	@Override
	public Type getTypeByName(String name) {
		Type t = SystemType.getInstanceByName(name);
		if (t == null || t.isUnknown()) {
			UserClass uc = userclass.getOrDefault(name.toLowerCase(), null);
			if (uc != null) {
				t = uc.getType();
			} else {
				uc = new UserClass(name);
				userclass.put(name.toLowerCase(), uc);
				t = uc.getType();
			}
		}
		return t;
	}
	public UserClass createUserClass(String name) {
		SystemType st = SystemType.getInstanceByName(name.toLowerCase());
		if (st != null && !st.isUnknown()) {
			throw new DoubleClassDeclarationException(name);
		}
		UserClass uc = userclass.getOrDefault(name.toLowerCase(), null);
		if (uc == null) {
			uc = new UserClass(name);
			userclass.put(name.toLowerCase(), uc);
		}
		uc.define();
		return uc;
	}
}
