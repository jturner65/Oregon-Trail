package component.parallax;

import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import component.Panel;
import component.sprite.Sprite;

public class ParallaxComponentLoop extends ParallaxComponent {
	private Sprite sprite;
	private Panel panel;
	
	/**
	 * Constructs a ParallaxSprite with a context, spriteWidth, and image. Sprite can have a random X position.
	 * @param context Context to use
	 * @param spriteWidth Width the sprite should be (e.g. for scaling)
	 * @param image Image to use for the sprite
	 * @param distance What the distance this sprite should be
	 */
	public ParallaxComponentLoop(GUIContext context, int spriteWidth, Image image, int distance) {
		super(context, spriteWidth, image, distance);
		
		sprite = new Sprite(context, spriteWidth, image);
		
		panel = new Panel(context, spriteWidth, sprite.getHeight());
		panel.add(sprite, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, 0, 0);
		
		add(panel, super.panel.getPosition(ReferencePoint.BOTTOMLEFT), ReferencePoint.BOTTOMRIGHT, 0, 0);
	}
	
	@Override
	public void update(int delta) {
		if (isPaused()) {
			return;
		}
		
		elapsedTime += delta;
		
		if (elapsedTime > maxElapsedTime) {
			super.panel.setLocation(super.panel.getX() + DELTA_X, super.panel.getY());
			panel.setLocation(panel.getX() + DELTA_X, panel.getY());
			elapsedTime = 0;
		}
		
		if (super.panel.getX() > container.getWidth()) {
			super.panel.setPosition(panel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPRIGHT);
		}
		
		if (panel.getX() > container.getWidth()) {
			panel.setPosition(super.panel.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPRIGHT);
		}
	}
}
