package de.lathanda.eos.parser.core;

import java.util.List;


/**
 * Sammlung von Vervollständigungsmöglichkeiten eines Types.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface AutoCompleteType {

	boolean isUnknown();

	List<AutoCompleteInformation> getAutoCompletes();

}
