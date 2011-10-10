package scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import component.Label;
import component.Panel;
import component.Positionable;

import core.FontManager;
import core.GameDirector;

public class PartyInventoryScene extends Scene {
	public static final SceneID ID = SceneID.PartyInventoryScene;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		UnicodeFont h1 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H1);
		UnicodeFont h2 = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.H2);
		
		Label titleLabel = new Label(container, h1, Color.white, "Party Inventory Scene");
		Label subtitleLabel = new Label(container, h2, Color.white, "Press escape to go back");
		
		mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.BottomCenter, 0, -5);
		mainLayer.add(subtitleLabel, titleLabel.getPosition(Positionable.ReferencePoint.BottomCenter), Positionable.ReferencePoint.TopCenter, 0, 5);
		
		backgroundLayer.add(new Panel(container, new Color(0xc4aeca)));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		return;
	}

	@Override
	public int getID() {
		return ID.ordinal();
	}
}