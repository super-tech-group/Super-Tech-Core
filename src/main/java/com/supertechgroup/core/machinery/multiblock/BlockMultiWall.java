package com.supertechgroup.core.machinery.multiblock;

import com.supertechgroup.core.util.BlockBase;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockMultiWall extends BlockBase implements ITileEntityProvider {

	public BlockMultiWall() {
		super(Material.ROCK, "multiWall");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileMultiBlock();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileMultiBlock te = (TileMultiBlock) world.getTileEntity(pos);
			te.onActivate(player, side);
		}
		return true;
	}

}
