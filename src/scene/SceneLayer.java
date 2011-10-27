package scene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import component.AnimatingColor;
import component.Component;

public class SceneLayer extends Component {
	private AnimatingColor overlayColor;
	
	public SceneLayer(GUIContext container) {
		super(container, container.getWidth(), container.getHeight());
		setLocation(0, 0);
		setVisible(true);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		g.setWorldClip(getX(), getY(), container.getWidth(), getHeight());
		super.render(container, g);
		
		if (overlayColor != null) {
			g.setColor(overlayColor);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		
		g.clearWorldClip();
	}
	
	@Override
	public void update(int delta) {
		if (overlayColor != null) {
			overlayColor.update(delta);
		}
	}
	
	public void add(Component component) { 
		components.add(component);
		component.setParentComponent(this);
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
	
	public void setOverlayColor(AnimatingColor overlayColor) {
		this.overlayColor = overlayColor;
	}
}
