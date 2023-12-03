
package eos;

import de.lathanda.eos.robot.Robot;
import de.lathanda.eos.robot.exceptions.CubeImmutableException;
import de.lathanda.eos.robot.exceptions.CubeMissingException;
import de.lathanda.eos.robot.exceptions.RobotException;
import de.lathanda.eos.robot.exceptions.RobotMovementFailedException;
import de.lathanda.eos.robot.exceptions.RobotVoidException;
import eos.ausnahmen.BewegungFehlgeschlagenAusnahme;
import eos.ausnahmen.KeinSteinVorhandenAusnahme;
import eos.ausnahmen.RoboterAusnahme;
import eos.ausnahmen.RoboterOhneWeltAusnahme;
import eos.ausnahmen.SteinFeststehendAusnahme;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse erzeugt einen Roboter. Dieser wird erst
 * sichtbar, wenn er eine Welt betritt. 
 * 
 * \code 
 *   Roboter karl = new Roboter(); 
 *   Welt welt = new Welt("spielfeld.eow");
 *   welt.betreten(karl); 
 * \endcode 
 * Nun können andere Befehle folgen. 
 * \code
 *   karl.schritt(); 
 *   karl.hinlegen(); 
 * \endcode
 */
public class Roboter {
	protected Robot robot;

	public Roboter() {
		robot = new Robot();
	}

	public void schritt() {
		try {
			robot.step();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}

	public void schritt(int anzahl) {
		try {
			robot.step(anzahl);
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void schrittZurueck() {
		try {
			robot.stepBack();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void schrittLinks() {
		try {
			robot.stepLeft();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void schrittRechts() {
		try {
			robot.stepRight();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void runterFliegen() {
		try {
			robot.flyDown();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void hochFliegen() {
		try {
			robot.flyUp();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void linksFliegen() {
		try {
			robot.flyLeft();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void rechtsFliegen() {
		try {
			robot.flyRight();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void fliegen() {
		try {
			robot.fly();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}	
	public void zurueckFliegen() {
		try {
			robot.flyBack();
		} catch (RobotMovementFailedException e) {
    		throw new BewegungFehlgeschlagenAusnahme(e); 
		} catch (RobotVoidException e) {
    		throw new RoboterOhneWeltAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void linksdrehen() {
		robot.turnLeft();
	}

	public void rechtsdrehen() {
		robot.turnRight();
	}
	public void umdrehen() {
		robot.turnAround();
	}

	public void hinlegen() {
		try {
			robot.dropStone();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}

	public void aufheben() {
		try {
			robot.pickup();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (CubeMissingException e) {
			throw new KeinSteinVorhandenAusnahme(e);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void aufheben(int n) {
		try {
			robot.pickup(n);
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (CubeMissingException e) {
			throw new KeinSteinVorhandenAusnahme(e);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void steinSetzen(int n) {
		try {
			robot.placeStone(n);
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void steinEntfernen(int n) {
		try {
			robot.removeStone(n);
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void farbeSetzen(Farbe farbe) {
		robot.setRobotColor(farbe.getColor());
	}
	public Farbe farbeLesen() {
		return new Farbe(robot.getRobotColor());
	}
	public void steinFarbeSetzen(Farbe farbe) {
		robot.setStoneColor(farbe.getColor());
	}

	public void markeSetzen() {
		try {
			robot.setMark();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public void markeSetzen(Farbe farbe) {
		try {
			robot.setMark(farbe.getColor());
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}

	public void markeLoeschen() {
		try {
			robot.removeMark();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istMarke() {
		try {
			return robot.isMarked();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istMarke(Farbe farbe) {
		try {
			return robot.isMarked(farbe.getColor());
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istFrei() {
		try {
			return robot.isFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istLinksFrei() {
		try {
			return robot.isLeftFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istRechtsFrei() {
		try {
			return robot.isRightFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istHintenFrei() {
		try {
			return robot.isBackFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istObenFrei() {
		try {
			return robot.isTopFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istUntenFrei() {
		try {
			return robot.isBottomFree();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istHindernis() {
		try {
			return robot.isObstacle();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}		
	}
	public boolean istHintenHindernis() {
		try {
			return robot.isBackObstacle();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}		
	}
	public boolean istLinksHindernis() {
		try {
			return robot.isLeftObstacle();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}		
	}
	public boolean istRechtsHindernis() {
		try {
			return robot.isRightObstacle();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}		
	}
	public boolean istStein() {
		try {
			return robot.hasStone();
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istStein(int n) {
		try {
			return robot.hasStone(n);
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
	}
	public boolean istSueden() {
		return robot.isFacingSouth();
	}
	public boolean istWesten() {
		return robot.isFacingWest();
	}
	public boolean istNorden() {
		return robot.isFacingNorth();
	}
	public boolean istOsten() {
		return robot.isFacingEast();
	}	
	public Farbe steinFarbe() {
		try {
			return new Farbe(robot.stoneColor());
		} catch (RobotVoidException e) {
			throw new RoboterOhneWeltAusnahme(e);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}		
	}
}
