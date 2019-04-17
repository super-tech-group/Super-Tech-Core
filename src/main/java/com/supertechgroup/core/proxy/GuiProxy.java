package com.supertechgroup.core.proxy;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.machinery.basicsmelter.ContainerBasicSmelter;
import com.supertechgroup.core.machinery.basicsmelter.GuiBasicSmelter;
import com.supertechgroup.core.machinery.basicsmelter.TileEntityBasicSmelter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		System.out.println("attempting to open gui for " + te.writeToNBT(new NBTTagCompound()));
		switch (ID) {
		case Reference.BASIC_SMELTER_ID:
			TileEntityBasicSmelter tile = (TileEntityBasicSmelter) te;
			System.out.println("Opening basic smelter");
			return new GuiBasicSmelter(tile, new ContainerBasicSmelter(player.inventory, tile));
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityBasicSmelter) {
			return new ContainerBasicSmelter(player.inventory, (TileEntityBasicSmelter) te);
		}
		return null;
	}
}