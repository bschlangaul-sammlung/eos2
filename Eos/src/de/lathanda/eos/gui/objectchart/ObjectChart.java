package de.lathanda.eos.gui.objectchart;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.lathanda.eos.base.util.GuiToolkit;
import de.lathanda.eos.baseparser.AbstractProgram;
import de.lathanda.eos.baseparser.CompilerListener;
import de.lathanda.eos.config.Language;
import de.lathanda.eos.gui.BackgroundCompiler;
import de.lathanda.eos.gui.Messages;
import de.lathanda.eos.gui.diagram.Diagram;
import de.lathanda.eos.vm.AbstractMachine;
import de.lathanda.eos.vm.DebugInfo;
import de.lathanda.eos.vm.DebugListener;
import de.lathanda.eos.vm.ErrorInformation;
import de.lathanda.eos.vm.MemoryEntry;
import de.lathanda.eos.vm.ReservedVariables;

/**
 * Objektdiagramm
 *
 * @author Peter (Lathanda) Schneider
 */
public class ObjectChart extends Diagram implements CompilerListener, DebugListener, ListSelectionListener {
	private static final long serialVersionUID = -2959905171654651632L;
	private AbstractMachine machine;
	private AbstractProgram program;
	private JList<String> memoryList;
	private ObjectDiagram objectDiagram;
	private DefaultListModel<String> memoryModel;
	private LinkedList<MemoryEntry> memory;
	private ArrayList<MemoryEntry> variableList = new ArrayList<>();

	public ObjectChart() {
		super(Messages.getString("ObjectChart.Title"));
		memoryModel = new DefaultListModel<String>();
		memoryList = new JList<>(memoryModel);
		memoryList.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		memoryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		memoryList.setFont(GuiToolkit.createFont(Font.SANS_SERIF, Font.PLAIN, 10));
		objectDiagram = new ObjectDiagram();
		setLayout(new BorderLayout());
		add(new JScrollPane(memoryList), BorderLayout.WEST);
		add(new JScrollPane(objectDiagram), BorderLayout.CENTER);
		memoryList.addListSelectionListener(this);
	}

	@Override
	public BufferedImage export(float dpi) {
		return objectDiagram.export(dpi);
	}

	@Override
	public void init(BackgroundCompiler bc) {
		bc.addCompilerListener(this);
	}

	@Override
	public void deinit(BackgroundCompiler bc) {
		bc.removeCompilerListener(this);
	}

	@Override
	public void compileComplete(LinkedList<ErrorInformation> errors, AbstractProgram program) {
		this.machine = program.getMachine();
		machine.addDebugListener(this);
		update();
	}

	@Override
	public void debugPointReached(DebugInfo debugInfo) {
		update();
	}

	private void update() {
		memory = machine.getMemory();
		SwingUtilities.invokeLater(() -> doUpdate());
	}

	private void doUpdate() {
		TreeSet<MemoryEntry> selectedValues = new TreeSet<>();
		int[] indices = memoryList.getSelectedIndices();
		for (int index : indices) {
			selectedValues.add(variableList.get(index));
		}
		memoryModel.clear();
		variableList.clear();
		for (MemoryEntry mem : memory) {
			if (mem.name.equals(ReservedVariables.RESULT)) {
				memoryModel.addElement(Language.def.getLabel(ReservedVariables.RESULT));
			} else if (mem.name.equals(ReservedVariables.WINDOW)) {
				memoryModel.addElement(Language.def.getLabel(ReservedVariables.WINDOW));
			} else if (mem.name.startsWith(ReservedVariables.REPEAT_TIMES_INDEX)) {
				memoryModel.addElement(Language.def.getLabel(ReservedVariables.REPEAT_TIMES_INDEX));
			} else {
				memoryModel.addElement(mem.name);
			}
			variableList.add(mem);
			if (selectedValues.contains(mem)) {
				memoryList.addSelectionInterval(memoryModel.size() - 1, memoryModel.size() - 1);
			}
		}
		revalidate();
	}

	@Override
	public void valueChanged(ListSelectionEvent lse) {
		LinkedList<MemoryEntry> data = new LinkedList<>();
		int[] indices = memoryList.getSelectedIndices();
		for (int index : indices) {
			data.add(variableList.get(index));
		}
		objectDiagram.setData(data, program);
		repaint();
	}
}
