package com.aucguy.usefulthings.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketGui implements IMessage {
	public enum Action {
		SWAP, TAKE, RECIEVE;
	}

	/**
	 * what button was pressed
	 */
	Action action;

	public PacketGui() {

	}

	public PacketGui(Action action) {
		this.action = action;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.action = Action.values()[buf.readByte()];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.action.ordinal());
	}
}
