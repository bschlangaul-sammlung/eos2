package de.lathanda.eos.robot;

import de.lathanda.eos.base.MutableColor;
import de.lathanda.eos.robot.exceptions.CubeMissingException;
import de.lathanda.eos.robot.exceptions.CubeNoSpaceException;
import de.lathanda.eos.robot.exceptions.RobotException;
import de.lathanda.eos.robot.exceptions.RobotVoidException;

public class BorderColumn extends Column {

	@Override
	public int isReachable(int level, int size, int climb, int fall) {
		return -1;
	}

	@Override
	public boolean isFree(int level, int size) {
		return false;
	}

	@Override
	public void dropCube(int level, Cube cube) throws CubeNoSpaceException {
		throw new CubeNoSpaceException();
	}

	@Override
	public void dropCube(Cube cube) throws CubeNoSpaceException {
		throw new CubeNoSpaceException();
	}

	@Override
	public MutableColor stoneColor(int level) throws CubeMissingException {
		throw new CubeMissingException();
	}

	@Override
	public void setMark(boolean mark) throws RobotVoidException {
		throw new RobotVoidException();
	}

	@Override
	public void pickup() throws RobotException {
		throw new CubeMissingException();
	}

	@Override
	void pickup(int level) throws RobotException {
		throw new CubeMissingException();
	}

	@Override
	public boolean isMarked() throws RobotException {
		throw new RobotVoidException();
	}

	@Override
	public boolean isMarked(MutableColor c) throws RobotException {
		throw new RobotVoidException();
	}

	@Override
	public MutableColor getMark() throws RobotException {
		throw new RobotVoidException();
	}

	@Override
	public void setMark() throws RobotException {
		throw new RobotVoidException();
	}

	@Override
	public void setMark(MutableColor c) throws RobotException {
		throw new RobotVoidException();
	}

	@Override
	public void toggleMark() throws RobotException {
		throw new RobotVoidException();
	}

	@Override
	public void toggleMark(MutableColor c) throws RobotException {
		throw new RobotVoidException();
	}

	@Override
	public void removeCube(int level) throws RobotException {
		throw new CubeMissingException();
	}

	@Override
	public boolean hasCube() throws RobotException {
		return false;
	}

	@Override
	public boolean hasCube(int n) throws RobotException {
		return false;
	}

	@Override
	public void setCube(int level, Cube cube) throws RobotException {
		throw new CubeNoSpaceException();
	}

	@Override
	public Cube[] getCubes() throws RobotException {
		throw new CubeMissingException();
	}

	@Override
	public void remove(int level) throws RobotException {
		throw new CubeMissingException();
	}

}
