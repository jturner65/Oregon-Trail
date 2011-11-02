package model.item;

import java.util.ArrayList;
import java.util.List;

import component.PartyComponentDataSource;
import core.ConstantStore;

import model.Item;

public class Animal extends Item implements PartyComponentDataSource {

	private double moveFactor;
	
	private boolean dead;
	
	public Animal(ITEM_TYPE type) {
		super(type);
		this.moveFactor = Double.parseDouble(ConstantStore.get("ITEMS", type + "_MOVE_FACTOR"));
	}
	
	public List<Item> killForFood() {
		int numberOf = (int) (this.getWeight() / ITEM_TYPE.MEAT.getWeight());
		List<Item> itemList = new ArrayList<Item>();
		for(int i = 0; i < (numberOf / 10); i++) {
			itemList.add(new Item(ITEM_TYPE.MEAT));
		}
		return itemList;
	}
	
	public double getMoveFactor() {
		return moveFactor;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	@Override
	public boolean isDead() {
		return dead;
	}
}
