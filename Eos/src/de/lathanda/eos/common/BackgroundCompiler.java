package de.lathanda.eos.common;

import java.util.LinkedList;

/**
 * Asynchroner Kompiler.
 * Der Thread läuft im Hintergrund und übersetzt das Programm.
 * 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.5
 */
public class BackgroundCompiler implements Runnable {
	private AbstractProgram program;
	private final Source source;
	private final LinkedList<ErrorInformation> errors;
	private final CompilerMulticaster cmc;

	public BackgroundCompiler(Source source) {
		this.cmc = new CompilerMulticaster();
		this.source = source;
		errors = new LinkedList<>();
	}

	@Override
	public void run() {
		while (true) {
			update();
		}
	}

	private void update() {
		try {
			String src = source.getSourceCode();
			errors.clear();
			program = Factory.createProgram(src);
			program.parse(source.getPath());
			program.compile();
		} catch (TranslationException te) {
			errors.add(te.getErrorInformation());
		}

		if (program != null) {
			errors.addAll(program.getErrors());
		}
		cmc.fireCompileComplete();
	}

	protected class CompilerMulticaster {

		private final LinkedList<CompilerListener> compilerListener;

		protected CompilerMulticaster() {
			compilerListener = new LinkedList<>();
		}

		void add(CompilerListener cl) {
			compilerListener.add(cl);
		}

		void remove(CompilerListener cl) {
			compilerListener.remove(cl);
		}

		void fireCompileComplete() {
			compilerListener.forEach((cl) -> {
				cl.compileComplete(errors, program);
			});
		}
	}
	public void addCompilerListener(CompilerListener cl) {
		cmc.add(cl);
		if (program != null) {
			// if a program is already available distribute it
			cl.compileComplete(errors, program);
		}
	}

	public void removeCompilerListener(CompilerListener cl) {
		cmc.remove(cl);
	}	
}
