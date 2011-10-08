package scene.test;

import model.Person;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.*;

import scene.Scene;
import scene.SceneID;

import component.*;
import core.*;

/**
 * Scene where all components are tested.
 * 
 * @author Tanner Smith
 */
public class ComponentTestScene extends Scene {
	public static final SceneID ID = SceneID.ComponentTest;
	private static final int PADDING = 20;

	private TextField textField;
	private Button button;
	private Label label, spinnerLabel;
	private Spinner spinner;
	private SegmentedControl segment;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
				
		Font fieldFont = GameDirector.sharedSceneDelegate().getFontManager().getFont(FontManager.FontID.FIELD);

		Label textFieldLabel = new Label(container, 300, fieldFont, Color.white, "Text Field");
		mainLayer.add(textFieldLabel, mainLayer.getPosition(Positionable.ReferencePoint.TopLeft), Positionable.ReferencePoint.TopLeft, PADDING, PADDING);
		
		textField = new TextField(container, 300, 40, fieldFont);
		textField.addListener(new ButtonListener());
		mainLayer.add(textField, textFieldLabel.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft);
		
		Label buttonLabel = new Label(container, fieldFont, Color.white, "Button");
		mainLayer.add(buttonLabel, textField.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		
		button = new Button(container, new Label(container, fieldFont, Color.white, "my button"), 300, 40);
		button.addListener(new ButtonListener());
		mainLayer.add(button, buttonLabel.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft);
		
		label = new Label(container, 300, fieldFont, Color.white, "Label");
		mainLayer.add(label, button.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
	
		spinner = new Spinner(container, 300, 50, fieldFont, Color.white, false, "Hello","There","Encyclopedia");
		mainLayer.add(spinner, label.getPosition(Positionable.ReferencePoint.BottomLeft),Positionable.ReferencePoint.TopLeft, 0, PADDING);
	
		spinnerLabel = new Label(container, fieldFont, Color.white, "Label");
		mainLayer.add(spinnerLabel, spinner.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		Person.Skill[] arr = Person.Skill.values();
		String[] strs = new String[arr.length - 2];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = arr[i].getName();
		}	

		segment = new SegmentedControl(container,500,100,4,3,0,4,strs);

		mainLayer.add(segment, spinnerLabel.getPosition(Positionable.ReferencePoint.BottomLeft), Positionable.ReferencePoint.TopLeft, 0, PADDING);
		int[] num = {2,3};
		segment.setPermanent(num);
		
		Component colLabels[] = new Component[5];
		for (int i = 0; i < colLabels.length; i++) {
			colLabels[i] = new Label(container, fieldFont, Color.white, "Label "+i);
		}
		//mainLayer.addAsColumn(colLabels, mainLayer.getPosition(Positionable.ReferencePoint.TopCenter), 0, PADDING, PADDING);
		
		Component rowLabels[] = new Component[5];
		for (int i = 0; i < rowLabels.length; i++) {
			rowLabels[i] = new Label(container, fieldFont, Color.white, "Label "+i);
		}
		//mainLayer.addAsRow(rowLabels, colLabels[colLabels.length - 1].getPosition(Positionable.ReferencePoint.TopLeft), 0, colLabels[colLabels.length - 1].getHeight() + PADDING, PADDING);
		
		Component gridLabels[] = new Component[9];
		for (int i = 0; i < gridLabels.length; i++) {
			gridLabels[i] = new Label(container, fieldFont, Color.white, "Label "+i);
		}
		mainLayer.addAsGrid(gridLabels, mainLayer.getPosition(Positionable.ReferencePoint.TopCenter), 3, 3, 0, PADDING, PADDING, PADDING);
		
		ToggleButton tb = new ToggleButton(container, new Label(container, fieldFont, Color.white, "Toggle Button"));
		mainLayer.add(tb, mainLayer.getPosition(Positionable.ReferencePoint.BottomRight), Positionable.ReferencePoint.BottomRight, 0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
	}
	
	private class ButtonListener implements ComponentListener {
		public void componentActivated(AbstractComponent source) {
			label.setText("TextField has \""+textField.getText()+"\"");
			spinnerLabel.setText("Spinner has \"" + spinner.getText() + "\"");
			modalLayer.add(new Panel(container, new Color(0.0f, 0.0f, 0.0f, 0.25f)));
		}
	}

	
	@Override
	public int getID() {
		return ID.ordinal();
	}
}
