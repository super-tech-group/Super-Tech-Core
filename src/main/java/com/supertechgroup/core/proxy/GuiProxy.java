package com.supertechgroup.core.proxy;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.machinery.multiblock.CrudeInputContainer;
import com.supertechgroup.core.machinery.multiblock.CrudeInputGui;
import com.supertechgroup.core.machinery.multiblock.CrudeInputTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		switch (ID) {
		case Reference.GUI_CRUDE_INPUT:
			CrudeInputTileEntity tile = (CrudeInputTileEntity) te;
			return new CrudeInputGui(tile, new CrudeInputContainer(player.inventory, tile));
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		switch (ID) {
		case Reference.GUI_CRUDE_INPUT:
			return new CrudeInputContainer(player.inventory, (CrudeInputTileEntity) tile);
		}
		return null;
	}
}