package de.lathanda.eos.gui;

import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.lathanda.eos.base.util.ErrorBehavior;
import de.lathanda.eos.base.util.GuiToolkit;
import de.lathanda.eos.config.Language;
import de.lathanda.eos.config.LanguageConfig;

import static de.lathanda.eos.base.icons.Icons.*;
import static de.lathanda.eos.gui.icons.Icons.*;

/**
 * Einstellungsfenster
 *
 * @author Peter (Lathanda) Schneider
 */
public class ConfigFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = -7493228893699859641L;
	/**
	 * Oberflächenconfiguration
	 */
	private GuiConfiguration guiConf = GuiConfiguration.def;

	/**
	 * Erzeugt ein Konfigurationsfenster.
	 */
	public ConfigFrame() {
		initComponents();
	}

	private static final NumberFormat FORMAT = DecimalFormat.getInstance();

	/**
	 * Initialisiert die Komponenten.
	 */
	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setIconImage(LOGO);
		generalPanel = new JPanel();
		commandPanel = new JPanel();
		lblLanguage = GuiToolkit.createLabel(Messages.getString("configuration.language"));
		lblFontSize = GuiToolkit.createLabel(Messages.getString("configuration.fontsize"));
		lblUnit = GuiToolkit.createLabel(Messages.getString("configuration.unit"));
		lblDPI = GuiToolkit.createLabel(Messages.getString("configuration.dpi"));
		lblErrorMode = GuiToolkit.createLabel(Messages.getString("configuration.errormode"));
		txtFontSize = GuiToolkit.createTextField();
		txtDPI = GuiToolkit.createTextField();
		txtUnit = GuiToolkit.createTextField();
		
		cmbErrorMode = GuiToolkit.<ErrorBehaviorEntry>createComboBox();

		cmbErrorMode.addItem(ErrorBehaviorEntry.IGNORE);
		cmbErrorMode.addItem(ErrorBehaviorEntry.WARN);
		cmbErrorMode.addItem(ErrorBehaviorEntry.ABORT);
		cmbErrorMode.addItem(ErrorBehaviorEntry.TRACE);
	
		comboLanguage = GuiToolkit.<LanguageConfig>createComboBox();
		Language.def.getAvailableLanguageConfigs().forEach(item->comboLanguage.addItem(item));
		
		generalPanel.add(lblLanguage);
		generalPanel.add(comboLanguage);
		generalPanel.add(lblFontSize);
		generalPanel.add(txtFontSize);
		generalPanel.add(lblUnit);
		generalPanel.add(txtUnit);
		generalPanel.add(lblDPI);
		generalPanel.add(txtDPI);
		generalPanel.add(lblErrorMode);
		generalPanel.add(cmbErrorMode);

		generalPanel.setBorder(GuiToolkit.createTitledBorder(Messages.getString("configuration.general")));
		generalPanel.setLayout(new GridLayout(0, 2));
		getContentPane().add(generalPanel);

		btnOk = GuiToolkit.createButton(OK, null, ae -> {
			writeData();
			setVisible(false);
		});

		btnCancel = GuiToolkit.createButton(CLOSE, null, ae -> {
			readData();
			setVisible(false);
		});

		commandPanel.add(btnOk);
		commandPanel.add(btnCancel);
		getContentPane().add(commandPanel);

		readData();
		pack();
	}

	/**
	 * Liest die Daten aus der Konfiguration ein.
	 */
	private void readData() {
		txtFontSize.setText(String.valueOf(guiConf.getFontsize()));
		txtDPI.setText(String.valueOf(guiConf.getDpi()));
		txtUnit.setText(FORMAT.format(guiConf.getUnit()));
		switch (guiConf.getErrorBehavior()) {
		case ABORT:
			cmbErrorMode.setSelectedItem(ErrorBehaviorEntry.ABORT);
			break;
		case WARN:
			cmbErrorMode.setSelectedItem(ErrorBehaviorEntry.WARN);
			break;
		case IGNORE:
			cmbErrorMode.setSelectedItem(ErrorBehaviorEntry.IGNORE);
			break;
		case TRACE:
			cmbErrorMode.setSelectedItem(ErrorBehaviorEntry.TRACE);
			break;
		}

	}

	/**
	 * überträgt die Daten zurück in die Konfiguration.
	 */
	public void writeData() {
		try {
			guiConf.setFontsize(Integer.parseInt(txtFontSize.getText()));
		} catch (NumberFormatException e) {
			txtFontSize.setText(String.valueOf(guiConf.getFontsize()));
		}
		try {
			guiConf.setDpi(Integer.parseInt(txtDPI.getText()));
		} catch (NumberFormatException e) {
			txtDPI.setText(String.valueOf(guiConf.getDpi()));
		}
		try {
			guiConf.setUnit(FORMAT.parse(txtUnit.getText()).doubleValue());
		} catch (ParseException e) {
			txtUnit.setText(FORMAT.format(guiConf.getUnit()));
		}
		guiConf.setErrorBehavior(((ErrorBehaviorEntry) cmbErrorMode.getSelectedItem()).errorBehavior);
	}

	private JPanel generalPanel;
	private JPanel commandPanel;
	private JLabel lblLanguage;
	private JComboBox<LanguageConfig> comboLanguage;
	private JLabel lblFontSize;
	private JTextField txtFontSize;
	private JLabel lblUnit;
	private JTextField txtUnit;
	private JLabel lblDPI;
	private JTextField txtDPI;
	private JLabel lblErrorMode;
	private JComboBox<ErrorBehaviorEntry> cmbErrorMode;
	private JButton btnOk;
	private JButton btnCancel;

	/**
	 * Eintrag in Dropdownbox.
	 * @author Peter (Lathanda) Schneider
	 *
	 */
	private static class ErrorBehaviorEntry {
		public static final ErrorBehaviorEntry TRACE = new ErrorBehaviorEntry(
				Messages.getString("configuration.errorbehavior.trace"), ErrorBehavior.TRACE);
		public static final ErrorBehaviorEntry ABORT = new ErrorBehaviorEntry(
				Messages.getString("configuration.errorbehavior.abort"), ErrorBehavior.ABORT);
		public static final ErrorBehaviorEntry IGNORE = new ErrorBehaviorEntry(
				Messages.getString("configuration.errorbehavior.ignore"), ErrorBehavior.IGNORE);
		public static final ErrorBehaviorEntry WARN = new ErrorBehaviorEntry(
				Messages.getString("configuration.errorbehavior.warn"), ErrorBehavior.WARN);
		private final String label;
		public final ErrorBehavior errorBehavior;

		private ErrorBehaviorEntry(String label, ErrorBehavior errorBehavior) {
			this.label = label;
			this.errorBehavior = errorBehavior;
		}

		@Override
		public String toString() {
			return label;
		}
	}
}
