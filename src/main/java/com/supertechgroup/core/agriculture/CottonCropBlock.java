package com.supertechgroup.core.agriculture;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.EnumPlantType;

public class CottonCropBlock extends BlockCrops {

	public CottonCropBlock() {
		super();
		this.setUnlocalizedName(Reference.MODID + ".cotton");
		this.setRegistryName(Reference.MODID, "cotton");
	}

	@Override
	protected Item getCrop() {
		return ModRegistry.itemCotton;
	}

	@Override
	public EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}

	@Override
	protected Item getSeed() {
		return ModRegistry.itemCotton;
	}
}
