package com.supertechgroup.core.research;

import javax.annotation.Nullable;

import com.supertechgroup.core.util.BlockTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockResearchStation extends BlockTileEntity<TileEntityResearchStation> {

	private static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0, 0, 0, 1, 1.0625, 1);

	public BlockResearchStation() {
		super(Material.ROCK, "researchStation");
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityResearchStation tile = getTileEntity(world, pos);
			player.sendMessage(new TextComponentString("Team: " + tile.getTeam().getTeamName()));
		}
		return true;
	}

	@Nullable
	@Override
	public TileEntityResearchStation createTileEntity(World world, IBlockState state) {
		return new TileEntityResearchStation();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!worldIn.isRemote) {
			((TileEntityResearchStation) worldIn.getTileEntity(pos)).setTeam(
					ResearchSavedData.get(worldIn).findPlayersResearchTeam(((EntityPlayerMP) placer).getUniqueID()));
			worldIn.getTileEntity(pos).markDirty();
		}
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
