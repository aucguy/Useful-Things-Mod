package com.aucguy.usefulthings.grave;

import com.aucguy.usefulthings.util.ComboIter;
import com.aucguy.usefulthings.util.Range;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * the container for a grave
 */
public class ContainerGrave extends Container implements IInvBasic, IGraveContainer {
	/**
	 * a special slot for armor. Taken from vanilla's ContainerPlayer
	 */
	public class ArmorSlot extends Slot {
		EntityPlayer player;
		int armorSlot;

		public ArmorSlot(IInventory inventory, EntityPlayer player, int armorSlot, int xPosition, int yPosition) {
			super(inventory, player.inventory.getSizeInventory() - 1 - armorSlot, xPosition, yPosition);
			this.player = player;
			this.armorSlot = armorSlot;
		}

		public int getSlotStackLimit() {
			return 1;
		}

		public boolean isItemValid(ItemStack stack) {
			if(stack == null)
				return false;
			return stack.getItem().isValidArmor(stack, this.armorSlot, this.player);
		}

		@SideOnly(Side.CLIENT)
		public String getSlotTexture() {
			return ItemArmor.EMPTY_SLOT_NAMES[this.armorSlot];
		}
	}

	/**
	 * the left side of the main inventory
	 */
	public static final int MAIN_LEFT = 28;
	/**
	 * the top side of the main inventory
	 */
	public static final int MAIN_TOP = 28;
	/**
	 * the height of an inventory
	 */
	public static final int SECTION_DIFF = 82;
	/**
	 * the top of the hot bar
	 */
	public static final int HOT_BAR_TOP = 58;
	/**
	 * the width of the main inventory
	 */
	public static final int INVENTORY_WIDTH = 9;
	/**
	 * the height of the main inventory excluding the hot bar
	 */
	public static final int INVENTORY_HEIGHT = 3;
	/**
	 * the width and the height of a slot
	 */
	public static final int SLOT_DIMENSION = 18;
	/**
	 * how many slots are in an inventory
	 */
	public static final int INVENTORY_SIZE = INVENTORY_WIDTH * (INVENTORY_HEIGHT + 1) + 4;
	/**
	 * how many slots are in a main inventory
	 */
	public static final int MAIN_INVENTORY = INVENTORY_SIZE - 4;

	/**
	 * the inventory belonging to the player who open the container
	 */
	public IInventory inventoryAlive;
	/**
	 * the inventory of the person who died when the died
	 */
	public IInventory inventoryDead;
	/**
	 * the grave tileentity
	 */
	TileEntityGrave tileEntity;

	public ContainerGrave(EntityPlayer player, InventoryGrave dead, TileEntityGrave tileEntity) {
		this.inventoryAlive = player.inventory;
		this.inventoryDead = dead;
		this.tileEntity = tileEntity;
		dead.func_110134_a(this);
		this.addSlots(player);
	}

	/**
	 * adds the necessary slots to this container
	 * 
	 * @param player - the player who opened this container
	 */
	protected void addSlots(EntityPlayer player) {
		int mainTop = MAIN_TOP;
		int hotBarTop;
		IInventory inventory = this.inventoryDead;

		for(int i = 0; i < 2; i++) {
			hotBarTop = mainTop + HOT_BAR_TOP;
			if(inventory != null) {
				for(int x = 0; x < 9; ++x) {
					this.addSlotToContainer(new Slot(inventory, x, MAIN_LEFT + x * SLOT_DIMENSION, hotBarTop));
				}

				for(int y = 0; y < INVENTORY_HEIGHT; ++y) {
					for(int x = 0; x < INVENTORY_WIDTH; ++x) {
						this.addSlotToContainer(new Slot(inventory, x + y * INVENTORY_WIDTH + INVENTORY_WIDTH,
								MAIN_LEFT + x * SLOT_DIMENSION, mainTop + y * SLOT_DIMENSION));
					}
				}

				for(int y = 0; y < 4; ++y) {
					this.addSlotToContainer(new ArmorSlot(inventory, player, y, 8, mainTop + y * SLOT_DIMENSION));
				}
				mainTop += SECTION_DIFF;
				inventory = this.inventoryAlive;
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	/**
	 * called when the player shift-clicks an item
	 * 
	 * @param playerIn - the player
	 * @param index - the slot index clicked
	 * @returns the item stack
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			boolean upperInventory = index < INVENTORY_SIZE;

			// try to put in armor
			if(itemstack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) itemstack.getItem();
				int armorIndex = MAIN_INVENTORY + armor.armorType;
				if(!this.getSlot(armorIndex + (upperInventory ? INVENTORY_SIZE : 0)).getHasStack()) {
					if(this.mergeStack(slot, itemstack, armorIndex, armorIndex + 1, upperInventory)) {
						return itemstack;
					}
				}
			}

			// main inventory
			if(this.getSlot(index).getHasStack()) {
				if(this.mergeStack(slot, itemstack, 0, 36, upperInventory)) {
					return itemstack;
				}
			}
			return null;
		}

		return itemstack;
	}

	/**
	 * merges two a stack into another
	 * 
	 * @param slot - the slot where the stack came from
	 * @param stack - the item stack
	 * @param startingIndex - the index to start from relative to the of an inventory
	 * @param endingIndex - the index to end with relative to the of an inventory
	 * @param upper - whether or not if the stack came from the upper or lower inventory
	 * @return
	 */
	protected boolean mergeStack(Slot slot, ItemStack stack, int startingIndex, int endingIndex, boolean upper) {
		if(upper) {
			startingIndex += INVENTORY_SIZE;
			endingIndex += INVENTORY_SIZE;
		}
		boolean set = this.mergeItemStack(stack, startingIndex, endingIndex, false);
		if(stack.stackSize == 0) {
			slot.putStack(null);
		} else {
			slot.onSlotChanged();
		}
		return set;
	}

	/**
	 * marks the tileentity dirty
	 */
	@Override
	public void onInventoryChanged(InventoryBasic inventory) {
		if(inventory instanceof InventoryGrave) {
			this.tileEntity.markDirty();
		}
	}
	
	@Override
	public boolean isValidItemForSlot(ItemStack stack, int slot, IInventory inventory) {
		return this.getSlot(slot).isItemValid(stack);
	}

	@Override
	public void doSwapAction() {
		InventoryUtil.swapInventories(this.inventoryAlive, this.inventoryDead);
	}
	
	@Override
	public void doTakeAction() {
		InventoryUtil.combineInventories(this.inventoryAlive, this.inventoryDead, new ComboIter<Integer>(new Range(36, 40), new Range(0, 36)));
	}
}
