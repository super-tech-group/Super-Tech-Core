package com.supertechgroup.core.fluids;

import com.supertechgroup.core.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockModFluid extends BlockFluidClassic {

	private static boolean pushesEntity;

	public static boolean getPushesEntity() {
		return pushesEntity;
	}

	public static void setPushesEntity(boolean parPushesEntity) {
		pushesEntity = parPushesEntity;
	}

	public BlockModFluid(Fluid fluid, Material material) {
		super(fluid, material);
		this.setUnlocalizedName(fluid.getUnlocalizedName());
		this.setRegistryName(Reference.MODID, fluid.getName());
	}

	/**
	 * Checks if an additional {@code -6} vertical drag should be applied to the
	 * entity. See {#link net.minecraft.block.BlockLiquid#getFlow()}
	 */
	@Override
	public boolean causesDownwardCurrent(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		Material material = iblockstate.getMaterial();

		if (material == this.blockMaterial) {
			return false;
		} else if (side == EnumFacing.UP) {
			return true;
		} else if (material == Material.ICE) {
			return false;
		} else {
			boolean flag = isExceptBlockForAttachWithPiston(block) || block instanceof BlockStairs;
			return !flag && iblockstate.getBlockFaceShape(worldIn, pos, side) == BlockFaceShape.SOLID;
		}
	}

	protected int getDepth(IBlockState state) {
		return state.getMaterial() == this.blockMaterial ? state.getValue(LEVEL).intValue() : -1;
	}

	protected Vec3d getFlow(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		double d0 = 0.0D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		int i = this.getRenderedDepth(state);
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
			blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
			int j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos));

			if (j < 0) {
				if (!worldIn.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement()) {
					j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos.down()));

					if (j >= 0) {
						int k = j - (i - 8);
						d0 += enumfacing.getFrontOffsetX() * k;
						d1 += enumfacing.getFrontOffsetY() * k;
						d2 += enumfacing.getFrontOffsetZ() * k;
					}
				}
			} else if (j >= 0) {
				int l = j - i;
				d0 += enumfacing.getFrontOffsetX() * l;
				d1 += enumfacing.getFrontOffsetY() * l;
				d2 += enumfacing.getFrontOffsetZ() * l;
			}
		}

		Vec3d vec3d = new Vec3d(d0, d1, d2);

		if (state.getValue(LEVEL).intValue() >= 8) {
//	         // DEBUG
//	         System.out.println("fluid level greater than zero");

			for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
				blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing1);

				if (this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos, enumfacing1)
						|| this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos.up(), enumfacing1)) {
//	                 // DEBUG
//	                 System.out.println("Causes downward current");

					vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
					break;
				}
			}
		}

		blockpos$pooledmutableblockpos.release();
		return vec3d.normalize();
	}

	protected int getRenderedDepth(IBlockState state) {
		int i = this.getDepth(state);
		return i >= 8 ? 0 : i;
	}

	@Override
	public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
//	     // DEBUG
//	     System.out.println("modifyAcceleration for "+entityIn+" with isPushedByWater() = "+entityIn.isPushedByWater());

		if (getPushesEntity()) {
			Vec3d flowAdder = getFlow(worldIn, pos, worldIn.getBlockState(pos));

//	      // DEBUG
//	      System.out.println("may push entity with motion adder = "+flowAdder);

			return motion.add(flowAdder);
		} else {
//	      // DEBUG
//	      System.out.println("may not push entity");

			return motion;
		}
	}

	public void registerModels() {
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(this.getRegistryName().toString(),
				"inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), DEFAULT_ITEM_SUBTYPE,
				itemModelResourceLocation);
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());

	}
}
