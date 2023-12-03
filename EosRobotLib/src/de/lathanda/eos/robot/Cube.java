package de.lathanda.eos.robot;

import de.lathanda.eos.base.MutableColor;
import de.lathanda.eos.robot.exceptions.CubeImmutableException;

/**
 * Der Inhalt einer Zelle.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Cube {
    private MutableColor color;
    private int type;
    private boolean free;
    private Cube(int type) {
    	this.type = type;
    }

    public boolean isFree() {
        return free;
    }
    public boolean isEmpty() {
    	return type == 0;
    }
    public void setEmpty() throws CubeImmutableException {
    	if (type == 3) {
    		throw new CubeImmutableException();
    	} else {
    		type = 0;
    		free = true;
    	}
    }

    public MutableColor getColor() {
    	return color;
    }
    public int getType() {
    	return type;
    }
	public static Cube createCube(int type, MutableColor color) {
		Cube cube = new Cube(type);		
		cube.color = color;
		cube.free  = type == 0;
		return cube;
	}
	public static Cube createStone(MutableColor stoneColor) {
		Cube cube = new Cube(2);
		cube.color = stoneColor;
		cube.free = false;
		return cube;
	}
	public static Cube createRock(MutableColor stoneColor) {
		Cube cube = new Cube(3);
		cube.color = stoneColor;
		cube.free = false;
		return cube;
	}
	public static Cube createEmpty() {
		Cube cube = new Cube(0);
		cube.color = MutableColor.BLACK;
		cube.free = true;
		return cube;
	}
	public static Cube createGround() {
		Cube cube = new Cube(1);
		cube.color = MutableColor.DIRTY_BROWN;
		cube.free = false;
		return cube;		
	}
}
