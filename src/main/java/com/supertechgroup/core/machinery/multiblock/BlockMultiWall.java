package com.supertechgroup.core.machinery.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockMultiWall extends Block implements ITileEntityProvider {

	public BlockMultiWall(Material materialIn) {
		super(materialIn);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileMultiBlock te = (TileMultiBlock) world.getTileEntity(pos);
			return te.onActivate(player, side);
		}
		return true;
	}

}
