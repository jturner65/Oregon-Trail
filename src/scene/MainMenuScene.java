package scene;


import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import component.Button;
import component.Label;
import component.Panel;
import component.Positionable;
import component.sprite.Sprite;

import core.*;

/**
 * Scene displayed upon game startup - displays title of game.
 */

public class MainMenuScene extends Scene {
	public static final SceneID ID = SceneID.MAINMENU;
	
	private Button newGameButton;
	Music sound;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		
		Font fieldFont = GameDirector.sharedSceneListener().getFontManager().getFont(FontManager.FontID.FIELD);
		newGameButton = new Button(container, 240, 60, new Label(container, fieldFont, Color.white, ConstantStore.get("MAIN_MENU", "NEW_GAME")));
		newGameButton.addListener(new ButtonListener());
		
		//mainLayer.add(titleLabel, mainLayer.getPosition(Positionable.ReferencePoint.CENTERCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -5);
		
		Sprite logoSprite = new Sprite(container, 480, new Image("resources/graphics/logo.png", false, Image.FILTER_NEAREST));
				
		mainLayer.add(logoSprite, mainLayer.getPosition(Positionable.ReferencePoint.TOPCENTER), Positionable.ReferencePoint.TOPCENTER, 0, 75);
		mainLayer.add(newGameButton, mainLayer.getPosition(Positionable.ReferencePoint.BOTTOMCENTER), Positionable.ReferencePoint.BOTTOMCENTER, 0, -75);
		
		backgroundLayer.add(new Panel(container, new Image("resources/graphics/backgrounds/map.png", false, Image.FILTER_NEAREST)));
		
		sound = new Music("resources/music/GBUogg.ogg");
		sound.loop();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}
	
	@Override
	public int getID() {
		return ID.ordinal();
	}
	
	private class ButtonListener implements ComponentListener {
		@Override
		public void componentActivated(AbstractComponent source) {
			if (source == newGameButton) {
				sound.stop();
				GameDirector.sharedSceneListener().requestScene(SceneID.PARTYCREATION, MainMenuScene.this);
			}
		}
		
	}
}
