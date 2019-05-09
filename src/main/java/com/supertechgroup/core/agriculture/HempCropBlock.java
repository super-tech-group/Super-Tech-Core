package com.supertechgroup.core.agriculture;

import java.util.Random;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.items.SuperTechItem;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class HempCropBlock extends BlockCrops {
	public static final PropertyInteger HEMP_AGE = PropertyInteger.create("age", 0, 3);
	private static final AxisAlignedBB[] HEMP_AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.5D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.875D, 1.0D) };

	public HempCropBlock() {
		super();
		this.setUnlocalizedName(Reference.MODID + ".hemp");
		this.setRegistryName(Reference.MODID, "hemp");
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { HEMP_AGE });
	}

	@Override
	protected PropertyInteger getAgeProperty() {
		return HEMP_AGE;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return HEMP_AABB[state.getValue(this.getAgeProperty()).intValue()];
	}

	@Override
	public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world,
			BlockPos pos, IBlockState state, int fortune) {
		int age = getAge(state);
		Random rand = world instanceof World ? ((World) world).rand : new Random();

		if (age >= getMaxAge()) {

			for (int i = 0; i < 3 + fortune; ++i) {
				if (rand.nextInt(2 * getMaxAge()) <= age) {
					drops.add(new ItemStack(ModRegistry.itemHempSeed));
				}
				drops.add(new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.HEMP_FIBER));
			}
		}
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	public EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}
}
