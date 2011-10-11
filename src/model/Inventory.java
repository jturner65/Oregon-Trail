package model;

import java.util.ArrayList;
import java.util.PriorityQueue;

import core.Logger;

/**
 * Inventory with an ArrayList that holds all the arrayLists of items in the inventory.
 */
public class Inventory {
	private ArrayList<PriorityQueue<Item>> slots;
	private final int MAX_SIZE;
	private final double MAX_WEIGHT;
	private int currentSize;

	/**
	 * Inventory starts out empty.
	 */
	public Inventory(int maxSize, double maxWeight) {
		this.MAX_SIZE = maxSize;
		this.MAX_WEIGHT = maxWeight;
		this.slots = new ArrayList<PriorityQueue<Item>>(ConstantStore.ITEM_TYPE.values().size();
		this.currentSize = 0;
	}
	
	/**
	 * Returns the max number of items we can hold.
	 * @return Max number of items to hold
	 */
	public int getMaxSize() {
		return MAX_SIZE;
	}
	
	/**
	 * Returns the slots in inventory.
	 * @return The slots in the inventory.
	 */
	public ArrayList<PriorityQueue<Item>> getSlots() {
		return slots;
	}
	
	/**
	 * Returns the current size of the inventory (number of item queues with contents)
	 * @return The size of the inventory
	 */
	public int getCurrentSize() {
		return currentSize;
	}
	
	/**
	 * Calculates and returns the weight of the inventory.
	 * @return The weight of the inventory.
	 */
	public double getWeight() {
		double weight = 0;
		for(PriorityQueue<Item> slot : slots) {
			for(Item item : slot) {
				weight += item.getWeight();
			}
		}
		return weight;
	}

	public boolean canAddItems(ArrayList<Item> itemsToAdd) {
		ConstantStore.ITEM_TYPE itemType = itemsToAdd.get(0).getType();
		double weight = 0;
		for(Item item : itemsToAdd) {
			weight += item.getWeight();
		}
		if(getWeight() + weight > MAX_WEIGHT) {
			Logger.log("Not enough weight capacity", Logger.Level.INFO);
			return false;
		} else if (currentSize == MAX_SIZE && slots.get(itemType).size() == 0) {
			Logger.log("Not enough slots open", Logger.Level.INFO);
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Adds the item to the inventory.
	 * @param item The item to add
	 * @return True if successful, false otherwise
	 */
	public boolean addItem(ArrayList<Item> itemsToAdd) {
		ConstantStore.ITEM_TYPE itemType = itemsToAdd.get(0).getType();		
		if(canAddItems(itemsToAdd)) {
			for(Item item : itemsToAdd) {
				slots.get(itemType).add(item);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes the item from the inventory.
	 * @param item The item to remove
	 * @return True if successful, false otherwise
	 * */
	public ArrayList<Item> removeItem(ConstantStore.ITEM_TYPE itemType, int quantity) {
		ArrayList<Item> removedItems = new ArrayList<Item>();
		if(slots.get(itemType).size() < quantity) {
			Logger.log("Not enough items to remove", Logger.Level.INFO);
			removedItems = null;
		} else {
			for(int i = 0; i < quantity; i++) {
				removedItems.add(slots.get(itemType).pop());
			}
		}
		return removedItems;
	}
	/**
	 * Checks to see if the current inventory is full.
	 * @return True if the inventory is full, false otherwise.
	 */
	public boolean isFull() {
		return (slots.size() == MAX_SIZE);
	}
}
