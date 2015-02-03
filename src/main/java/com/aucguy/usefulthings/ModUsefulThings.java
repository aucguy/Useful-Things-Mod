package com.aucguy.usefulthings;

import com.aucguy.usefulthings.grave.BlockGrave;
import com.aucguy.usefulthings.grave.TileEntityGrave;
import com.aucguy.usefulthings.network.MessageHandlerGui;
import com.aucguy.usefulthings.network.PacketGui;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * the mod class
 */
@Mod(modid = ModUsefulThings.MODID, version = ModUsefulThings.VERSION, useMetadata = true)
public class ModUsefulThings {
	/**
	 * Modid and version constants
	 */
	public static final String MODID = "usefulthings";
	public static final String VERSION = "1.0";

	/**
	 * the mod instance
	 */
	@Instance
	public static ModUsefulThings instance;

	/**
	 * the grave block instance
	 */
	public BlockGrave graveBlock;

	/**
	 * the gui handler
	 */
	IGuiHandler guiHandler;
	
	/**
	 * the network handler
	 */
	public SimpleNetworkWrapper networkHandler;

	/**
	 * initialize everything
	 * 
	 * @param event
	 *            - the initialization event
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.graveBlock = new BlockGrave();
		this.registerBlock(this.graveBlock);
		GameRegistry.registerTileEntity(TileEntityGrave.class, "grave");
		this.guiHandler = new CustomGuiHandler();
		
		MinecraftForge.EVENT_BUS.register(this);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, this.guiHandler);
		
		this.networkHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		this.networkHandler.registerMessage(MessageHandlerGui.class, PacketGui.class, 0, Side.SERVER);
	}

	/**
	 * registers the given block
	 */
	public void registerBlock(Block block) {
		String name = block.getUnlocalizedName().substring(5);
		GameRegistry.registerBlock(block, name);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(this.graveBlock.getItem(null, null), 0, new ModelResourceLocation(MODID+":"+name, "inventory"));
	}
	
	/**
	 * hook for creating graves
	 */
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		if(event.entity instanceof EntityPlayerMP) {
			this.graveBlock.onPlayerDeath((EntityPlayer) event.entity);
		}
	}
}