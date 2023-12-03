package de.lathanda.eos.gui;

import de.lathanda.eos.vm.Marker;

public interface CodeColorHook {
	void init(SourceCode source);

	void setFontSize(int fontsize);

	void unmarkExecutionPoint();

	void doColoring();

	void markError(Marker code);

	void markExecutionPoint(Marker codeRange);

}
