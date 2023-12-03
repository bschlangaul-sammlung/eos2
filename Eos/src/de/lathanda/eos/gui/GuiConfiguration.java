package de.lathanda.eos.gui;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Properties;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.base.util.ErrorBehavior;
import de.lathanda.eos.base.util.GuiToolkit;

/**
 * Diese Klasse verwaltet alle Benutzereinstellungen der Oberfläche.
 *
 * @author Peter (Lathanda) Schneider
 */
public class GuiConfiguration {


	/**
	 * Globales Singelton.
	 */
	public static final GuiConfiguration def = new GuiConfiguration();
	/**
	 * Konfigurationsdatei.
	 */
	private Properties configuration;
	/**
	 * Wurden Daten verändert?
	 */
	private boolean dirty;
	/**
	 * Schriftgröße für die Anzeige.
	 */
	private int fontsize;
	/**
	 * Manuelle dpi des Bildschirms. 0 Systemeinstellung
	 */
	private int dpi;
	/**
	 * Einheit in mm.
	 */
	private double unit;
	/**
	 * Ausgewählte Sprachedefinition
	 */
	private String language;
	/**
	 * Fehlerverhalten.
	 */
	private ErrorBehavior errorBehavior;
	/**
	 * Anzahl der angezeigten Fehlermeldungen
	 */
	private int errorNumber;

	private GuiConfiguration() {
		try {
			load();
		} catch (Exception e) {
			setDefault();
		}
	}

	/**
	 * Konfiguration aus der Konfigurationsdatei laden.
	 * 
	 * 1) %userhome%/.eos/guiconfig.ini
	 * 2) %jar%/guiconfig.ini
	 * 
	 * @throws IOException Dateien sind korrupt...
	 */
	private void load() throws IOException {
		String home = System.getProperty("user.home");
		File file = new File(home + "/.eos/guiconfig.ini");
		if (file.exists()) {
			try (InputStream in = new FileInputStream(file)) {
				load(in);
			}
		} else {
			try (InputStream in = ResourceLoader.getResourceAsStream("guiconfig.ini")) {
				load(in);
			}
		}
	}

	/**
	 * Speichert die KOnfiguration als Datei ab.
	 * 
	 * 1) Verzeichnis %userhome%/.eos/ anlegen
	 * 2) In %userhome%/.eos/guiconfig.ini speichern
	 * 
	 * @throws IOException Rechte fehlen...
	 */
	public void save() throws IOException {
		String home = System.getProperty("user.home");
		File dir = new File(home + "/.eos");
		File file = new File(home + "/.eos/guiconfig.ini");
		if (!(dir.exists() && dir.isDirectory())) {
			dir.mkdir();
		}
		save(new FileOutputStream(file));
	}

	/**
	 * Lädt die Konfiguration aus einem Datenstrom.
	 * @param in
	 * @throws IOException
	 */
	private void load(InputStream in) throws IOException {
		configuration = new Properties();
		configuration.load(in);
		fontsize = Integer.valueOf(configuration.getProperty("fontsize"));
		errorBehavior = ErrorBehavior.decode(Integer.valueOf(configuration.getProperty("errorbehavior")));
		errorNumber = Integer.valueOf(configuration.getProperty("errornumber"));
		dpi = Integer.valueOf(configuration.getProperty("dpi", "0"));
		unit = Double.valueOf(configuration.getProperty("unit", "1"));
		language = configuration.getProperty("language","eos2");
		if (dpi == 0) {
			dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		}
		GuiToolkit.setScreenResolution(dpi);
		dirty = false;
	}

	/**
	 * Speichert die Konfiguration in einen Datenstrom.
	 * @param out
	 * @throws IOException
	 */
	private void save(OutputStream out) throws IOException {
		configuration.setProperty("fontsize", Integer.toString(fontsize));
		configuration.setProperty("errorbehavior", Integer.toString(errorBehavior.encode()));
		configuration.setProperty("errornumber", Integer.toString(errorNumber));
		configuration.setProperty("dpi", Integer.toString(dpi));
		configuration.setProperty("unit", Double.toString(unit));
		configuration.setProperty("language", language);
		configuration.store(out, "EOS Configuration");
		dirty = false;
	}

