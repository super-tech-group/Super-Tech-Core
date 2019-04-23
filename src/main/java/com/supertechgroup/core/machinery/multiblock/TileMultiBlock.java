package com.supertechgroup.core.machinery.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public abstract class TileMultiBlock extends TileEntity {
	BlockPos masterPos = new BlockPos(-1, -1, -1);

	protected abstract boolean blockActivated(EntityPlayer player, EnumFacing side);

	public boolean canInteractWith(EntityPlayer playerIn) {
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	public BlockPos getMasterPos() {
		return new BlockPos(masterPos);
	}

	public boolean onActivate(EntityPlayer player, EnumFacing side) {
		if (getMasterPos().equals(new BlockPos(-1, -1, -1))) {
			player.sendMessage(new TextComponentString("Not part of a valid MultiBlock Structure"));
			return false;
		} else {
			return this.blockActivated(player, side);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		int[] coordlist = compound.getIntArray("masterPos");
		if (coordlist.length == 3) {
			masterPos = new BlockPos(coordlist[0], coordlist[1], coordlist[2]);
		}

	}

	public void setMasterPos(BlockPos pos) {
		this.masterPos = pos;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		int[] coordlist = { masterPos.getX(), masterPos.getY(), masterPos.getZ() };
		compound.setIntArray("masterPos", coordlist);
		return compound;
	}

}
