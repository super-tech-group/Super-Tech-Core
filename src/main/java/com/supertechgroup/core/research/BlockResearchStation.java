package com.supertechgroup.core.research;

import javax.annotation.Nullable;

import com.supertechgroup.core.util.BlockTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockResearchStation extends BlockTileEntity<TileEntityResearchStation> {

	private static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0, 0, 0, 1, 1.0625, 1);

	public BlockResearchStation() {
		super(Material.ROCK, "researchStation");
	}

	@Nullable
	@Override
	public TileEntityResearchStation createTileEntity(World world, IBlockState state) {
		return new TileEntityResearchStation();
	}

	@Override
	public Class<TileEntityResearchStation> getTileEntityClass() {
		return TileEntityResearchStation.class;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BASE_AABB;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return BASE_AABB;
	}
}
