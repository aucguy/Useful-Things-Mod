package com.aucguy.usefulthings.grave;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * utilities that handle grave operations
 * 
 * @author aucguy
 */
public class InventoryUtil {
	/**
	 * inventory handlers for vanilla classes or any inventory that doesn't implement IInventoryHandler
	 */
	public static Map<Class<?>, IInventoryHandler> inventoryHandlers;

	static {
		registerInventoryHandlers();
	}

	/**
	 * registers the needed inventory handlers for vanilla minecraft
	 */
	protected static void registerInventoryHandlers() {
		inventoryHandlers = new HashMap<Class<?>, IInventoryHandler>();
		inventoryHandlers.put(InventoryPlayer.class, new PlayerInventoryHandler());
	}

	/**
	 * swaps two items. TODO make this not ignore items going into invalid slots
	 * 
	 * @param inventory1 - the inventory of the first item
	 * @param slot1 - the index of the first item (in the inventory, not the container)
	 * @param inventory2 - the inventory of the second item
	 * @param slot2 - the index of the second item (in the inventory, not the container)
	 */
	public static void swapItems(IInventory inventory1, int slot1, IInventory inventory2, int slot2) {
		ItemStack item1 = inventory1.getStackInSlot(slot1);
		ItemStack item2 = inventory2.getStackInSlot(slot2);
		if(!(getInventoryHandler(inventory1).isValidItem(item2, slot1) && getInventoryHandler(inventory2).isValidItem(item1, slot2))) {
			return;
		}
		inventory2.setInventorySlotContents(slot2, item1);
		inventory1.setInventorySlotContents(slot1, item2);
	}

	/**
	 * moves an item stack into another slot, if the other slot is not occupied
	 * 
	 * @param outof - the inventory of the moving item
	 * @param outofIndex - the index of the moving item (in the inventory, not the container)
	 * @param into - the inventory of the new location
	 * @param intoIndex - the index of the new location (in the inventory, not the container)
	 */
	public static void moveItem(IInventory outof, int outofIndex, IInventory into, int intoIndex) {
		ItemStack stack1 = outof.getStackInSlot(outofIndex);
		ItemStack stack2 = into.getStackInSlot(intoIndex);

		if(stack1 == null && stack2 != null) {
			swapItems(outof, outofIndex, into, intoIndex);
		}
	}

	/**
	 * swaps two inventories. If the length of the inventories are unequal, nothing happens
	 */
	public static void swapInventories(IInventory inventory1, IInventory inventory2) {
		if(inventory1.getSizeInventory() != inventory2.getSizeInventory()) {
			return;
		}

		for(int slot = 0; slot < inventory1.getSizeInventory(); slot++) {
			InventoryUtil.swapItems(inventory1, slot, inventory2, slot);
		}
	}

	/**
	 * combines two inventories (like when the 'take' button is pressed). If the length of the inventories are unequal
	 * nothing happens.
	 * 
	 * @param into - the inventory the items are going into
	 * @param outof - the inventory the items are going outof
	 * @param order - the order in which the slots are moved orginally, not in the second pass where the 'overflow' is
	 *            moved
	 */
	public static void combineInventories(IInventory into, IInventory outof, Iterable<Integer> order) {
		if(into.getSizeInventory() != outof.getSizeInventory()) {
			return;
		}

		for(int index : order) {
			InventoryUtil.moveItem(into, index, outof, index);
		}

		for(int index = 0; index < into.getSizeInventory(); index++) {
			if(outof.getStackInSlot(index) != null) {
				for(int index2 = 0; index2 < into.getSizeInventory(); index2++) {
					InventoryUtil.moveItem(into, index2, outof, index);
				}
			}
		}
	}
	
	/**
	 * returns the inventory handler for the given inventory
	 */
	public static IInventoryHandler getInventoryHandler(IInventory inventory) {
		if(inventory instanceof IInventoryHandler) {
			return (IInventoryHandler) inventory;
		}

		Class<?> clazz = inventory.getClass();
		while(clazz != Object.class) {
			if(inventoryHandlers.containsKey(clazz)) {
				return inventoryHandlers.get(clazz);
			}
			clazz = clazz.getSuperclass();
		}
		return inventoryHandlers.get(clazz);
	}
}
