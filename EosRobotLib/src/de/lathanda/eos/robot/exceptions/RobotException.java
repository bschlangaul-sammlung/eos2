package de.lathanda.eos.robot.exceptions;
/**
 * Etwas ist schief gegangen.
 *
 * @author Peter (Lathanda) Schneider
 */

import static de.lathanda.eos.robot.text.Text.ERROR;
public abstract class RobotException extends Exception {
	private static final long serialVersionUID = 6166388499569040612L;
	private final String localMessage;
	public RobotException(String errorID) {
		localMessage = ERROR.getString(errorID);
	}
	@Override
	public String getLocalizedMessage() {
		return localMessage;
	}
}
