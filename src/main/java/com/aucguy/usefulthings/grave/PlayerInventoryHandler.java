package com.aucguy.usefulthings.grave;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * the inventory handler vanilla's player inventory
 * 
 * @author aucguy
 */
public class PlayerInventoryHandler implements IInventoryHandler {
	@Override
	public boolean isValidItem(ItemStack stack, int index) {
		return isValidItemForPlayer(stack, index);
	}
	
	public static boolean isValidItemForPlayer(ItemStack stack, int index) {
		if(index < 36 || stack == null) { //main inventory
			return true;
		}
		else if(stack.getItem() instanceof ItemArmor) { //armor
			return ((ItemArmor) stack.getItem()).armorType + 37 == index;
		}
		return false; //not armor and armor slot
	}
}
