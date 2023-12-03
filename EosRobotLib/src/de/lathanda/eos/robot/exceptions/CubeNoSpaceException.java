package de.lathanda.eos.robot.exceptions;
/**
 * Nicht genügend Platz für den Stein.
 *
 * @author Peter (Lathanda) Schneider
 * 
 */

public class CubeNoSpaceException extends RobotException {
	private static final long serialVersionUID = -7146680986946313207L;

	public CubeNoSpaceException() {
		super("cube.no.space");
	}
}
