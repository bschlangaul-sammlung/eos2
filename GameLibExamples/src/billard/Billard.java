package billard;

import de.lathanda.eos.base.MutableColor;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.Game;

public class Billard {
	public static void main(String[] args) {
		Game game = new Game(300, 200, MutableColor.GREEN, 100, "Billard");
		game.addSprite(new Border(-150, -100,   2, 200, new Vector( 1,  0)));
		game.addSprite(new Border( 148, -100,   2, 200, new Vector(-1,  0)));
		game.addSprite(new Border(-150, -100, 300,   2, new Vector( 0,  1)));
		game.addSprite(new Border(-150,   98, 300,   2, new Vector( 0, -1)));
		game.addSprite(new Ball(-125,   0, MutableColor.YELLOW));
		game.addSprite(new Ball(-115,  -6, MutableColor.YELLOW));
		game.addSprite(new Ball(-115,   6, MutableColor.YELLOW));
		game.addSprite(new Ball(-105, -12, MutableColor.YELLOW));
		game.addSprite(new Ball(-105,   0, MutableColor.YELLOW));
		game.addSprite(new Ball(-105,  12, MutableColor.YELLOW));
		game.addSprite(new Ball( -95,  -6, MutableColor.YELLOW));
		game.addSprite(new Ball( -95,   6, MutableColor.YELLOW));
		game.addSprite(new Ball( -85,   0, MutableColor.YELLOW));
		game.addSprite(new WhiteBall(135, 0));
	}
}
