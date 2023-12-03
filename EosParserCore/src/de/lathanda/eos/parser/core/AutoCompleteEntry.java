package de.lathanda.eos.parser.core;


/**
 * Eintrag für die Automatische Vervollständigung in der GUI.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class AutoCompleteEntry implements AutoCompleteInformation {
	/**
	 * Basistyp
	 */
	public final Type cls;
	/**
	 * Text der mit der Eingabe abgeglichen wird.
	 */
	public final String scantext;
	/**
	 * Text der in der Liste angezeigt wird.
	 */
	public final String label;
	/**
	 * Sortierkriterium.
	 */
	public final String sort;
	/**
	 * Hilfe die bei Mausover angezeigt wird (Tooltip).
	 */
	public final String tooltip;
	/**
	 * Text der beim Anwenden eingefügt wird.
	 */
	public final String template;
	/**
	 * Art der Auswahl. Methode, Attribut, Klasse, Template.
	 * Beeinflusst verwendetes Icon und Sortierung.
	 */
	public final int type;
	public final int selectionStart;
	public final int selectionLength;

	public AutoCompleteEntry(Type cls, String scantext, String label, String sort, String tooltip, String template, int type, int start, int length) {
		this.cls = cls;
		this.scantext = scantext;
		this.label = label;
		this.sort = sort;
		this.tooltip = tooltip;
		this.template = template;
		this.type = type;
		this.selectionStart = start;
		this.selectionLength = length;
	}

	public static AutoCompleteEntry createTemplateEntry(String sort, String label, String template, int start, int length) {
		return new AutoCompleteEntry(
			Type.getVoid(),	
			"",
			label,
			sort,
			"",
			template,
			CODE,
			start,
			length
		);
	}
	public static AutoCompleteEntry createClassEntry(Type type, String scan, String label, String tooltip) {
		return new AutoCompleteEntry(
			type,	
			scan,
			label,
			scan,
			tooltip,
			label,
			CLASS,
			label.length(),
			0
		);
	}	

	public static AutoCompleteEntry createMethodEntry(SystemType cls, String scan, String label, String[] parameters, String tooltip) {
		StringBuilder template = new StringBuilder(label);
		int start, length;
		if (parameters.length == 0) {
			template.append("()");
			start = template.length();
			length = 0;
		} else {
			template.append("(");
			start = template.length();
			length = parameters[0].length();
			template.append(parameters[0]);
			for(int i = 1; i < parameters.length; i++) {
				template.append(", ");
				template.append(parameters[i]);
			}
			template.append(")");
			
		}
		return new AutoCompleteEntry(
			cls,	
			scan,
			label,
			scan,
			tooltip,
			template.toString(),
			METHOD,
			start,
			length
		);
	}

	public static AutoCompleteEntry createPropertyEntry(SystemType cls, String scan, String label, String tooltip) {
		return new AutoCompleteEntry(
			cls,	
			scan,
			label,
			scan,
			tooltip,
			label,
			PROPERTY,
			label.length(),
			0
		);
	}
	
	@Override
	public String getScantext() {
		return scantext;
	}

	public AutoCompleteType getCls() {
		return cls;
	}

	public String getLabel() {
		return label;
	}

	public String getSort() {
		return sort;
	}

	public String getTooltip() {
		return tooltip;
	}

	public String getTemplate() {
		return template;
	}

	public int getType() {
		return type;
	}

	@Override
	public int getSelectionStart() {
		return selectionStart;
	}

	@Override
	public int getSelectionLength() {
		return selectionLength;
	}

}
