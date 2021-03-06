package model.huntingMap;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import component.sprite.AnimatingSprite;
import component.sprite.PreyAnimatingSprite;
import component.sprite.AnimatingSprite.Direction;
import core.ImageStore;

public class PreyCow extends Prey {
	
		/**how much meat the cow gives*/
	public final static int cowMeat = 10;
	private int direction;
	private int timeMovingInCurrentDirection = 0;
	
	public PreyCow(GameContainer container, StateBasedGame game, Random cowRand, int mapXWidth, int mapYHeight){
		//cow gives 10 meat
		super(cowMeat);
		//takes 3 shots to kill a cow
		this.hitPoints = 3;
		direction = new Random().nextInt(5);
		Image[] cowAnimLeft = new Image[9];
 		Image[] cowAnimRight = new Image[9];
 		Image[] cowAnimFront = new Image[9];
 		Image[] cowAnimBack = new Image[9];
 		
 		//9 images of animation
 		for (int incr = 1; incr < 10; incr ++) {
 	 		cowAnimLeft[incr - 1] = ImageStore.get().getImage("HUNT_COWLEFT" + incr);
 	 		cowAnimRight[incr - 1] = ImageStore.get().getImage("HUNT_COWRIGHT" + incr);
 	 		cowAnimFront[incr - 1] = ImageStore.get().getImage("HUNT_COWFRONT" + incr);
 	 		cowAnimBack[incr - 1] = ImageStore.get().getImage("HUNT_COWBACK" + incr);		
 		}//for i 1 to 10

		preySprite =  new PreyAnimatingSprite(container,
//				48,
				new Animation(cowAnimLeft, 250),
				new Animation(cowAnimRight, 250),
				new Animation(cowAnimFront, 250),
				new Animation(cowAnimBack, 250),
				AnimatingSprite.Direction.RIGHT);
		
		this.xLocation = cowRand.nextInt(mapXWidth/4) + mapXWidth/2; //focus location near center of hunt map
		this.yLocation = cowRand.nextInt(mapYHeight/4) + mapYHeight/2; //focus location near center of hunt map
		
		movePrey(0);
	}//prey cow constructor



	/**
	 * @param direction the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}



	/**
	 * will determine where the cow is going to move to next
	 */
	@Override
	public int[] movePrey(int delta) {
		switch(direction) {
		case 1:
			preySprite.setDirectionFacing(Direction.LEFT);
			preySprite.setMoving(true);
			//move map to right
			preySprite.setLocation(preySprite.getX() - delta, preySprite.getY());
			xLocation -= delta;
			break;
		case 2:
			preySprite.setDirectionFacing(Direction.BACK);
			preySprite.setMoving(true);
			//move map down
			preySprite.setLocation(preySprite.getX(), preySprite.getY() - delta);
			yLocation -= delta;
			break;
		case 3:
			preySprite.setDirectionFacing(Direction.RIGHT);
			preySprite.setMoving(true);
			//move map to left
			preySprite.setLocation(preySprite.getX() + delta, preySprite.getY());
			xLocation += delta;
			break;
		case 4:
			preySprite.setDirectionFacing(Direction.FRONT);
			preySprite.setMoving(true);
			//move map up
			preySprite.setLocation(preySprite.getX(), preySprite.getY() + delta);
			yLocation += delta;
			break;
		case 0:
			preySprite.setMoving(false);
			break;
		}
		
		int[] returnThis = {preySprite.getX(), preySprite.getY()};
		return returnThis;
	}

	public int getDirection() {
		if(this.timeMovingInCurrentDirection < 50) {
			this.timeMovingInCurrentDirection++;
		} else {
			this.timeMovingInCurrentDirection = 0;
			this.direction = new Random().nextInt(5);
		}

		return this.direction;
	}
}