	/**
	 * Speichert die Konfiguration, wenn sie verändert wurde.
	 * @throws Throwable
	 */
	public void cleanup() throws Throwable {
		if (dirty) {
			save();
		}
	}

	/**
	 * Lädt die Standardwerte.
	 */
	private void setDefault() {
		fontsize = 12;
		errorBehavior = ErrorBehavior.WARN;
		errorNumber = 1;
		dirty = false;
	}

	public int getFontsize() {
		return fontsize;
	}

	public void setFontsize(int fontsize) {
		if (this.fontsize != fontsize) {
			this.fontsize = fontsize;
			fireFontsizeChanged();
		}
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	public int getNumberOfShownErrors() {
		return errorNumber;
	}

	public void setNumberOfShownErrors(int errorNumber) {
		this.errorNumber = errorNumber;
		dirty = false;
	}

	public ErrorBehavior getErrorBehavior() {
		return errorBehavior;
	}

	public void setErrorBehavior(ErrorBehavior errorBehavior) {
		if (this.errorBehavior != errorBehavior) {
			this.errorBehavior = errorBehavior;
			fireErrorBehaviorChanged();
		}
	}

	public int getDpi() {
		return dpi;
	}

	public void setDpi(int dpi) {
		if (dpi != this.dpi) {
			this.dpi = dpi;
			GuiToolkit.setScreenResolution(dpi);
			dirty = true;
		}
	}

	public double getUnit() {
		return unit;
	}

	public void setUnit(double unit) {
		if (unit != this.unit) {
			this.unit = unit;
			GuiToolkit.setUnit(unit);
			dirty = true;
		}
	}

	/**
	 * Liste der von der Konfiguration abhängigen Objekte.
	 * Diese werden bei Änderungen informiert.
	 */
	private LinkedList<GuiConfigurationListener> configurationListener = new LinkedList<>();

	/**
	 * Registriert ein Objekt, sodass es über Konfigurationsänderungen informiert wird.
	 * @param cf
	 */
	public synchronized void addConfigurationListener(GuiConfigurationListener cf) {
		configurationListener.add(cf);
	}

	/**
	 * Entfernt ein Objekt, sodass es nicht mehr über Konfigurationsänderungen informiert wird.
	 * @param cf
	 */
	public synchronized void removeConfigurationListener(GuiConfigurationListener cf) {
		configurationListener.remove(cf);
	}

	/**
	 * Die Schriftgröße hat sich geändert.
	 */
	public synchronized void fireFontsizeChanged() {
		dirty = true;
		configurationListener.forEach(cf -> cf.fontsizeChanged(fontsize));
	}

	/**
	 * Das Fehlerverhalten hat sich geändert.
	 */
	public synchronized void fireErrorBehaviorChanged() {
		dirty = true;
		configurationListener.forEach(cf -> cf.errorBehaviorChanged(errorBehavior));
	}

	/**
	 * Schnittstelle für Objekte, weclhe von der Konfiguration abhängig sind
	 * und über Änderungen informiert werden wollen.
	 * 
	 * @author Peter (Lathanda) Schneider
	 *
	 */
	public interface GuiConfigurationListener {
		/**
		 * Schirftgröße hat sich geändert.
		 * @param fontsize
		 */
		public default void fontsizeChanged(int fontsize) {
		}

		/**
		 * Fehlerverhalten hat sich geändert.
		 * @param errorBehavior
		 */
		public default void errorBehaviorChanged(ErrorBehavior errorBehavior) {
		}
	}
	public void log(String text) {
		System.err.println(text);
		//TODO
	}
	public void log(Exception e) {
		log(e.getLocalizedMessage());
		for(var s :e.getStackTrace()) {
			log(s.toString());
		}
	}
}
