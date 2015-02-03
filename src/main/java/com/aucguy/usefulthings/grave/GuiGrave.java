package com.aucguy.usefulthings.grave;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import com.aucguy.usefulthings.ModUsefulThings;
import com.aucguy.usefulthings.network.PacketGui;

/**
 * the gui for a grave
 */
public class GuiGrave extends GuiContainer {

	/**
	 * the background texture
	 */
	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("usefulthings:gui/grave.png");
	/**
	 * the gui id for this gui. Used in networking
	 */
	public static final int GUI_ID = 0;
	
	public static final int BUTTON_TOP = 5;
	public static final int BUTTON_LEFT = 5;

	/**
	 * the buttons
	 */
	protected GuiButton swapButton;
	protected GuiButton takeButton;

	public GuiGrave(Container container) {
		super(container);
		this.xSize = 196;
		this.ySize = 192;
	}

	public void initGui() {
		super.initGui();
		int k = (this.width - this.xSize) / 2 + BUTTON_TOP;
		int l = (this.height - this.ySize) / 2 + BUTTON_LEFT;
		this.swapButton = new GuiButton(0, k, l, 40, 20, "Swap");
		this.buttonList.add(this.swapButton);
		this.takeButton = new GuiButton(1, k+45, l, 40, 20, "Take");
		this.buttonList.add(this.takeButton);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if(button == this.swapButton) {
			ModUsefulThings.instance.networkHandler.sendToServer(new PacketGui(PacketGui.Action.SWAP));
		}
		else if(button == this.takeButton) {
			ModUsefulThings.instance.networkHandler.sendToServer(new PacketGui(PacketGui.Action.TAKE));
		}
	}
}