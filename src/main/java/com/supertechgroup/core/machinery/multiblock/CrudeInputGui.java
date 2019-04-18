package com.supertechgroup.core.machinery.multiblock;

import java.awt.Color;

import com.supertechgroup.core.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CrudeInputGui extends GuiContainer {

	// This is the resource location for the background image for the GUI
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID,
			"textures/gui/crude_input.png");
	private CrudeInputTileEntity tile;

	public CrudeInputGui(CrudeInputTileEntity tile, CrudeInputContainer crudeInputContainer) {
		super(crudeInputContainer);
		this.tile = tile;
		// Set the width and height of the gui. Should match the size of the texture!
		xSize = 176;
		ySize = 133;
	}

	// draw the background for the GUI - rendered first
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		// Bind the image texture of our custom container
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	// draw the foreground for the GUI - rendered after the slots, but before the
	// dragged items and tooltips
	// renders relative to the top left corner of the background
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRenderer.drawString(tile.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS,
				Color.darkGray.getRGB());
	}
}