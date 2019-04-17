package com.supertechgroup.core.machinery.basicsmelter;

import com.supertechgroup.core.Reference;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiBasicSmelter extends GuiContainer {

	public static final int WIDTH = 176;
	public static final int HEIGHT = 152;
	private static final ResourceLocation background = new ResourceLocation(Reference.MODID,
			"textures/gui/basic_smelter.png");
	private ContainerBasicSmelter container;

	public GuiBasicSmelter(TileEntityBasicSmelter tileEntity, ContainerBasicSmelter container) {
		super(container);
		this.container = container;

		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;

		if (TileEntityBasicSmelter.isBurning(container.te)) {

			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = container.te.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		// this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(),
		// 8, this.ySize - 96 + 2, 4210752);
	}

	private int getCookProgressScaled(int pixels) {
		int i = container.te.getField(0);
		int j = container.te.getField(1);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private int getBurnLeftScaled(int pixels) {
		int i = container.te.getField(1);

		if (i == 0) {
			i = 200;
		}

		return container.te.getField(0) * pixels / i;
	}
}
