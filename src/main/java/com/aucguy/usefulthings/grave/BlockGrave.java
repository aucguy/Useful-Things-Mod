package com.aucguy.usefulthings.grave;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockGrave extends BlockContainer {
	public BlockGrave() {
		super(Material.ground);
		this.setUnlocalizedName("grave");
		this.setHardness(100);
		this.setResistance(6000000.0F);
	}

	/**
	 * the arguement is null since the player isn't known here
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityGrave(null);
	}

	/**
	 * called when a player dies
	 * 
	 * @param player
	 *            - the player
	 */
	public void onPlayerDeath(EntityPlayer player) {
		BlockPos pos = new BlockPos(Math.round(player.posX), Math.round(player.posY), Math.round(player.posZ));
		player.worldObj.setBlockState(pos, this.getDefaultState());
		TileEntity tileentity = player.worldObj.getTileEntity(pos);
		if (tileentity instanceof TileEntityGrave) {
			TileEntityGrave grave = (TileEntityGrave) tileentity;
			InventoryGrave inventory = new InventoryGrave(player.getName(), true, ContainerGrave.INVENTORY_SIZE);
			for (int i = 0; i < ContainerGrave.INVENTORY_SIZE; i++) {
				inventory.setInventorySlotContents(i, player.inventory.getStackInSlotOnClosing(i));
			}
			grave.inventory = inventory;
			player.worldObj.markBlockForUpdate(pos);
			grave.markDirty();
		}
	}

	/**
	 * opens the gui
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityGrave) {
				((TileEntityGrave) tileentity).onActivated(world, pos, player);
			}
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		//taken from BlockChest
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityGrave) {
			InventoryHelper.dropInventoryItems(worldIn, pos, ((TileEntityGrave) tileentity).inventory);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public int getRenderType() {
		return 3;
	}
}
