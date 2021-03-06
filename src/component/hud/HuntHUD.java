
package component.hud;
/*
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
*/


import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

import component.Button;
//import component.Component;
import component.Label;
import component.Panel;
import component.Label.VerticalAlignment;
//import component.Positionable.ReferencePoint;
import component.sprite.Sprite;

import core.ConstantStore;
import core.FontStore;
import core.ImageStore;


/**
 * the hud for the hunt scene
 */
public class HuntHUD extends HUD {
	private static final int MARGIN = 10;
	
	private static final int BUTTON_HEIGHT = HEIGHT - (2 * MARGIN);
	
	private static final int INFO_WIDTH = 200;
	
	private Button hudButton1;
	private Button hudButton2;
	

//	private Label timeLabel;
//	private Label dateLabel;
	private Label notificationLabel;
	
	private Panel huntPanel;
	
//	private Queue<String> notificationQueue;

	/**
	 * 
	 * @param context
	 * @param listener
	 */
	public HuntHUD(GUIContext context, ComponentListener listener) {
		super(context, context.getWidth(), HEIGHT);
	
		int panelWidth = context.getWidth() - INFO_WIDTH - MARGIN * 2;
		
		huntPanel = makeHuntPanel(panelWidth, listener);
		
		add(huntPanel, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT);
	

	}

	public Panel makeHuntPanel(int width, ComponentListener listener) {
		Panel panel = new Panel(container, width, HEIGHT);
		
		Font fieldFont = FontStore.get().getFont(FontStore.FontID.FIELD);
		
		Label menuLabel = new Label(container, fieldFont, Color.white, ConstantStore.get("HUNT_SCENE", "CAMP"));
		
		Sprite fireSprite = new Sprite(container, 48, ImageStore.get().getImage("CAMP_ICON"));
		Sprite inventorySprite = new Sprite(container, 48, ImageStore.get().getImage("INVENTORY_ICON"));
		
		hudButton1 = new Button(container, menuLabel.getWidth() + (2 * MARGIN), BUTTON_HEIGHT, menuLabel);
		hudButton1.setSprite(fireSprite);
		hudButton1.setShowLabel(false);
		hudButton1.addListener(listener);
		panel.add(hudButton1, getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, MARGIN, MARGIN);
		
		hudButton1.setTooltipEnabled(true);
		hudButton1.setTooltipMessage(ConstantStore.get("HUNT_SCENE", "CAMP"));
		
		int notificationWidth = width - hudButton1.getWidth() -  MARGIN * 2;
		
		notificationLabel = new Label(container, notificationWidth, BUTTON_HEIGHT, fieldFont, Color.white, "");
		notificationLabel.setVerticalAlignment(VerticalAlignment.CENTER);
		notificationLabel.setBackgroundColor(Color.black);
		panel.add(notificationLabel, hudButton1.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, MARGIN, 0);

		hudButton2 = new Button(container, menuLabel.getWidth() + (2 * MARGIN), BUTTON_HEIGHT, menuLabel);
		hudButton2.setSprite(inventorySprite);
		hudButton2.setShowLabel(false);
		hudButton2.addListener(listener);
		panel.add(hudButton2, notificationLabel.getPosition(ReferencePoint.TOPRIGHT), ReferencePoint.TOPLEFT, MARGIN, 0);
		
		hudButton2.setTooltipEnabled(true);
		hudButton2.setTooltipMessage(ConstantStore.get("HUNT_SCENE", "INVENTORY"));
		
		return panel;
	} 
	
	/**
	 * Get the camp button.
	 * @return camp button
	 */
	public Button getCampButton() {
		return hudButton1;
	}
	/**
	 * Get the inventory button.
	 * @return inventory button
	 */
	public Button getInventoryButton() {
		return hudButton2;
	}
	
	/**
	 * Adds a notification onto the queue.
	 * @param message New message to show
	 */
	public void setNotification(String message) {
		notificationLabel.setText(message);
	}
}
