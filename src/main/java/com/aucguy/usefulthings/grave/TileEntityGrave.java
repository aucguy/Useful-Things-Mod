package com.aucguy.usefulthings.grave;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.aucguy.usefulthings.ModUsefulThings;

/**
 * the tileentity for a grave
 */
public class TileEntityGrave extends TileEntity {
	/**
	 * the inventory from the dead player
	 * TODO make this a custom inventory type
	 */
	public InventoryGrave inventory;
	
	public TileEntityGrave() {
		this(null);
	}
	
	TileEntityGrave(InventoryGrave inventory) {
		if(this.inventory == null) {
			this.inventory = new InventoryGrave("", false, ContainerGrave.INVENTORY_SIZE);
		}
		else {
			this.inventory = inventory;
		}
	}
	
	/**
	 * called when this gets right-clicked
	 */
	void onActivated(World world, BlockPos pos, EntityPlayer player) {
		player.openGui(ModUsefulThings.instance, GuiGrave.GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * reads the tileenitty from NBT
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTTagList inventory = compound.getTagList("inventory", 10);
		this.inventory.readFromNBT(inventory);
	}
	
	/**
	 * writes the tileentity to NBT
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("inventory", this.inventory.writeToNBT(new NBTTagList()));
	}
	
	/**
	 * returns a container for this tileentity
	 */
	public ContainerGrave getContainer(EntityPlayer player) {
		return new ContainerGrave(player, this.inventory, this); 
	}
}
