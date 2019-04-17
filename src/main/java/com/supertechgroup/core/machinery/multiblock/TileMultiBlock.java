package com.supertechgroup.core.machinery.multiblock;

import com.supertechgroup.core.SuperTechCoreMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class TileMultiBlock extends TileEntity {
	BlockPos masterPos = new BlockPos(-1, -1, -1);

	public boolean hasGUI() {
		return false;
	}

	public int getGuiID() {
		return -1;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		int[] coordlist = { masterPos.getX(), masterPos.getY(), masterPos.getZ() };
		compound.setIntArray("masterPos", coordlist);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		int[] coordlist = compound.getIntArray("masterPos");
		if (coordlist.length == 3) {
			masterPos = new BlockPos(coordlist[0], coordlist[1], coordlist[2]);
		}

	}

	public BlockPos getMasterPos() {
		return new BlockPos(masterPos);
	}

	public void setMasterPos(BlockPos pos) {
		this.masterPos = pos;
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	public void onActivate(EntityPlayer player, EnumFacing side) {
		if (getMasterPos().equals(new BlockPos(-1, -1, -1))) {
			player.sendMessage(new TextComponentString("Not part of a valid MultiBlock Structure"));
		} else {
			if (!getMasterPos().equals(this.getPos())) {
				TileMultiBlock master = (TileMultiBlock) world.getTileEntity(getMasterPos());
				master.onActivate(player, side);
			} else {
				if (hasGUI()) {
					TileEntity te = world.getTileEntity(pos);
					System.out.println("te found " + te.writeToNBT(new NBTTagCompound()));
					player.openGui(SuperTechCoreMod.instance, getGuiID(), world, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
	}

}
