package component.sprite;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

import component.Component;

/**
 * {@code Sprite} inherits from {@code Component} to extend features
 * to represent a sprite with an left and right animation.
 */
public class Sprite extends Component {
	private Animation leftAnimation;
	private Animation rightAnimation;
	private Animation currentAnimation;

	private float rotation;
	private float scale;
	
	public static enum DirectionFacing { LEFT, RIGHT }
	private DirectionFacing xDirection = DirectionFacing.LEFT;
	
	/**
	 * Constructs a {@code Sprite} with a right and left animation.
	 * @param context The GUI context
	 * @param rightAnimation Animation for when facing right
	 * @param leftAnimation Animation for when facing left
	 */
	public Sprite(GUIContext context, Animation rightAnimation, Animation leftAnimation) {
		super(context, rightAnimation.getWidth(), rightAnimation.getHeight());
				
		this.rightAnimation = rightAnimation;
		this.leftAnimation = leftAnimation;
		
		xDirection = DirectionFacing.LEFT;
		currentAnimation = leftAnimation;
		
		scale = 1;
		rotation = 0;
	}
	
	/**
	 * Constructs a {@code Sprite} with a right and left image..
	 * @param container The GUI context
	 * @param rightImage Image for when facing right
	 * @param leftImage Image for when facing left
	 */
	public Sprite(GUIContext context, Image rightImage, Image leftImage) {
		this(context, new Animation(new Image[]{rightImage}, 1), new Animation(new Image[]{leftImage}, 1));
	}
	
	public Sprite(GUIContext context, Image image) {
		this(context, image, image);
	}
	
	public Sprite(GUIContext context, int width, Image image) {
		super(context, width, width * image.getHeight() / image.getWidth());
		
		this.leftAnimation = new Animation(new Image[]{image}, 1);
		currentAnimation = leftAnimation;
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		super.render(context, g);

		currentAnimation.draw(getX(), getY(), getWidth(), getHeight());
	}
	
	/**
	 * Updates the Sprite on a clock cycle.
	 * @param container Container displaying the component
	 * @param game Game containing the Entity
	 * @param delta Time since last update
	 */
	public void update(GameContainer container, StateBasedGame game, int delta) {
		if (xDirection == DirectionFacing.LEFT) {
			currentAnimation = leftAnimation;
		} else {
			currentAnimation = rightAnimation;
		}
		
		currentAnimation.update(delta);
	}
	
	/**
	 * Get the rotation.
	 * @return Rotation of the Sprite
	 */
	public float getRotation() {
		return rotation;
	}
	
	/**
	 * Get which direction the Sprite is facing.
	 * @return Direction the Sprite is facing
	 */
	public DirectionFacing getDirectionFacing() {
		return xDirection;
	}
	
	/**
	 * Get the scale.
	 * @return Scale of the Sprite
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * Set the scale of the Sprite
	 * @param scale New scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * Set the rotation of the Sprite
	 * @param rotation New rotation in degrees
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Sets the direction the Sprite is facing
	 * @param xDirection New direction the Sprite is facing
	 */
	public void setDirectionFacing(DirectionFacing xDirection) {
		this.xDirection = xDirection;
	}
}
