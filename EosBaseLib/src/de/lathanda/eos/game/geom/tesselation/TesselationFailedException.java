package de.lathanda.eos.game.geom.tesselation;

/**
 * \brief Korrupte Tesselierung
 *
 * Diese Ausnahme wird erzeugt, wenn versucht wurde ein Polygon in Dreiecke zu zerlegen, dass
 * so geringe Abstände aufweisst, dass aufgrund von numerischen Ungenauigkeiten
 * eine Zerlegung scheitert.
 *
 * @author Lathanda
 */
public class TesselationFailedException extends Exception {

	/**
     * \brief Serialisierungs UID
     */
	private static final long serialVersionUID = 694731230479366619L;

}
