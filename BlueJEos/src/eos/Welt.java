package eos;

import de.lathanda.eos.robot.World;
import de.lathanda.eos.robot.exceptions.CubeImmutableException;
import de.lathanda.eos.robot.exceptions.CubeMissingException;
import de.lathanda.eos.robot.exceptions.RobotEntranceMissingException;
import de.lathanda.eos.robot.exceptions.RobotException;
import de.lathanda.eos.robot.exceptions.RobotNoSpaceException;
import de.lathanda.eos.robot.exceptions.UnknownWorldVersionException;
import de.lathanda.eos.robot.exceptions.WorldLoadFailedException;
import de.lathanda.eos.robot.exceptions.WorldNotFoundException;
import eos.ausnahmen.EingangFehltAusnahme;
import eos.ausnahmen.KeinPlatzAusnahme;
import eos.ausnahmen.KeinSteinVorhandenAusnahme;
import eos.ausnahmen.RoboterAusnahme;
import eos.ausnahmen.SteinFeststehendAusnahme;
import eos.ausnahmen.WeltKorruptAusnahme;
import eos.ausnahmen.WeltNichtGefundenAusnahme;

/**
 * @author Peter Schneider
 * 
 * Eine Welt für Roboter.
 */
public class Welt {
    World world;
    public Welt() {
        world = new World();
    }
    public Welt(String filename) {
    	this();
    	try {
    		world.load(filename);
    	} catch (UnknownWorldVersionException | WorldLoadFailedException cwe) {
    		throw new WeltKorruptAusnahme(cwe);
    	} catch (WorldNotFoundException wnfe) {
    		throw new WeltNichtGefundenAusnahme(wnfe);
    	}
    }
    public void betreten(Roboter roboter) {
        try {
			world.enter(roboter.robot);
		} catch (RobotEntranceMissingException nle) {
			throw new EingangFehltAusnahme(nle);
		} catch (RobotNoSpaceException rnse) {
			throw new KeinPlatzAusnahme(rnse);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
    }
    public void steinSetzen(int x, int y, int z) {
    	try {
			world.setStone(x,y,z);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
    }
    public void steinFarbeSetzen(Farbe farbe) {
    	world.setStoneColor(farbe.getColor());
    }
    public void steinHinlegen(int x, int y) {
    	try {
			world.dropStone(x, y);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
    }
    public void steinEntfernen(int x, int y, int z) {
    	try {
			world.removeStone(x,y,z);
		} catch (CubeImmutableException e) {
			throw new SteinFeststehendAusnahme(e); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
    }
    public void steinAufheben(int x, int y) {
    	try {
			world.pickupStone(x,y);
		} catch (CubeMissingException cme) {
    		throw new KeinSteinVorhandenAusnahme(cme); 
		} catch (CubeImmutableException cie) {
    		throw new SteinFeststehendAusnahme(cie); 
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
    }
    public void laden(String name) {
    	try {
    		world.load(name);
    	} catch (UnknownWorldVersionException | WorldLoadFailedException cwe) {
    		throw new WeltKorruptAusnahme(cwe);    		
    	} catch (WorldNotFoundException wnfe) {
    		throw new WeltNichtGefundenAusnahme(wnfe);    		
		}
    }
    public void ziegelVerstreuen(int links, int oben, int rechts, int unten, double dichte) {
    	try {
			world.fillRandom(links, oben, rechts, unten, dichte);
		} catch (RobotException re) {
			throw new RoboterAusnahme(re);
		}
    }
}
