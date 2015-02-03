package com.aucguy.usefulthings.grave;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryGrave extends InventoryBasic implements IInventoryHandler {
	public InventoryGrave(String title, boolean customName, int slotCount) {
		super(title, customName, slotCount);
	}

	/**
	 * reads inventory from NBT. doesn't override any items that aren't specified in the list
	 * 
	 * @param list - the compound to read from
	 */
	public void readFromNBT(NBTTagList list) {
		// taken and modified from InventoryPlayer
		for(int i = 0; i < list.tagCount(); ++i) {
			this.readItem(list.getCompoundTagAt(i));
		}
	}

	/**
	 * writes the inventory to NBT
	 * 
	 * @param list - the compound to write to
	 * @return - the compound to write to
	 */
	public NBTTagList writeToNBT(NBTTagList list) {
		// taken and modified from InventoryPlayer

		for(int i = 0; i < this.getSizeInventory(); ++i) {
			this.writeItem(list, i);
		}
		return list;
	}

	/**
	 * reads an item from NBT
	 * 
	 * @param compound - the serialized item
	 */
	public void readItem(NBTTagCompound compound) {
		int j = compound.getByte("Slot") & 255;
		ItemStack itemstack = ItemStack.loadItemStackFromNBT(compound);
		this.setInventorySlotContents(j, itemstack);
	}

	/**
	 * writes an item to NBT
	 * 
	 * @param list - the to-be serialized list of this inventory
	 * @param slot - the slot to serialize
	 */
	public void writeItem(NBTTagList list, int slot) {
		if(this.getStackInSlot(slot) != null) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Slot", (byte) slot);
			this.getStackInSlot(slot).writeToNBT(nbttagcompound);
			list.appendTag(nbttagcompound);
		}
	}

	@Override
	public boolean isValidItem(ItemStack stack, int index) {
		return PlayerInventoryHandler.isValidItemForPlayer(stack, index);
	}
}
