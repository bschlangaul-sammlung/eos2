package tron;

import de.lathanda.eos.base.MutableColor;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.game.Sprite;
import de.lathanda.eos.game.geom.Rectangle;

public class Block extends Sprite {
	MutableColor c;

	public Block(MutableColor c, double x, double y, double width, double height) {
		this.c = c;
		shape = new Rectangle(new Point(x,y), width, height);
		shape.moveTo(x, y);
	}

	@Override
	public void render(Picture g) {
		g.setLineColor(c);
		g.setFillColor(c);
		g.drawShape(shape);
	}

}
