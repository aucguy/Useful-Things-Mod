package com.aucguy.usefulthings.grave;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * a container that is used in a grave
 * TODO allow use of multiple containers in one
 * 
 * @author aucguy
 */
public interface IGraveContainer {
	/**
	 * called when two inventories are swapped. ie the 'swap' button is pressed
	 */
	void doSwapAction();

	/**
	 * called when two inventories are combined. ie the 'take' button is pressed
	 */
	void doTakeAction();

	/**
	 * returns whether or not the given item stack can be in the given slot
	 * 
	 * @param stack - the item stack
	 * @param slot - the slot
	 * @param inventory - the inventory
	 * @return whether or not the stack can be placed there
	 */
	boolean isValidItemForSlot(ItemStack stack, int slot, IInventory inventory);

}
