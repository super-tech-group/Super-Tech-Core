package com.supertechgroup.core.proxy;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.machinery.multiblock.crudeCoupling.CrudeCouplingContainer;
import com.supertechgroup.core.machinery.multiblock.crudeCoupling.CrudeCouplingGui;
import com.supertechgroup.core.machinery.multiblock.crudeCoupling.CrudeCouplingTileEntity;
import com.supertechgroup.core.machinery.multiblock.crudeheater.CrudeHeaterContainer;
import com.supertechgroup.core.machinery.multiblock.crudeheater.CrudeHeaterGui;
import com.supertechgroup.core.machinery.multiblock.crudeheater.CrudeHeaterTileEntity;
import com.supertechgroup.core.machinery.multiblock.crudeio.CrudeIOContainer;
import com.supertechgroup.core.machinery.multiblock.crudeio.CrudeIOGui;
import com.supertechgroup.core.machinery.multiblock.crudeio.CrudeIOTileEntity;

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
		case Reference.GUI_CRUDE_IO:
			CrudeIOTileEntity io = (CrudeIOTileEntity) te;
			System.out.println("Opening io");
			return new CrudeIOGui(io, new CrudeIOContainer(player.inventory, io));
		case Reference.GUI_CRUDE_HEATER:
			CrudeHeaterTileEntity heater = (CrudeHeaterTileEntity) te;
			System.out.println("Opening heater");
			return new CrudeHeaterGui(heater, new CrudeHeaterContainer(player.inventory, heater));
		case Reference.GUI_CRUDE_COUPLING:
			CrudeCouplingTileEntity coupling = (CrudeCouplingTileEntity) te;
			System.out.println("Opening copuling");
			return new CrudeCouplingGui(coupling, new CrudeCouplingContainer(player.inventory, coupling));

		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		switch (ID) {
		case Reference.GUI_CRUDE_IO:
			return new CrudeIOContainer(player.inventory, (CrudeIOTileEntity) tile);
		case Reference.GUI_CRUDE_HEATER:
			return new CrudeHeaterContainer(player.inventory, (CrudeHeaterTileEntity) tile);
		case Reference.GUI_CRUDE_COUPLING:
			return new CrudeCouplingContainer(player.inventory, (CrudeCouplingTileEntity) tile);
		}
		return null;
	}
}