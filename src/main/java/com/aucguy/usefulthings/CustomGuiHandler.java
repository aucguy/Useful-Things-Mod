package com.aucguy.usefulthings;

import com.aucguy.usefulthings.grave.GuiGrave;
import com.aucguy.usefulthings.grave.TileEntityGrave;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CustomGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (ID) {
		case (GuiGrave.GUI_ID):
			TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
			if(tileentity instanceof TileEntityGrave) {
				return ((TileEntityGrave) tileentity).getContainer(player);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (ID) {
		case (GuiGrave.GUI_ID):
			TileEntity tileentity = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(x, y, z));
			if(tileentity instanceof TileEntityGrave) {
				return new GuiGrave(((TileEntityGrave) tileentity).getContainer(player));
			}
		}
		return null;
	}
}
