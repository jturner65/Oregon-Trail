package model;

import java.io.Serializable;

import core.ConstantStore;

/**
 * 
 * An item contains it's own condition as well as a 
 * name, description, and weight.  The only modifiable aspect
 * is weight.
 */
public class Item implements Conditioned, Comparable<Item>, Serializable{
	
	private final Condition status;
	
	private boolean isStackable = true;
	
	private final ITEM_TYPE type;
	
	public enum ITEM_TYPE {
		APPLE ("APPLE", true, true, false, false),
		BREAD ("BREAD", true, false, false, false),
		AMMO ("AMMO", false, false, false, false),
		GUN ("GUN", false, false, false, false),
		MEAT ("MEAT", true, false, false, false),
		SONIC ("SONIC", false, false, false, false),
		WAGON ("WAGON", false, false, false, false),
		WHEEL ("WHEEL", false, false, false, true),
		OX ("OX", false, false, true, false),
		TOOLS ("TOOLS", false, false, false, true),
		AXLE ("AXLE", false, false, false, true),
		HORSE ("HORSE", false, false, true, false),
		MULE ("MULE", false, false, true, false),
		STRANGEMEAT ("STRANGE_MEAT", true, false, false, false);
		
		private final String name;
		private final String pluralName;
	
		private final String description;
		
		private final int cost;
		
		private final double weight;
		
		private final boolean isFood, isPlant, isAnimal, isTool;
		
		private final int factor;
		private final int necessaryQuality;
				
		/**
		 * Makes the item type
		 * @param name Name of the item type
		 * @param description Description of the item type
		 * @param cost Cost of the item type
		 * @param weight Weight of the item type
		 * @param isFood If the item is food
		 * @param isPlant If the item is a plant
		 * @param foodFactor The multiplier for the food (if it is one, else 0)
		 */
		private ITEM_TYPE (String type, boolean isFood, boolean isPlant, boolean isAnimal, boolean isTool) {
			this.name = ConstantStore.get("ITEMS", type + "_NAME");
			this.pluralName = ConstantStore.get("ITEMS", type + "_PLURAL_NAME");
			
			this.description = ConstantStore.get("ITEMS", type + "_DESCRIPTION");
			this.cost = Integer.parseInt(ConstantStore.get("ITEMS", type + "_COST"));
			this.weight = Double.parseDouble(ConstantStore.get("ITEMS", type + "_WEIGHT"));
			this.isFood = isFood;
			this.isPlant = isPlant;
			this.isAnimal = isAnimal;
			this.isTool = isTool;
			if(isFood) {
				this.factor = Integer.parseInt(ConstantStore.get("ITEMS", type + "_FOOD_FACTOR"));
			} else if (isTool){
				this.factor = Integer.parseInt(ConstantStore.get("ITEMS", type + "_REPAIR_FACTOR"));
			} else {
				this.factor = 0;
			}
			if (ConstantStore.get("ITEMS", type + "_NECESSARY_QUALITY") != null) {
				this.necessaryQuality = Integer.parseInt(ConstantStore.get("ITEMS", type + "_NECESSARY_QUALITY"));
			} else {
				this.necessaryQuality = 0;
			}
		}
		
		/**
		 * The name of the type
		 * @return The name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * The plural name of the type
		 * @return The plural name
		 */
		public String getPluralName() {
			return pluralName;
		}
		
		/**
		 * The description of the type
		 * @return The description
		 */
		public String getDescription() {
			return description;
		}
		
		/**
		 * The cost of the type
		 * @return The cost
		 */
		public int getCost() {
			return cost;
		}
		
		/**
		 * The weight of the type
		 * @return The weight
		 */
		public double getWeight() {
			return weight;
		}
		
		/**
		 * Whether or not the item is food
		 * @return Is the item food?
		 */
		public boolean isFood() {
			return isFood;
		}
		
		/**
		 * Whether or not the item is a tool
		 * @return Is the item a tool?
		 */
		public boolean isTool() {
			return isTool;
		}
		
		/**
		 * The multiplier for the food
		 * @return The multiplier
		 */
		public int getFactor() {
			return factor;
		}
		
		/**
		 * Is the item a plant
		 * @return Is it a plant?
		 */
		public boolean isPlant() {
			return isPlant;
		}

		/**
		 * Is the item an animal?
		 * @return Is it an animal
		 */
		public boolean isAnimal() {
			return isAnimal;
		}

		public double getNecessaryQuality() {
			return necessaryQuality;
		}
	}

	/**
	 * 
	 * Creates a new item with a name, description, status, and weight.
	 * @param type The type of the item
	 */
	public Item(ITEM_TYPE type) {
		this.status = new Condition(100);
		this.type = type;
	}

	/**
	 * Returns the item's name.
	 * @return The item's name
	 */
	public String getName() {
		return type.getName();
	}

	/**
	 * Returns the item's description.
	 * @return The item's description
	 */
	public String getDescription() {
		return type.getDescription();
	}

	/**
	 * Returns the cost of the item.  Cost is the item multiplied
	 * by its current condition. 
	 * @return The base cost of the item
	 */
	public int getCost() {
		return (int) (type.getCost() * status.getPercentage());
	}
	
	/**
	 * Returns the current condition of the item.
	 * @return The current condition of the item.
	 */
	public Condition getStatus() {
		return status.copy();
	}
	
	@Override
	public double getConditionPercentage() {
		return status.getPercentage();
	}

	/**
	 * Increases the item's status by a specific amount. 
	 * Returns false if the increase fails.
	 * @param amount The amount by which to increase the status
	 */
	public void increaseStatus(int amount) {
		status.increase(amount);
		if (status.getCurrent() == status.getMax()) {
			this.isStackable = true;
		}
	}
	
	/**
	 * Decreases the item's status by a specific amount.  
	 * Returns false if the decrease fails.
	 * @param amount The amount by which to decrease the status
	 */
	public void decreaseStatus(int amount) {
		status.decrease(amount);
		if (status.getCurrent() < status.getMax()) {
			this.isStackable = false;
		}
	}
	
	/**
	 * Returns the weight of the item.
	 * @return The weight of the item
	 */
	public double getWeight() {
		return type.getWeight();
	}
	
	/**
	 * Whether or not the item can be stacked.
	 * @return True if it can be stacked, false otherwise.
	 */
	public boolean isStackable() {
		return isStackable;
	}
	
	@Override
	public int compareTo(Item i) {
		if (Math.abs(getConditionPercentage() - i.getConditionPercentage()) < 0.000001) {
			return 0;
		} else if (getConditionPercentage() < i.getConditionPercentage()) {
			return -1;
		} else {
			return 1;
		}
	}
	
	/**
	 * Returns the item type
	 * @return The item type
	 */
	public ITEM_TYPE getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type.getName();
	}

	@Override
	public Condition getCondition() {
		return status;
	}
}