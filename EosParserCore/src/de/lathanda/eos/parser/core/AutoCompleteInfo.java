package de.lathanda.eos.parser.core;


/**
 * Daten einer Autovervollständigungsmöglichkeit.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
class AutoCompleteInfo implements AutoCompleteInformation {
	private String name;
	private AutoCompleteType t;
	private int type;
	private int selectionStart;
	private int selectionLength;

	public AutoCompleteInfo(String name, AutoCompleteType t, int type, int start, int length) {
		this.name = name;
		this.t = t;
		this.type = type;
		this.selectionStart = start;
		this.selectionLength = length;
	}

	@Override
	public String getScantext() {
		return name;
	}

	@Override
	public String getLabel() {
		return name;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public String getTooltip() {
		return "";
	}

	@Override
	public AutoCompleteType getCls() {
		return t;
	}

	@Override
	public String getSort() {
		return PREFIX[type] + name;
	}

	@Override
	public String getTemplate() {
		return name;
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
