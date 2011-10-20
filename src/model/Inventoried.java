package model;

import java.util.List;

public interface Inventoried {
	
	/**
	 * Adds the items to inventory.
	 * @param itemsToAdd The items to add to the inventory
	 * @return True if successful
	 */
	boolean addItemToInventory(List<Item> itemsToAdd);
	
	/**
	 * Adds a single item to inventory.
	 * @param item The items to add to the inventory
	 * @return True if successful
	 */
	boolean addItemToInventory(Item item);
	
	/**
	 * Removes the item from inventory.
	 * @param itemType The type of item to remove
	 * @param quantity The number of items to remove
	 * @return True if successful
	 */
	List<Item> removeItemFromInventory(Item.ITEM_TYPE itemType, int quantity);
	
	/**
	 * Returns whether or not the number of a specific item type 
	 * can be added to the inventory.
	 * @param itemType The type of item to test with
	 * @param numberOf The number of items to test with
	 * @return True if successful.
	 */
	boolean canGetItem(Item.ITEM_TYPE itemType, int numberOf);
	
	/**
	 * The name of the inventoried person.
	 * @return The name
	 */
	String getName();
	
	/**
	 * The inventory of the inventoried person.
	 * @return The inventory
	 */
	Inventory getInventory();
	
	/**
	 * The max size of the inventory.
	 * @return The max size.
	 */
	int getMaxSize();
	
	/**
	 * Returns the current carried weight
	 * @return The current carried weight
	 */
	double getWeight();
	
	/**
	 * Returns the max weight of the inventory
	 * @return The max weight
	 */
	double getMaxWeight();
}
