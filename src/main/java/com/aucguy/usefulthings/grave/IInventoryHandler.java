package com.aucguy.usefulthings.grave;

import net.minecraft.item.ItemStack;

/**
 * provides information about an inventory
 * 
 * @author aucguy
 */
public interface IInventoryHandler {
	/**
	 * returns whether or not an itemstack can be placed in a certain slot
	 * @param stack - the item stack
	 * @param index - the slot relative to the beginning of the inventory
	 * @return - whether or not an itemstack can be placed in a certain slot 
	 */
	boolean isValidItem(ItemStack stack, int index);
}
