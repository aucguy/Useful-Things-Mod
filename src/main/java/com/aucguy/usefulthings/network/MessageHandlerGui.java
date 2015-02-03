package com.aucguy.usefulthings.network;

import com.aucguy.usefulthings.grave.ContainerGrave;

import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * handles gui packets. Sent from client when the swap button is pressed in the grave gui
 */
public class MessageHandlerGui implements IMessageHandler<PacketGui, IMessage> {
	@Override
	public IMessage onMessage(PacketGui message, MessageContext ctx) {
		Container container = ctx.getServerHandler().playerEntity.openContainer;
		if(container instanceof ContainerGrave) {
			ContainerGrave grave = (ContainerGrave) container;
			if(message.action == PacketGui.Action.SWAP) {
				grave.doSwapAction();
			}
			else if(message.action == PacketGui.Action.TAKE) {
				grave.doTakeAction();
			}
		}
		return null;
	}
}
