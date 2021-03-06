package component;

import model.Condition;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

/**
 * A group consisting of a {@code Component} and {@code ConditionBar}.
 */
public class ComponentConditionGroup <T extends Component> extends Component {
	public static final int CONDITION_BAR_HEIGHT = 5;
	
	protected T component;
	protected ConditionBar conditionBar;
	
	/**
	 * Construct a {@code ComponentConditionGroup} with a {@code GUIContext},
	 * width, height, {@code Font}, {@code Component}, and a {@code Condition}.
	 * @param container The container for this {@code Component}
	 * @param width Width
	 * @param height Height
	 * @param conditionBarHeight Height for the {@code ConditionBar}
	 * @param component Component to use
	 * @param condition Condition for the {@code ConditionBar}
	 */
	public ComponentConditionGroup(GUIContext container, int width, int height, int conditionBarHeight, T component, Condition condition) {
		super(container, width, height);
		
		this.component = component;
		
		conditionBar = new ConditionBar(container, width, conditionBarHeight, condition);
		
		int padding = height - (component.getHeight() + conditionBar.getHeight());
		
		add(component, this.getPosition(ReferencePoint.TOPLEFT), ReferencePoint.TOPLEFT, 0, 0);
		add(conditionBar, component.getPosition(ReferencePoint.BOTTOMCENTER), ReferencePoint.TOPCENTER, 0, padding);
	}
	
	/**
	 * Construct a {@code ComponentConditionGroup} with a {@code GUIContext}, width, height, {@code Font}, {@code Component}, and a {@code Condition}.
	 * @param container The container for this {@code Component}
	 * @param width Width
	 * @param height Height
	 * @param component Component to use
	 * @param condition Condition for the {@code ConditionBar}
	 */
	public ComponentConditionGroup(GUIContext container, int width, int height, T component, Condition condition) {
		this(container, width, height, CONDITION_BAR_HEIGHT, component, condition);
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(container, g);
	}
	
	/**
	 * Set the {@code Condition} for the {@code ConditionBar}.
	 * @param condition New Condition
	 */
	public void setCondition(Condition condition) {
		conditionBar.setCondition(condition);
	}
	
	/**
	 * Returns the {@code Component}.
	 * @return The Component
	 */
	public T getComponent() {
		return component;
	}
	
	/**
	 * Set whether or not this component is disabled.
	 * @param disabled Disabled or not (true for disabled)
	 */
	public void setDisable(boolean disabled) {
		if (component instanceof Disableable) {
			((Disableable) component).setDisabled(disabled);
		} else {
			component.setVisible(!disabled);
		}
		
		conditionBar.setVisible(!disabled);
	}

	public void update() {
		conditionBar.update();
	}
}