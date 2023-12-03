package de.lathanda.eos.parser.core;


public interface AutoCompleteInformation extends Comparable<AutoCompleteInformation> {

	final static char[] PREFIX = { '@', '%', '!', '$', '§', '&' };
	final int METHOD = 0;
	final int PROPERTY = 1;
	final int CLASS = 2;
	final int PRIVATE = 3;
	final int NEUTRAL = 4;
	final int CODE = 5;

	String getScantext();

	String getLabel();

	int getType();

	String getTooltip();

	AutoCompleteType getCls();

	String getSort();

	String getTemplate();
	
	int getSelectionStart();
	
	int getSelectionLength();

	@Override
	default int compareTo(AutoCompleteInformation b) {
		if (b.getType() == getType()) {
			return getSort().compareTo(b.getSort());
		} else {
			return getType() - b.getType();
		}
	}

}
