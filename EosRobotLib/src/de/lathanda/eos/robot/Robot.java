package de.lathanda.eos.robot;

import de.lathanda.eos.base.MutableColor;
import de.lathanda.eos.base.util.Direction;
import de.lathanda.eos.robot.exceptions.CubeImmutableException;
import de.lathanda.eos.robot.exceptions.CubeMissingException;
import de.lathanda.eos.robot.exceptions.RobotException;
import de.lathanda.eos.robot.exceptions.RobotMovementFailedException;
import de.lathanda.eos.robot.exceptions.RobotVoidException;
import de.lathanda.eos.robot.gui.Configuration;
import de.lathanda.eos.robot.gui.Configuration.ConfigurationListener;

/**
 * Ein Roboter.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Robot implements ConfigurationListener {
    /**
     * Größe des Roboters
     */
    public static final int SIZE = 3;
    /**
     * Maximale Sprunghöhe
     */
    private int jump = Configuration.def.getJumpheight();
    /**
     * Maximale Fallhöhe
     */
    private int fall = Configuration.def.getFallheight();
    /**
     * Welt in der der Roboter platziert wurde.
     */
    private World world;
    /**
     * Farb des Roboters
     */
    private MutableColor robotColor = MutableColor.BLUE;
    /**
     * Blickrichtung des Roboters
     */
    private Direction direction = Direction.EAST;
    /**
     * x-Koordinate des Roboters.
     */
    private int x = 0;
    /**
     * y-Koordinate des Roboters.
     */
    private int y = 0;
    /**
     * z-Koordinate des Roboters.
     */
    private int z = 0;
    /**
     * Farbe von neuen Steinen
     */
    private MutableColor stoneColor = MutableColor.RED;
    /**
     * Standard Roboter
     */
    public Robot() {
    	Configuration.def.addConfigurationListener(this);
    	world = null;
    }
    /**
     * Setzt die Welt beim betreten dieser.
     * @param world
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param z z-Koordiante
     * @param d Blickrichtung
     */
    protected void initialize(World world, int x, int y, int z, Direction d) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = d;
    	this.world = world;
    }    
    @Override
	public void robotConfigurationChanged(int fallheight, int jumpheight) {
    	jump = jumpheight;
    	fall = fallheight;
	}

	/**
	 * Liefert die Säule vor dem Roboter
	 * @return
	 */
    private Column frontColumn() throws RobotVoidException {
        return getColumn(direction);
    }
    /**
     * Liefer die Säule in in der Richtung ausgehend vom Roboter.
     * @param d
     * @return
     */
    private Column getColumn(Direction d) throws RobotVoidException {
    	if (world != null) {
    		return world.getColumn(new Coordinate(x + d.dx, y + d.dy));
    	} else {
    		throw new RobotVoidException();
    	}
    }    
    /**
     * Liefert die Säule in der sich der Roboter befindet.
     * @return
     */
    private Column getColumn(int x, int y) throws RobotVoidException {
    	if (world != null) {
    		return world.getColumn(new Coordinate(x, y));
    	} else {
    		throw new RobotVoidException();
    	}
    }
    /**
     * Liefert die Säule in der sich der Roboter befindet.
     * @return
     */
    private Column getColumn() throws RobotVoidException {
    	if (world != null) {
    		return world.getColumn(new Coordinate(x, y));
    	} else {
    		throw new RobotVoidException();
    	}
    }
    /**
     * Setzt die Position des Roboters.
     * @param x
     * @param y
     * @param z
     */
    private void setPosition(int x, int y, int z) {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }

    /**
     * Setzt eine Marke zu Füssen des Roboters.
     */
    public final void setMark() throws RobotException {
    	getColumn().setMark(true);
    }

    /**
     * Setzt eine farbige Marke zu Füssen des Roboters.
     */
    public final void setMark(MutableColor c) throws RobotException {
    	getColumn().setMark(c);
    }
    
    /**
     * Entfernt die Marke zu Füssen des Robters.
     * @throws RobotVoidException
     */
    public final void removeMark() throws RobotException {
    	getColumn().setMark(false);
    }
    /**
     * Legt einen Stein vor dem Roboter ab.
     * @throws RobotVoidException
     */
    public final void dropStone() throws RobotException {
        frontColumn().dropCube(z, Cube.createStone(stoneColor));
    }
    /**
     * Legt einen Stein vor dem Roboter ab.
     * @param c Farbe des Steins
     * @throws RobotVoidException
     */
    public final void dropStone(MutableColor c) throws RobotException {
        frontColumn().dropCube(z, Cube.createStone(c));
    }
    /**
     * Gibt die Farbe des Steins zurück auf den der Roboter einen neuen Stein legen würde.
     * @return Farbe des Bodensteins, Bodenfarbe falls es diesen nicht gibt.
     */
    public final MutableColor stoneColor() throws RobotException {
    	return frontColumn().stoneColor(z);
    }
    /**
     * Hebt einen Stein vor dem Roboter auf.
     * @throws RobotVoidException
     * @throws CubeMissingException
     */
    public final void pickup() throws RobotException {
        frontColumn().pickup(z);
    }
    /**
     * Hebt mehrere Steine auf.
     * @param n Anzahl der Steine
     * @throws RobotVoidException
     * @throws CubeMissingException
     * @throws CubeImmutableException
     */
    public final void pickup(int n) throws RobotException {
    	for (int i = n; i --> 0; ) {
    		pickup();
    	}
    }
    /**
     * Prüft ob der Roboter in einer Marke steht.
     * @return
     * @throws RobotVoidException 
     */
    public final boolean isMarked() throws RobotException {
        return getColumn().isMarked();
    }
    /**
     * Prüft ob der Roboter in einer Marke mit einer bestimmten Farbe steht.
     * @return
     * @throws RobotVoidException 
     */
    public final boolean isMarked(MutableColor c) throws RobotException {
        return getColumn().isMarked(c);
    }    
    /**
     * Dreht den Roboter nach links.
     */
    public final void turnLeft() {
        direction = direction.getLeft();
    }
    /**
     * Dreht den Roboter nach rechts.
     */
    public final void turnRight() {
        direction = direction.getRight();
    }
    /**
     * Dreht den Roboter um.
     */
    public final void turnAround() {
        direction = direction.getLeft().getLeft();
    }
    /**
     * Der Roboter geht in die Richtung.
     * Hierbei wird die Fallhöhe und Sprunghöhe beachtet.
     * @param d
     * @throws RobotMovementFailedException
     * @throws RobotVoidException 
     */
    private void stepDirection(Direction d) throws RobotException {
        Column columnTo = getColumn(d);
        int newZ = columnTo.isReachable(z, SIZE, jump, fall);
        if (newZ >= 0) {
            setPosition(x + d.dx, y + d.dy, newZ);
        } else {
            throw new RobotMovementFailedException();
        }    	
    }
    /**
     * Der Roboter macht einen Schritt nach vorne.
     * @throws RobotMovementFailedException
     */
    public void step() throws RobotException {
        stepDirection(direction);
    }
    /**
     * Der Roboter macht mehrere Schritte nach vorne.
     * @param count Anzahl der Schritte.
     * @throws RobotMovementFailedException
     * @throws RobotVoidException
     */
    public void step(int count) throws RobotException {
        for(int i = count; i --> 0; ) {
            step();
        }
    }
    /**
     * Der Roboter macht einen Schritt nach hinten.
     * @throws RobotMovementFailedException
     * @throws RobotVoidException
     */
	public void stepBack() throws RobotException {
        stepDirection(direction.getBack());		
	}
	/**
	 * Der Roboter macht einen Schritt nach links.
	 * @throws RobotMovementFailedException
	 */
	public void stepLeft() throws RobotException {
        stepDirection(direction.getLeft());		
    }
	/**
	 * Der Roboter macht einen Schritt nach rechts.
	 * @throws RobotMovementFailedException
	 * @throws RobotVoidException
	 */
	public void stepRight() throws RobotException {
        stepDirection(direction.getRight());		
	}
	/**
	 * Prüft ob in dieser Richtung ein Hindernis ist.
	 * @param direction
	 * @return
	 */
	private boolean isObstacleDirection(Direction direction) throws RobotException {
		return getColumn(direction).isReachable(z, SIZE, jump, fall) < 0;
	}
	/**
	 * Prüft ob vor dem Roboter ein Hindernis ist.
	 * @return
	 * @throws RobotVoidException
	 */
	public boolean isObstacle() throws RobotException {
		return isObstacleDirection(direction);
	}
	/**
	 * Prüft ob links vom Roboter ein Hindernis ist.
	 * @return
	 * @throws RobotVoidException
	 */
	public boolean isLeftObstacle() throws RobotException {
		return isObstacleDirection(direction.getLeft());
	}
	/**
	 * Prüft ob rechts vom Roboter ein Hindernis ist.
	 * @return
	 * @throws RobotVoidException
	 */
	public boolean isRightObstacle() throws RobotException {
		return isObstacleDirection(direction.getRight());
	}
	/**
	 * Prüft ob hinter dem Roboter ein Hindernis ist.
	 * @return
	 * @throws RobotVoidException
	 */
	public boolean isBackObstacle() throws RobotException {
		return isObstacleDirection(direction.getBack());
	}
	/**
	 * Der Roboter fliegt in diese Richtung.
	 * @param dx
	 * @param dy
	 * @param dz
	 * @throws RobotMovementFailedException
	 */
    private void flyDirection(int dx, int dy, int dz) throws RobotException {
        Column column=  world.getColumn(new Coordinate(x + dx, y + dx));
        if (column.isFree(z+dz, SIZE)) {
            setPosition(x + dx, y + dy, z+dz);
        } else {
            throw new RobotMovementFailedException();
        }    	   	
    }
    /**
     * Der Roboter fliegt nach unten.
     * @throws RobotVoidException
     * @throws RobotMovementFailedException
     */
    public void flyDown() throws RobotException {
		flyDirection(0,0,-1);
	}
    /**
     * Der Roboter fliegt nach oben.
     * @throws RobotVoidException
     * @throws RobotMovementFailedException
     */
	public void flyUp() throws RobotException {
		flyDirection(0,0,1);
	}
	/**
	 * der Roboter fliegt nach vorne.
	 * @throws RobotVoidException
	 * @throws RobotMovementFailedException
	 */
	public void fly() throws RobotException {
		flyDirection(direction.dx, direction.dy, 0);
	}
	/**
	 * Der Roboter fliegt nach links.
	 * @throws RobotVoidException
	 * @throws RobotMovementFailedException
	 */
	public void flyLeft() throws RobotException {
		flyDirection(direction.getLeft().dx, direction.getLeft().dy, 0);
	} 
	/**
	 * Der Roboter fliegt nach rechts.
	 * @throws RobotVoidException
	 * @throws RobotMovementFailedException
	 */
	public void flyRight() throws RobotException {
		flyDirection(direction.getRight().dx, direction.getRight().dy, 0);
	} 
	/**
	 * Der Roboter fliegt zurück.
	 * @throws RobotVoidException
	 * @throws RobotMovementFailedException
	 */
	public void flyBack() throws RobotException {
		flyDirection(direction.getBack().dx, direction.getBack().dy, 0);
	} 
	/**
	 * Prüft ob diese Richtung frei zum fliegen ist.
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	private boolean isFreeDirection(int dx, int dy, int dz) throws RobotException {
		return getColumn(x+dx, y+dy).isFree(z+dz, SIZE);
	}
	/**
	 * Prüft ober der Roboter nach vorne fliegen könnte.
	 * @return
	 */
	public boolean isFree() throws RobotException {
		return isFreeDirection(direction.dx, direction.dy, 0);
	}
	/**
	 * Prüft ober der Roboter nach links fliegen könnte.
	 * @return
	 */
	public boolean isLeftFree() throws RobotException {
		return isFreeDirection(direction.getLeft().dx, direction.getLeft().dy, 0);
	}
	/**
	 * Prüft ober der Roboter nach rechts fliegen könnte.
	 * @return
	 */
	public boolean isRightFree() throws RobotException {
		return isFreeDirection(direction.getRight().dx, direction.getRight().dy, 0);
	}
	/**
	 * Prüft ober der Roboter zurück fliegen könnte.
	 * @return
	 */
	public boolean isBackFree() throws RobotException {
		return isFreeDirection(direction.getBack().dx, direction.getBack().dy, 0);
	}
	/**
	 * Prüft ober der Roboter nach oben fliegen könnte.
	 * @return
	 */
	public boolean isTopFree() throws RobotException {
		return isFreeDirection(0,0,1);
	}
	/**
	 * Prüft ober der Roboter nach unten fliegen könnte.
	 * @return
	 */
	public boolean isBottomFree() throws RobotException {
		return isFreeDirection(0,0,-1);
	}
	/**
	 * Platziert einen Stein auf Fußhöhe + n oder tauscht einen vorhandenen gegen diesen aus.
	 * @param n
	 * @throws RobotVoidException
	 * @throws CubeImmutableException
	 */
	public void placeStone(int n) throws RobotException {
		frontColumn().setCube(z+n, Cube.createStone(stoneColor));	
	}
	/**
	 * Platziert einen farbigen Stein auf Fußhöhe + n oder tauscht einen vorhandenen gegen diesen aus.
	 * @param n
	 * @param c Farbe des Steins
	 * @throws RobotVoidException
	 * @throws CubeImmutableException
	 */
	public void placeStone(int n, MutableColor c) throws RobotException {
		frontColumn().setCube(z+n, Cube.createStone(c));	
	}
	/**
	 * Entfernt einen Stein auf Fußhöhe + n.
	 * @throws RobotVoidException
	 * @throws CubeImmutableException
	 */
	public void removeStone(int n) throws RobotException {
		frontColumn().removeCube(z+ n);
	}
	/**
	 * Setzt die Farbe die neue Steine haben.
	 * @param c
	 */
	public void setStoneColor(MutableColor c) {
		stoneColor = c;		
	}
	public MutableColor getStoneColor() {
		return stoneColor;
	}
	/**
	 * Prüft ob vor dem Roboter mindestens ein Stein liegt.
	 * @return
	 * @throws RobotVoidException
	 */
	public boolean hasStone() throws RobotException {
		return frontColumn().hasCube();
	}
	/**
	 * Prüft ob vor dem Roboter n Steine liegen.
	 * @param n
	 * @return
	 * @throws RobotVoidException
	 */
	public boolean hasStone(int n) throws RobotException  {
		return frontColumn().hasCube(n);
	}
	/**
	 * Prüft ob der Roboter nach Süden blickt.
	 * @return
	 */
	public boolean isFacingSouth() {
		return direction == Direction.SOUTH;
	}
	/**
	 * Prüft ob der Roboter nach Westen blickt.
	 * @return
	 */
	public boolean isFacingWest() {
		return direction == Direction.WEST;
	}
	/**
	 * Prüft ob der Roboter nach Norden blickt.
	 * @return
	 */
	public boolean isFacingNorth() {
		return direction == Direction.NORTH;
	}
	/**
	 * Prüft ob der Roboter nach Osten blickt.
	 * @return
	 */
	public boolean isFacingEast() {
		return direction == Direction.EAST;
	}
	
	public MutableColor getRobotColor() {
		return robotColor;
	}
	public void setRobotColor(MutableColor robotColor) {
		this.robotColor = robotColor;
	}
	public Direction getDirection() {
		return direction;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	} 
}
